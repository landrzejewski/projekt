package local.wspolnyprojekt.nodeagent.git;

import local.wspolnyprojekt.nodeagent.task.Task;
import local.wspolnyprojekt.nodeagentlib.dto.GitCredentials;
import local.wspolnyprojekt.nodeagentlib.dto.GitResource;

public interface GitClient {
    void clone(GitResource gitResource, Task task);

    void pull(Task task);

    void setCredentials(String user, String password);

    default void setCredentials(GitCredentials gitCredentials) {
        setCredentials(gitCredentials.getUsername(), gitCredentials.getPassword());
    }

}
