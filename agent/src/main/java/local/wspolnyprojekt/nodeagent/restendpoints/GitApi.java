package local.wspolnyprojekt.nodeagent.restendpoints;

import local.wspolnyprojekt.nodeagent.task.TasksService;
import local.wspolnyprojekt.nodeagentlib.dto.GitCredentials;
import local.wspolnyprojekt.nodeagentlib.dto.RestEndpoints;
import local.wspolnyprojekt.nodeagentlib.dto.GitResource;
import local.wspolnyprojekt.nodeagent.git.GitClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static local.wspolnyprojekt.nodeagentlib.dto.RestEndpoints.TASKID_PATH_VARIABLE;

@Slf4j
@RestController
@RequiredArgsConstructor
public class GitApi {

    private final GitClient gitClient;
    private final TasksService tasksService;

    @PostMapping(RestEndpoints.GIT_CREDENTIALS_ENDPOINT)
    void setCredentials(@RequestBody GitCredentials gitCredentials) {
        log.info("GitCredentials: {}", gitCredentials);
        gitClient.setCredentials(gitCredentials);
    }

    @PostMapping(RestEndpoints.GIT_ENDPOINT)
    void cloneGitRepository(@RequestBody GitResource gitResource, @PathVariable(name = TASKID_PATH_VARIABLE) String taskid) {
        log.info("GitClone: {}", gitResource);
        tasksService.clone(gitResource, taskid);
    }

    @PutMapping(RestEndpoints.GIT_ENDPOINT)
    void pullAndCheckoutGitRepository(@PathVariable(name = TASKID_PATH_VARIABLE) String taskid) throws GitAPIException, IOException {
        tasksService.pull(tasksService.getTask(taskid));
    }

}
