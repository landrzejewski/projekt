package local.wspolnyprojekt.nodeagent.restendpoints;

import local.wspolnyprojekt.nodeagent.Configuration;
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

    @PostMapping(RestEndpoints.GIT_CREDENTIALS_ENDPOINT)
    void setCredentials(@RequestBody GitCredentials gitCredentials) {
        gitClient.setCredentials(gitCredentials);
    }

    @PostMapping(RestEndpoints.GIT_ENDPOINT)
    void cloneGitRepository(@RequestBody GitResource gitResource, @PathVariable(name = TASKID_PATH_VARIABLE) String taskid) throws GitAPIException {
        gitClient.clone(gitResource, Configuration.getWorkspaceDir(taskid));
    }

    @PutMapping(RestEndpoints.GIT_ENDPOINT)
    boolean pullAndCheckoutGitRepository(@PathVariable(name = TASKID_PATH_VARIABLE) String taskid) throws GitAPIException, IOException {
        return gitClient.pull(Configuration.getWorkspaceDir(taskid));
    }

}
