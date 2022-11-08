package com.example.restdockerplatform.rest.util;


import com.example.restdockerplatform.persistence.inMemory.node.NodeEntity;
import local.wspolnyprojekt.nodeagentlib.dto.NodeHttpRequestMethod;
import local.wspolnyprojekt.nodeagentlib.dto.RequestDetails;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
public class HttpCommunicationService {


    public static ResponseEntity<?> sendRequest(NodeEntity node, RequestDetails details) {

        final String payload = details.getJsonPayload();
        final String url = prepareUrl(node, details);
        final HttpMethod method = determineMethod(details.getRequestMethod());
        final HttpEntity<String> request = prepareRequest(payload);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<?> response = restTemplate.exchange(url, method, request, String.class);

        return response;
    }

    private static HttpMethod determineMethod(NodeHttpRequestMethod requestMethod) {

        return HttpMethod.resolve(requestMethod.name());
    }


    private static HttpEntity<String> prepareRequest(String payload) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new HttpEntity<>(payload, headers);
    }


    private static String prepareUrl(NodeEntity node, RequestDetails details) {

        return String.format("http://%s:%s%s", node.getHost(), node.getPort(), details.getUriEndpoint());
    }


}
