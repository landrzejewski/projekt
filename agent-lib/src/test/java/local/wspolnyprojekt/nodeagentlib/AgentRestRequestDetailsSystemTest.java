package local.wspolnyprojekt.nodeagentlib;

import local.wspolnyprojekt.nodeagentlib.dto.NodeHttpRequestMethod;
import local.wspolnyprojekt.nodeagentlib.dto.RequestDetails;
import local.wspolnyprojekt.nodeagentlib.dto.RestEndpoints;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static local.wspolnyprojekt.nodeagentlib.AgentRestRequestDetails.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class AgentRestRequestDetailsSystemTest {

    @BeforeEach
    void init() {
    }

    @Test
    void shouldCreatePingRequestDetails() {
        RequestDetails requestDetails = getPingRequestDetails();
        assertThat(requestDetails.getRequestMethod()).isEqualTo(NodeHttpRequestMethod.GET);
        assertThat(requestDetails.getJsonPayload()).isEmpty();
        assertThat(requestDetails.getUriEndpoint()).isEqualTo(RestEndpoints.SYSTEM_PING);
    }

    @Test
    void shouldCreateSystemLoadRequestDetails() {
        RequestDetails requestDetails = getSystemLoadRequestDetails();
        assertThat(requestDetails.getRequestMethod()).isEqualTo(NodeHttpRequestMethod.GET);
        assertThat(requestDetails.getJsonPayload()).isEmpty();
        assertThat(requestDetails.getUriEndpoint()).isEqualTo(RestEndpoints.SYSTEM_LOAD);
    }
}
