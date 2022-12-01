package local.wspolnyprojekt.nodeagent.git;

import local.wspolnyprojekt.nodeagentlib.dto.GitCredentials;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CredentialsTest {
    private final String username = "user";
    private final String password = "passwrd";
    private Credentials credentials;

    @BeforeEach
    void init() {
        credentials = new Credentials();
    }

    @Test
    void shouldSetPlainTextCredentials() {
        credentials.setGitCredentials(username, password);
        assertThat(credentials.getGitCredentials()).isInstanceOf(UsernamePasswordCredentialsProvider.class);
    }

    @Test
    void shouldSetCredentialsViaGitCredentialsObject() {
        credentials.setGitCredentials(new GitCredentials(username, password));
        assertThat(credentials.getGitCredentials()).isInstanceOf(UsernamePasswordCredentialsProvider.class);
    }

}
