package local.wspolnyprojekt.nodeagentlib;

import local.wspolnyprojekt.nodeagentlib.dto.AgentRequestMethod;
import local.wspolnyprojekt.nodeagentlib.dto.RequestDetails;
import local.wspolnyprojekt.nodeagentlib.dto.RestEndpoints;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static local.wspolnyprojekt.nodeagentlib.AgentRestRequestDetails.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class AgentRestRequestDetailsFtpTest {
    String taskId = "simpleTaskId";
    String file = "hello/world";

    @BeforeEach
    void init() {
    }

    @Test
    void shouldCreateFtpGetRequestDetails() {
        RequestDetails requestDetails = getFtpGetFileRequestDetaild(file, taskId);
        assertThat(requestDetails.getRequestMethod()).isEqualTo(AgentRequestMethod.GET);
        assertThat(requestDetails.getJsonPayload()).isEmpty();
        assertThat(requestDetails.getUriEndpoint()).isEqualTo(RestEndpoints.FTP_ENDPOINT.replace("{" + RestEndpoints.TASKID_PATH_VARIABLE + "}", taskId).replace("{*" + RestEndpoints.FILENAME_PATH_VARIABLE + "}", file));
    }

    @Test
    void shouldCreateFtpPostRequestDetails() {
        RequestDetails requestDetails = getFtpPostFileRequestDetaild(file, taskId);
        assertThat(requestDetails.getRequestMethod()).isEqualTo(AgentRequestMethod.POST);
        assertThat(requestDetails.getJsonPayload()).isEmpty();
        assertThat(requestDetails.getUriEndpoint()).isEqualTo(RestEndpoints.FTP_ENDPOINT.replace("{" + RestEndpoints.TASKID_PATH_VARIABLE + "}", taskId).replace("{*" + RestEndpoints.FILENAME_PATH_VARIABLE + "}", file));
    }

    @Test
    void shouldCreateFtpDeleteRequestDetails() {
        RequestDetails requestDetails = getFtpDeleteFileRequestDetaild(file, taskId);
        assertThat(requestDetails.getRequestMethod()).isEqualTo(AgentRequestMethod.DELETE);
        assertThat(requestDetails.getJsonPayload()).isEmpty();
        assertThat(requestDetails.getUriEndpoint()).isEqualTo(RestEndpoints.FTP_ENDPOINT.replace("{" + RestEndpoints.TASKID_PATH_VARIABLE + "}", taskId).replace("{*" + RestEndpoints.FILENAME_PATH_VARIABLE + "}", file));
    }
}
