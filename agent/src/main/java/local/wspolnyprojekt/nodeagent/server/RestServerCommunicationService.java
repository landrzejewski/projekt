package local.wspolnyprojekt.nodeagent.server;

import local.wspolnyprojekt.nodeagent.configuration.ConfigurationPersistence;
import local.wspolnyprojekt.nodeagent.configuration.NodeConfigurationProperties;
import local.wspolnyprojekt.nodeagent.configuration.NodeInternetInterface;
import local.wspolnyprojekt.nodeagentlib.dto.TaskLogMessage;
import local.wspolnyprojekt.nodeagentlib.dto.NodeRegistrationEntity;
import local.wspolnyprojekt.nodeagentlib.dto.TaskStatusMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.UUID;

@Service
@ApplicationScope
@RequiredArgsConstructor
@Slf4j
public class RestServerCommunicationService implements ServerCommunicationService {

    private final NodeInternetInterface nodeInternetInterface;
    private final NodeConfigurationProperties nodeConfigurationProperties;
    private final ConfigurationPersistence configurationPersistence;
    private boolean registered = false;

    @Override
    public void sendTaskLog(TaskLogMessage taskLogMessage) {
        sendRestPostRequest(nodeConfigurationProperties.getTaskLogUrl(taskLogMessage.getSenderId()), taskLogMessage.getJsonString());
    }

    @Override
    public void sendTaskStatus(TaskStatusMessage taskStatusMessage) {
        sendRestPostRequest(nodeConfigurationProperties.getTaskStatusUrl(taskStatusMessage.getTaskId()), taskStatusMessage.getJsonString());
    }

    @Override
    public void registerAgent() {
        String agentId = configurationPersistence.load(nodeConfigurationProperties.getConfigurationAgentIdKey()).orElse(UUID.randomUUID().toString());
        registerAgent(agentId, nodeInternetInterface.getIp(), nodeInternetInterface.getPort());
    }


    private void registerAgent(String agentId, String host, int port) {
        NodeRegistrationEntity nodeRegistrationEntity = new NodeRegistrationEntity();
        nodeRegistrationEntity.setNodeId(agentId);
        nodeRegistrationEntity.setHost(host);
        nodeRegistrationEntity.setPort(port);
        try {
            sendRestPostRequest(nodeConfigurationProperties.getRegisterUrl(), nodeRegistrationEntity.getJsonString());
            registered = true;
            configurationPersistence.save(nodeConfigurationProperties.getConfigurationAgentIdKey(), agentId);
        } catch (Exception e) {
            log.error("{}", e.getMessage());
        }
    }

    @Override
    public boolean isRegistered() {
        return registered;
    }

    private void sendRestPostRequest(String endpoint, String payload) {
        var restTemplate = new RestTemplate();
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(payload, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(endpoint, request, String.class);
//        log.info("request result: {} -> {}", response.getBody(), response.getStatusCode());
    }

}
