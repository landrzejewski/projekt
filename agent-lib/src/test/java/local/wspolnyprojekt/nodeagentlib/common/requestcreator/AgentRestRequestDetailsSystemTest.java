package local.wspolnyprojekt.nodeagentlib.common.requestcreator;

import local.wspolnyprojekt.nodeagentlib.common.RequestDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.RequestMethod;

import static local.wspolnyprojekt.nodeagentlib.AgentRestRequestDetails.*;
import static local.wspolnyprojekt.nodeagentlib.common.RestEndpoints.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class AgentRestRequestDetailsSystemTest {

    @BeforeEach
    void init() {
    }

    @Test
    void shouldCreatePingRequestDetails() {
        RequestDetails requestDetails = getPingRequestDetails();
        assertThat(requestDetails.getRequestMethod()).isEqualTo(RequestMethod.GET);
        assertThat(requestDetails.getJsonPayload()).isEmpty();
        assertThat(requestDetails.getUriEndpoint()).isEqualTo(SYSTEM_PING);
    }

    @Test
    void shouldCreateSystemLoadRequestDetails() {
        RequestDetails requestDetails = getSystemLoadRequestDetails();
        assertThat(requestDetails.getRequestMethod()).isEqualTo(RequestMethod.GET);
        assertThat(requestDetails.getJsonPayload()).isEmpty();
        assertThat(requestDetails.getUriEndpoint()).isEqualTo(SYSTEM_LOAD);
    }
}
