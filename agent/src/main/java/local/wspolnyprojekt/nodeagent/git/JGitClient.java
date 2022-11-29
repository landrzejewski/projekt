package local.wspolnyprojekt.nodeagent.git;

import local.wspolnyprojekt.nodeagent.task.Task;
import local.wspolnyprojekt.nodeagent.task.TaskAutorunService;
import local.wspolnyprojekt.nodeagent.task.state.TaskStateAllocated;
import local.wspolnyprojekt.nodeagent.task.state.TaskStateFail;
import local.wspolnyprojekt.nodeagent.task.state.TaskStateReady;
import local.wspolnyprojekt.nodeagentlib.dto.GitResource;
import lombok.RequiredArgsConstructor;
import org.eclipse.jgit.api.Git;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

@ApplicationScope
@Component
@RequiredArgsConstructor
public class JGitClient implements GitClient {

    private final Credentials credentials;
    private final TaskAutorunService taskAutorunService;

    @Async
    @Override
    public void clone(GitResource gitResource, Task task) {
        if (task.getSemaphore().tryAcquire()) {
            task.setStatus(new TaskStateAllocated());
            var gitCommand = Git.cloneRepository()
                    .setURI(gitResource.getRepositoryUrl())
                    .setBranch(gitResource.getBranch())
                    .setDirectory(task.getWorkspaceAsFile());
            if (credentials.getGitCredentials() != null) {
                gitCommand = gitCommand.setCredentialsProvider(credentials.getGitCredentials());
            }
            try {
                gitCommand.call().close();
                task.setStatus(new TaskStateReady());
                if(task.isAutorun()) {
                    taskAutorunService.addToAutorun(task);
                }
            } catch (Exception e) {
                task.setStatus(new TaskStateFail(), e.getMessage());
            } finally {
                task.getSemaphore().release();
            }
        } else {
            throw new RuntimeException("BUSY");
        }
    }

    @Async
    @Override
    public void pull(Task task) {
        if (task.getSemaphore().tryAcquire()) {
            task.setStatus(new TaskStateAllocated());
            try (var gitCommand = Git.open(task.getWorkspaceAsFile())) {
                gitCommand.pull().call().isSuccessful();
                task.setStatus(new TaskStateReady());
            } catch (Exception e) {
                task.setStatus(new TaskStateFail(), e.getMessage());
            } finally {
                task.getSemaphore().release();
            }
        } else {
            throw new RuntimeException("BUSY");
        }
    }

    @Override
    public void setCredentials(String user, String password) {
        credentials.setGitCredentials(user, password);
    }
}
