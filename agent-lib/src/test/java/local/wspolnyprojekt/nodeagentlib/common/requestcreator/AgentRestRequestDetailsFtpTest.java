package local.wspolnyprojekt.nodeagentlib.common.requestcreator;

import local.wspolnyprojekt.nodeagentlib.common.RequestDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.RequestMethod;

import static local.wspolnyprojekt.nodeagentlib.common.RestEndpoints.*;
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
        assertThat(requestDetails.getRequestMethod()).isEqualTo(RequestMethod.GET);
        assertThat(requestDetails.getJsonPayload()).isEmpty();
        assertThat(requestDetails.getUriEndpoint()).isEqualTo(FTP_ENDPOINT.replace("{" + TASKID_PATH_VARIABLE + "}", taskId).replace("{*" + FILENAME_PATH_VARIABLE + "}", file));
    }

    @Test
    void shouldCreateFtpPostRequestDetails() {
        RequestDetails requestDetails = getFtpPostFileRequestDetaild(file, taskId);
        assertThat(requestDetails.getRequestMethod()).isEqualTo(RequestMethod.POST);
        assertThat(requestDetails.getJsonPayload()).isEmpty();
        assertThat(requestDetails.getUriEndpoint()).isEqualTo(FTP_ENDPOINT.replace("{" + TASKID_PATH_VARIABLE + "}", taskId).replace("{*" + FILENAME_PATH_VARIABLE + "}", file));
    }

    @Test
    void shouldCreateFtpDeleteRequestDetails() {
        RequestDetails requestDetails = getFtpDeleteFileRequestDetaild(file, taskId);
        assertThat(requestDetails.getRequestMethod()).isEqualTo(RequestMethod.DELETE);
        assertThat(requestDetails.getJsonPayload()).isEmpty();
        assertThat(requestDetails.getUriEndpoint()).isEqualTo(FTP_ENDPOINT.replace("{" + TASKID_PATH_VARIABLE + "}", taskId).replace("{*" + FILENAME_PATH_VARIABLE + "}", file));
    }
}
