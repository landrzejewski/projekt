package local.wspolnyprojekt.nodeagent.server;

import local.wspolnyprojekt.nodeagent.configuration.ConfigurationPersistence;
import local.wspolnyprojekt.nodeagent.configuration.NodeConfigurationProperties;
import local.wspolnyprojekt.nodeagent.configuration.NodeInternetInterface;
import local.wspolnyprojekt.nodeagentlib.dto.TaskLogMessage;
import local.wspolnyprojekt.nodeagentlib.dto.NodeRegistrationEntity;
import local.wspolnyprojekt.nodeagentlib.dto.TaskStatusMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

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
        sendRestPutRequest(nodeConfigurationProperties.getTaskLogUrl(taskLogMessage.getSenderId()), taskLogMessage.getJsonString());
    }

    @Override
    public void sendTaskStatus(TaskStatusMessage taskStatusMessage) {
        sendRestPutRequest(nodeConfigurationProperties.getTaskStatusUrl(taskStatusMessage.getTaskId()), taskStatusMessage.getJsonString());
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
            HttpStatus status = sendRestPostRequest(nodeConfigurationProperties.getRegisterUrl().replace("\\{nodeid\\}", agentId), nodeRegistrationEntity.getJsonString());
            if (status.is2xxSuccessful()) {
                registered = true;
                configurationPersistence.save(nodeConfigurationProperties.getConfigurationAgentIdKey(), agentId);
            }
        } catch (Exception e) {
            log.error("{}", e.getMessage());
        }
    }

    @Override
    public boolean isRegistered() {
        return registered;
    }

    private HttpStatus sendRestPostRequest(String endpoint, String payload) {
        return sendRestRequest(endpoint, payload, HttpMethod.POST).getStatusCode();
    }

    private HttpStatus sendRestPutRequest(String endpoint, String payload) {
        return sendRestRequest(endpoint, payload, HttpMethod.PUT).getStatusCode();
    }

    private ResponseEntity<String> sendRestRequest(String endpoint, String payload, HttpMethod method) {
        log.info("Endpoint: {}", endpoint);
        var restTemplate = new RestTemplate();
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(payload, headers);
        ResponseEntity<String> response = restTemplate.exchange(endpoint, method, request, String.class);
        return response;
    }

    @Scheduled(fixedDelay = 3, timeUnit = TimeUnit.SECONDS)
    private void checkIfServerIsOnlineAndRegister() {
        // TODO Nie ma na razie endpointa PING na serwerze, więc nie ma sprawdzania
        // Tylko rejestrowanie przy starcie
        if (!isRegistered()) {
            registerAgent();
        }
    }

}
