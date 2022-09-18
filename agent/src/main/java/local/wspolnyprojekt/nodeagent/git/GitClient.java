package local.wspolnyprojekt.nodeagent.git;

import local.wspolnyprojekt.nodeagent.common.GitCredentials;
import local.wspolnyprojekt.nodeagent.common.GitResource;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;
import java.io.IOException;

public interface GitClient {
    void clone(GitResource gitResource, File workspace) throws GitAPIException;

    boolean pull(File workspace) throws IOException, GitAPIException;

    void setCredentials(String user, String password);

    default void setCredentials(GitCredentials gitCredentials) {
        setCredentials(gitCredentials.getUsername(), gitCredentials.getPassword());
    }

}
