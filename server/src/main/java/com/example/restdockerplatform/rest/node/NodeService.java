package com.example.restdockerplatform.rest.node;

import com.example.restdockerplatform.git.GitHubConfigurationConfig;
import com.example.restdockerplatform.persistence.database.TaskService;
import com.example.restdockerplatform.persistence.inMemory.node.NodeEntity;
import com.example.restdockerplatform.persistence.inMemory.node.NodeRepository;
import com.example.restdockerplatform.persistence.inMemory.node.NodeStatus;
import com.example.restdockerplatform.rest.util.HttpCommunicationService;
import com.example.restdockerplatform.utils.DateTimeService;
import local.wspolnyprojekt.nodeagentlib.dto.NodeRegistrationEntity;
import local.wspolnyprojekt.nodeagentlib.dto.RequestDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.example.restdockerplatform.git.GitHubConfigurationConfig.GIT_EXTENSION;
import static local.wspolnyprojekt.nodeagentlib.AgentRestRequestDetails.gitCloneRequestDetails;
import static local.wspolnyprojekt.nodeagentlib.AgentRestRequestDetails.gitCredentialsRequestDetails;


@Service
@Slf4j
public class NodeService {

    private final NodeRepository nodeRepository;
    private final NodeBalancingService nodeBalancingService;
    private final TaskService taskService;
    private final DateTimeService dateTimeService;
    private final GitHubConfigurationConfig gitHubConfigurationConfig;


    NodeService(NodeRepository nodeRepository,
                NodeBalancingService nodeBalancingService,
                TaskService taskService,
                DateTimeService dateTimeService,
                GitHubConfigurationConfig gitHubConfigurationConfig) {
        this.nodeRepository = nodeRepository;
        this.nodeBalancingService = nodeBalancingService;
        this.taskService = taskService;
        this.dateTimeService = dateTimeService;
        this.gitHubConfigurationConfig = gitHubConfigurationConfig;
    }


    public String registerNode(NodeRegistrationEntity nodeFromRequest) {

        final NodeEntity nodeEntity = mapNode(nodeFromRequest, NodeStatus.ALIVE);

        // change statuses of all not finished tasks to fail
        // TODO analyse need of changing statuses of other tasks as well
        taskService.updateStatusesOfNotFinishedTasks(nodeEntity.getId());

        return nodeRepository.insertNode(nodeEntity);
    }


    public String updateNode(NodeRegistrationEntity nodeFromRequest) {

        final NodeEntity nodeEntity = mapNode(nodeFromRequest, NodeStatus.ALIVE);

        return nodeRepository.updateNode(nodeEntity);
    }


    private NodeEntity mapNode(NodeRegistrationEntity nodeFromRequest, NodeStatus nodeStatus) {

        return NodeEntity.builder()
                .id(nodeFromRequest.getNodeId())
                .host(nodeFromRequest.getHost())
                .port(nodeFromRequest.getPort())
                .lastUpdate(dateTimeService.getSystemDateTime())
                .status(nodeStatus)
                .build();
    }


    public String orderExecute(String user, String project, String taskId) {

        // ping all nodes
        nodeBalancingService.refreshNodesStatus();

        final NodeEntity node = nodeBalancingService.getNode()
                .orElseThrow(() -> new NoResourcesAvailableException("No resources to execute task"));

        // send request to provide git credentials
        sendGitCredentials(node);

        // send request to execute task
        sendTask(node, user, project, taskId);

        return node.getId();
    }


    private void sendTask(NodeEntity node, String user, String project, String taskId) {

        final String url = gitHubConfigurationConfig.getRepositoryURI() + project + GIT_EXTENSION;

        final RequestDetails requestDetails = gitCloneRequestDetails(url, user, taskId);

        HttpCommunicationService.sendRequest(node, requestDetails);
    }


    private void sendGitCredentials(NodeEntity node) {

        final RequestDetails gitCredentials = gitCredentialsRequestDetails(gitHubConfigurationConfig.getRepositoryToken(), "");

        HttpCommunicationService.sendRequest(node, gitCredentials);
    }

}
