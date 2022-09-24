package local.wspolnyprojekt.nodeagent.git;

import local.wspolnyprojekt.nodeagentlib.dto.GitCredentials;
import lombok.Getter;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

@Component
@ApplicationScope //TODO Docelowo dla każdego taska będzie osobny (aby taski mogły pochodzić z różnych kont)
public class Credentials {
    @Getter
    private UsernamePasswordCredentialsProvider credentials;

    public void setCredentials(String username, String password) {
        credentials = new UsernamePasswordCredentialsProvider(username, password);
    }

    public void setCredentials(GitCredentials gitCredentials) {
        setCredentials(gitCredentials.getUsername(), gitCredentials.getPassword());
    }
}
