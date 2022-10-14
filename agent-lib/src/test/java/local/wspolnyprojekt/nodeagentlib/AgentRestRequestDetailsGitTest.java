package local.wspolnyprojekt.nodeagentlib;

import local.wspolnyprojekt.nodeagentlib.dto.AgentRequestMethod;
import local.wspolnyprojekt.nodeagentlib.dto.GitCredentials;
import local.wspolnyprojekt.nodeagentlib.dto.RequestDetails;
import local.wspolnyprojekt.nodeagentlib.dto.RestEndpoints;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static local.wspolnyprojekt.nodeagentlib.AgentRestRequestDetails.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class AgentRestRequestDetailsGitTest {
    String taskId = "simpleTaskId";

    @BeforeEach
    void init() {
    }

    @Test
    void shouldCreateGitCredentialsRequestDetails() {
        String username = "hello";
        String password = "world";
        GitCredentials credentials = new GitCredentials(username, password);
        RequestDetails requestDetails = gitCredentialsRequestDetails(username, password);
        assertThat(requestDetails.getRequestMethod()).isEqualTo(AgentRequestMethod.POST);
        assertThat(requestDetails.getUriEndpoint()).isEqualTo(RestEndpoints.GIT_CREDENTIALS_ENDPOINT);
        assertThat(requestDetails.getJsonPayload()).isEqualTo(credentials.getJsonString());
    }

    @Test
    void shouldCreateGitCloneRequestDetails() {
        String repository = "repository";
        String branch = "branch";
        RequestDetails requestDetails = gitCloneRequestDetails(repository, branch, taskId);
        assertThat(requestDetails.getRequestMethod()).isEqualTo(AgentRequestMethod.POST);
        assertThat(requestDetails.getUriEndpoint()).isEqualTo(RestEndpoints.GIT_ENDPOINT.replace("{" + RestEndpoints.TASKID_PATH_VARIABLE + "}", taskId));
        assertThat(requestDetails.getJsonPayload()).isEqualTo(String.format("{\"repositoryUrl\":\"%s\",\"branch\":\"%s\"}", repository, branch));
    }

    @Test
    void shouldCreateGitPullRequestDetails() {
        RequestDetails requestDetails = gitPullRequestDetails(taskId);
        assertThat(requestDetails.getRequestMethod()).isEqualTo(AgentRequestMethod.PUT);
        assertThat(requestDetails.getJsonPayload()).isEmpty();
        assertThat(requestDetails.getUriEndpoint()).isEqualTo(RestEndpoints.GIT_ENDPOINT.replace("{" + RestEndpoints.TASKID_PATH_VARIABLE + "}", taskId));
    }

    @Test
    void shouldCreateSystemLoadRequestDetails() {
        RequestDetails requestDetails = getSystemLoadRequestDetails();
        assertThat(requestDetails.getRequestMethod()).isEqualTo(AgentRequestMethod.GET);
        assertThat(requestDetails.getJsonPayload()).isEmpty();
        assertThat(requestDetails.getUriEndpoint()).isEqualTo(RestEndpoints.SYSTEM_LOAD);
    }
}
