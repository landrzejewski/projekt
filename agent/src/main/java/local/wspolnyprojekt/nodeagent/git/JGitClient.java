package local.wspolnyprojekt.nodeagent.git;

import local.wspolnyprojekt.nodeagent.task.Task;
import local.wspolnyprojekt.nodeagent.task.state.TaskStateAllocated;
import local.wspolnyprojekt.nodeagent.task.state.TaskStateReady;
import local.wspolnyprojekt.nodeagent.workspaceutils.WorkspaceUtils;
import local.wspolnyprojekt.nodeagentlib.dto.GitResource;
import lombok.RequiredArgsConstructor;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import java.io.IOException;

@ApplicationScope
@Component
@RequiredArgsConstructor
public class JGitClient implements GitClient {

    private final Credentials credentials;
    private final WorkspaceUtils workspaceUtils;

    @Async
    @Override
    public void clone(GitResource gitResource, Task task) throws GitAPIException {
        if (task.getSemaphore().tryAcquire()) {
            task.setStatus(new TaskStateAllocated());
            var gitCommand = Git.cloneRepository()
                    .setURI(gitResource.getRepositoryUrl())
                    .setBranch(gitResource.getBranch())
                    .setDirectory(task.getWorkspaceAsFile());
            if (credentials.getGitCredentials() != null) {
                gitCommand = gitCommand.setCredentialsProvider(credentials.getGitCredentials());
            }
            gitCommand.call().close();
            task.setStatus(new TaskStateReady());
            task.getSemaphore().release();
        } else {
            throw new RuntimeException("BUSY");
        }
    }

    @Override
    public boolean pull(Task task) throws IOException, GitAPIException {
        try (var gitCommand = Git.open(task.getWorkspaceAsFile())) {
            return gitCommand.pull().call().isSuccessful();
        }
    }

    @Override
    public void setCredentials(String user, String password) {
        credentials.setGitCredentials(user, password);
    }
}
