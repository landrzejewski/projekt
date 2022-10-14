package local.wspolnyprojekt.nodeagent.git;

import local.wspolnyprojekt.nodeagent.task.Task;
import local.wspolnyprojekt.nodeagentlib.dto.GitCredentials;
import local.wspolnyprojekt.nodeagentlib.dto.GitResource;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.IOException;

public interface GitClient {
    void clone(GitResource gitResource, Task task) throws GitAPIException;

    boolean pull(Task task) throws IOException, GitAPIException;

    void setCredentials(String user, String password);

    default void setCredentials(GitCredentials gitCredentials) {
        setCredentials(gitCredentials.getUsername(), gitCredentials.getPassword());
    }

}
