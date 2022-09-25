package com.example.restdockerplatform.node;

import local.wspolnyprojekt.nodeagentlib.AgentRestRequestDetails;
import local.wspolnyprojekt.nodeagentlib.dto.NodeLoad;
import local.wspolnyprojekt.nodeagentlib.dto.RequestDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Component
public class LoadBalancer {

    private NodeService nodeService;
    private static Long FREE_MEMORY_SIZE;
    private static Long AVAILABLE_DISK_SPACE;
    private static final long TIMEOUT = 1000;

    LoadBalancer(NodeService nodeService,
                 @Value("${node.free.memory.size}") String freeMemorySize,
                 @Value("${node.available.disk.space}") String availableDiskSpace){
        this.nodeService = nodeService;
        FREE_MEMORY_SIZE = Long.parseLong(freeMemorySize);
        AVAILABLE_DISK_SPACE = Long.parseLong(availableDiskSpace);
    }

    public Node getNodeForTask(){
        //naiwny algorytm przechodzący po nodach w liście, zwraca pierwszego noda spełniającego warunki
        int iteration = 0;
        Node node;
        while((node = nodeService.getNextNode(iteration)) != null){
            WebClient webClient = prepareWebClientForNode(node);
            if (checkNodeAvailability(webClient) && checkNodeResources(webClient)) {
                return node;
            }
            iteration++;
        }

        return null;
    }

    private WebClient prepareWebClientForNode(Node node){
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host(node.getHost())
                .port(node.getPort())
                .build();
        return WebClient.create(uriComponents.toUriString());
    }

    private boolean checkNodeAvailability(WebClient webClient){

        RequestDetails requestDetails = AgentRestRequestDetails.getPingRequestDetails();
        return Boolean.TRUE.equals(webClient.get()
                .uri(requestDetails.getUriEndpoint())
                .exchangeToMono(clientResponse -> Mono.just(clientResponse.statusCode().equals(HttpStatus.OK)))
                .onErrorReturn(false)
                .block(Duration.of(TIMEOUT, ChronoUnit.MILLIS)));
    }

    private boolean checkNodeResources(WebClient webClient){
        boolean res = false;
        RequestDetails requestDetails = AgentRestRequestDetails.getSystemLoadRequestDetails();
        Optional<NodeLoad> nodeLoad =
                webClient.get()
                .uri(requestDetails.getUriEndpoint())
                .retrieve()
                .bodyToMono(NodeLoad.class)
                .onErrorReturn(null)
                .blockOptional(Duration.of(TIMEOUT, ChronoUnit.MILLIS));

        if (nodeLoad.isPresent()) {
            res = checkNode(nodeLoad.get());
        }
       return res;
    }

    private boolean checkNode(NodeLoad node){
        return (node.getFreeMemorySize() > FREE_MEMORY_SIZE
                && node.getAvailableDiskSpace() > AVAILABLE_DISK_SPACE);
    }
}
