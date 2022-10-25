package local.wspolnyprojekt.nodeagentlib.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestDetails {
    NodeHttpRequestMethod requestMethod;
    String uriEndpoint;
    String jsonPayload;
}
