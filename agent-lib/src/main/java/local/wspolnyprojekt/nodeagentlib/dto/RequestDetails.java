package local.wspolnyprojekt.nodeagentlib.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestDetails {
    AgentRequestMethod requestMethod;
    String uriEndpoint;
    String jsonPayload;
}
