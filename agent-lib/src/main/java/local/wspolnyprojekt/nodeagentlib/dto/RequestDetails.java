package local.wspolnyprojekt.nodeagentlib.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestMethod;

@Data
@Builder
public class RequestDetails {
    RequestMethod requestMethod;
    String uriEndpoint;
    String jsonPayload;
}
