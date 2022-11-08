package com.example.restdockerplatform.rest.node;

import com.example.restdockerplatform.persistence.inMemory.node.NodeEntity;
import com.example.restdockerplatform.persistence.inMemory.node.NodeRepository;
import com.example.restdockerplatform.persistence.inMemory.node.NodeStatus;
import com.example.restdockerplatform.rest.util.HttpCommunicationService;
import com.example.restdockerplatform.utils.DateTimeService;
import local.wspolnyprojekt.nodeagentlib.dto.RequestDetails;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static local.wspolnyprojekt.nodeagentlib.AgentRestRequestDetails.getPingRequestDetails;


@Service
public class NodeBalancingService {

    private final NodeRepository nodeRepository;
    private final DateTimeService dateTimeService;


    public NodeBalancingService(NodeRepository nodeRepository,
                                DateTimeService dateTimeService) {
        this.nodeRepository = nodeRepository;
        this.dateTimeService = dateTimeService;
    }


    public void refreshNodesStatus() {

        final RequestDetails requestDetails = getPingRequestDetails();

        // may be required to check all nodes not only ALIVE, but it may slow down this step considerably
        // not use parallel stream until thread pool can be specified
        final List<NodeEntity> notAvailableNodes = nodeRepository.getAllNodes().stream()
                .filter(node -> node.getStatus().equals(NodeStatus.ALIVE))
//                .filter(node -> HttpCommunicationService.sendRequest(node, requestDetails).getStatusCode() != HttpStatus.OK)
                .filter(node -> executeSinglePing(node, requestDetails))
                .toList();

        notAvailableNodes.stream()
                .peek(node -> {
                    node.setStatus(NodeStatus.DISCONNECTED);
                    node.setLastUpdate(dateTimeService.getSystemDateTime());
                })
                .forEach(node -> nodeRepository.updateNode(node));
    }


    private boolean executeSinglePing(NodeEntity node, RequestDetails requestDetails) {

        // do it with timeout

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Boolean> future = executor.submit(
                () ->
                        HttpCommunicationService.sendRequest(node, requestDetails).getStatusCode() != HttpStatus.OK
        );

        try {
            return future.get(2, TimeUnit.SECONDS);

        } catch (TimeoutException e) {
            future.cancel(true);
            return false;

        } catch (Exception e) {
            // handle other exceptions
            return false;

        } finally {
            executor.shutdownNow();
        }

    }


    public Optional<NodeEntity> getNode() {

        return nodeRepository.getAllNodes().stream()
                .filter(node -> node.getStatus().equals(NodeStatus.ALIVE))
                .sorted(Comparator.comparing(NodeEntity::getLastUpdate))
                .findFirst();
    }


}
