package local.wspolnyprojekt.nodeagent.git;

import local.wspolnyprojekt.nodeagentlib.common.GitResource;
import lombok.RequiredArgsConstructor;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import java.io.File;
import java.io.IOException;

@ApplicationScope
@Component
@RequiredArgsConstructor
public class JGitClient implements GitClient {

    private final Credentials credentials;

    @Override
    public void clone(GitResource gitResource, File workspace) throws GitAPIException {
        var gitCommand = Git.cloneRepository()
                .setURI(gitResource.getRepositoryUrl())
                .setBranch(gitResource.getBranch())
                .setDirectory(workspace);
        if (credentials.getCredentials() != null) {
            gitCommand = gitCommand.setCredentialsProvider(credentials.getCredentials());
        }
        gitCommand.call().close();
    }

    @Override
    public boolean pull(File workspace) throws IOException, GitAPIException {
        try (var gitCommand = Git.open(workspace)) {
            return gitCommand.pull().call().isSuccessful();
        }
    }

    @Override
    public void setCredentials(String user, String password) {
        credentials.setCredentials(user, password);
    }
}
