package local.wspolnyprojekt.nodeagent.restendpoints;

import local.wspolnyprojekt.nodeagent.Configuration;
import local.wspolnyprojekt.nodeagentlib.common.GitCredentials;
import local.wspolnyprojekt.nodeagentlib.common.RestEndpoints;
import local.wspolnyprojekt.nodeagentlib.common.GitResource;
import local.wspolnyprojekt.nodeagent.git.GitClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

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
    void cloneGitRepository(@RequestBody GitResource gitResource, @PathVariable String taskid) throws GitAPIException {
        gitClient.clone(gitResource, Configuration.getWorkspaceDir(taskid));
    }

    @PutMapping(RestEndpoints.GIT_ENDPOINT)
    boolean pullAndCheckoutGitRepository(@PathVariable String taskid) throws GitAPIException, IOException {
        return gitClient.pull(Configuration.getWorkspaceDir(taskid));
    }

}
