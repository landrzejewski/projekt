package local.wspolnyprojekt.nodeagentlib;

import local.wspolnyprojekt.nodeagentlib.dto.RequestDetails;
import local.wspolnyprojekt.nodeagentlib.dto.RestEndpoints;
import local.wspolnyprojekt.nodeagentlib.dto.TaskCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.RequestMethod;

import static local.wspolnyprojekt.nodeagentlib.AgentRestRequestDetails.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class AgentRestRequestDetailsTaskTest {
    String taskId = "simpleTaskId";

    @BeforeEach
    void init() {
    }

    @Test
    void shouldCreateTaskStartRequestDetailsTest() {
        RequestDetails requestDetails = getTaskStartRequestDetails(taskId);
        assertThat(requestDetails.getRequestMethod()).isEqualTo(RequestMethod.POST);
        assertThat(requestDetails.getUriEndpoint()).isEqualTo(RestEndpoints.TASK_ENDPOINT.replace("{" + RestEndpoints.TASKID_PATH_VARIABLE + "}", taskId));
        assertThat(requestDetails.getJsonPayload()).isEqualTo(TaskCommand.TASK_COMMAND_START.getJsonString());
    }

    @Test
    void shouldCreateTaskStopRequestDetailsTest() {
        RequestDetails requestDetails = getTaskStopRequestDetails(taskId);
        assertThat(requestDetails.getRequestMethod()).isEqualTo(RequestMethod.POST);
        assertThat(requestDetails.getUriEndpoint()).isEqualTo(RestEndpoints.TASK_ENDPOINT.replace("{" + RestEndpoints.TASKID_PATH_VARIABLE + "}", taskId));
        assertThat(requestDetails.getJsonPayload()).isEqualTo(TaskCommand.TASK_COMMAND_STOP.getJsonString());
    }

    @Test
    void shouldCreateTaskLogRequestDetailsTest() {
        RequestDetails requestDetails = getTaskLogRequestDetails(taskId);
        assertThat(requestDetails.getRequestMethod()).isEqualTo(RequestMethod.POST);
        assertThat(requestDetails.getUriEndpoint()).isEqualTo(RestEndpoints.TASK_ENDPOINT.replace("{" + RestEndpoints.TASKID_PATH_VARIABLE + "}", taskId));
        assertThat(requestDetails.getJsonPayload()).isEqualTo(TaskCommand.TASK_COMMAND_LOG.getJsonString());
    }

    @Test
    void shouldCreateTaskCleanupRequestDetailsTest() {
        RequestDetails requestDetails = getTaskCleanupRequestDetails(taskId);
        assertThat(requestDetails.getRequestMethod()).isEqualTo(RequestMethod.POST);
        assertThat(requestDetails.getUriEndpoint()).isEqualTo(RestEndpoints.TASK_ENDPOINT.replace("{" + RestEndpoints.TASKID_PATH_VARIABLE + "}", taskId));
        assertThat(requestDetails.getJsonPayload()).isEqualTo(TaskCommand.TASK_COMMAND_CLEANUP.getJsonString());
    }

    @Test
    void shouldCreateTaskStatusRequestDetailsTest() {
        RequestDetails requestDetails = getTaskStatusRequestDetails(taskId);
        assertThat(requestDetails.getRequestMethod()).isEqualTo(RequestMethod.POST);
        assertThat(requestDetails.getUriEndpoint()).isEqualTo(RestEndpoints.TASK_ENDPOINT.replace("{" + RestEndpoints.TASKID_PATH_VARIABLE + "}", taskId));
        assertThat(requestDetails.getJsonPayload()).isEqualTo(TaskCommand.TASK_COMMAND_STATUS.getJsonString());
    }

    @Test
    void shouldCreateTaskDeleteRequestDetailsTest() {
        RequestDetails requestDetails = getTaskDeleteRequestDetails(taskId);
        assertThat(requestDetails.getRequestMethod()).isEqualTo(RequestMethod.POST);
        assertThat(requestDetails.getUriEndpoint()).isEqualTo(RestEndpoints.TASK_ENDPOINT.replace("{" + RestEndpoints.TASKID_PATH_VARIABLE + "}", taskId));
        assertThat(requestDetails.getJsonPayload()).isEqualTo(TaskCommand.TASK_COMMAND_DELETE.getJsonString());
    }

}
