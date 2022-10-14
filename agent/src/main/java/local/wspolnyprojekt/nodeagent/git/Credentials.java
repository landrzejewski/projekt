package local.wspolnyprojekt.nodeagent.git;

import local.wspolnyprojekt.nodeagentlib.dto.GitCredentials;
import lombok.Getter;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

@Component
@ApplicationScope
public class Credentials {
    @Getter
    private UsernamePasswordCredentialsProvider gitCredentials;

    public void setGitCredentials(String username, String password) {
        gitCredentials = new UsernamePasswordCredentialsProvider(username, password);
    }

    public void setGitCredentials(GitCredentials gitCredentials) {
        setGitCredentials(gitCredentials.getUsername(), gitCredentials.getPassword());
    }
}
