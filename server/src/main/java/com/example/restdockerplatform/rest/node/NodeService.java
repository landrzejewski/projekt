package com.example.restdockerplatform.rest.node;

import com.example.restdockerplatform.persistence.database.TaskService;
import com.example.restdockerplatform.persistence.inMemory.node.NodeEntity;
import com.example.restdockerplatform.persistence.inMemory.node.NodeRepository;
import com.example.restdockerplatform.persistence.inMemory.node.NodeStatus;
import com.example.restdockerplatform.rest.util.HttpCommunicationService;
import com.example.restdockerplatform.utils.DateTimeService;
import local.wspolnyprojekt.nodeagentlib.dto.NodeRegistrationEntity;
import local.wspolnyprojekt.nodeagentlib.dto.RequestDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static local.wspolnyprojekt.nodeagentlib.AgentRestRequestDetails.gitCloneRequestDetails;
import static local.wspolnyprojekt.nodeagentlib.AgentRestRequestDetails.gitCredentialsRequestDetails;


@Service
@Slf4j
public class NodeService {


    private static String GIT_TOKEN;
    private final NodeRepository nodeRepository;
    private final NodeBalancingService nodeBalancingService;
    private final TaskService taskService;
    private final DateTimeService dateTimeService;


    NodeService(NodeRepository nodeRepository,
                NodeBalancingService nodeBalancingService,
                TaskService taskService,
                DateTimeService dateTimeService,
                @Value("${github.token}") String gitHubToken) {
        this.nodeRepository = nodeRepository;
        this.nodeBalancingService = nodeBalancingService;
        this.taskService = taskService;
        this.dateTimeService = dateTimeService;
        GIT_TOKEN = gitHubToken;
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


    public String orderExecute(String user, String project) {

        // ping all nodes
        nodeBalancingService.refreshNodesStatus();

        final Optional<NodeEntity> node = nodeBalancingService.getNode();

        if (node.isEmpty()) {

            throw new NoResourcesAvailableException("No resources to execute task");
        }

        // send request to provide git credentials
        sendGitCredentials(node.get());


        // send request to execute task
        String taskId = UUID.randomUUID().toString();
        sendTask(node.get(), user, project, taskId);

        return taskId;

        // nie podoba mi, że musze nadawać UUID taskowi
        // nie podoba mi się, ze musze przekazywac nodowi credentiale do repozytorium
        // nie podoba mi się wykorzystanie NodeHttpRequestMethod zamiast HttpMethod

        // HttpCommunicationService może byc komponentem a nie wykorzystywać metody statyczne

        // nalezy przygotować klasę konfiguracyjną czytająca z properties a nie podpinac @Value
    }

    private void sendTask(NodeEntity node, String user, String project, String taskId) {

        final RequestDetails requestDetails = gitCloneRequestDetails(project, user, taskId);

        HttpCommunicationService.sendRequest(node, requestDetails);
    }


    private void sendGitCredentials(NodeEntity node) {

        final RequestDetails gitCredentials = gitCredentialsRequestDetails(GIT_TOKEN, "");

        HttpCommunicationService.sendRequest(node, gitCredentials);
    }


}
