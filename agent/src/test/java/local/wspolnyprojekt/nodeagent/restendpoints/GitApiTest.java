package local.wspolnyprojekt.nodeagent.restendpoints;

import local.wspolnyprojekt.nodeagent.git.GitClient;
import local.wspolnyprojekt.nodeagent.task.Task;
import local.wspolnyprojekt.nodeagent.task.TasksService;
import local.wspolnyprojekt.nodeagentlib.dto.GitCredentials;
import local.wspolnyprojekt.nodeagentlib.dto.GitResource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static local.wspolnyprojekt.nodeagentlib.AgentRestRequestDetails.gitCloneRequestDetails;
import static local.wspolnyprojekt.nodeagentlib.AgentRestRequestDetails.gitCredentialsRequestDetails;
import static local.wspolnyprojekt.nodeagentlib.AgentRestRequestDetails.gitPullRequestDetails;

import static local.wspolnyprojekt.nodeagentlib.dto.RestEndpoints.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(GitApi.class)
class GitApiTest {

    String taskId = UUID.randomUUID().toString();
    String username = "username";
    String password = "password";
    String gitRepository = "url_to_git_repository";
    String gitBranch = "branch";

    @Autowired
    MockMvc mvc;

    @MockBean
    TasksService tasksService;
    @MockBean
    GitClient gitClient;

    @Captor
    ArgumentCaptor<GitCredentials> gitCredentialsCaptor;
    @Captor
    ArgumentCaptor<GitResource> gitResourceCaptor;
    @Captor
    ArgumentCaptor<String> taskIdCaptor;
    @Captor
    ArgumentCaptor<Task> taskCaptor;

    @BeforeEach
    void init() {
        when(tasksService.getTask(taskId)).thenReturn(new Task(null, null, taskId));
    }

    @Test
    void shouldCallGitClientAfterGitCredentials() throws Exception {
        mvc.perform(
                post(GIT_CREDENTIALS_ENDPOINT)
                        .content(gitCredentialsRequestDetails(username, password).getJsonPayload())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
        verify(gitClient).setCredentials(gitCredentialsCaptor.capture());
        assertThat(gitCredentialsCaptor.getValue().getUsername()).isEqualTo(username);
        assertThat(gitCredentialsCaptor.getValue().getPassword()).isEqualTo(password);
    }

    @Test
    void shouldCallTaskServiceAfterGitClone() throws Exception {
        var gitCloneRequestDetails = gitCloneRequestDetails(gitRepository, gitBranch, taskId);
        mvc.perform(
                post(gitCloneRequestDetails.getUriEndpoint())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gitCloneRequestDetails.getJsonPayload())
        ).andExpect(status().isOk());
        verify(tasksService).clone(gitResourceCaptor.capture(), taskIdCaptor.capture());
        assertThat(gitResourceCaptor.getValue().getRepositoryUrl()).isEqualTo(gitRepository);
        assertThat(gitResourceCaptor.getValue().getBranch()).isEqualTo(gitBranch);
        assertThat(taskIdCaptor.getValue()).isEqualTo(taskId);
    }

    @Test
    void shouldCallTaskServiceAfterGitPull() throws Exception {
        var gitPullRequestDetails = gitPullRequestDetails(taskId);
        mvc.perform(
                put(gitPullRequestDetails.getUriEndpoint())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gitPullRequestDetails.getJsonPayload())
        ).andExpect(status().isOk());
        verify(tasksService).pull(taskCaptor.capture());
        assertThat(taskCaptor.getValue().getTaskId()).isEqualTo(taskId);
    }
}
