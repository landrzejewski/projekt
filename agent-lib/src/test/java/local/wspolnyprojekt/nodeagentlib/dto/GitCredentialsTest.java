package local.wspolnyprojekt.nodeagentlib.dto;

import local.wspolnyprojekt.nodeagentlib.dto.GitCredentials;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class GitCredentialsTest {
    GitCredentials credentials;
    String username = "hello";
    String password = "world";

    @BeforeEach
    void init() {
        credentials = new GitCredentials(username, password);
    }

    @Test
    void isUsernameOk() {
        assertThat(credentials.getUsername()).isEqualTo(username);
    }

    @Test
    void isPasswordOk() {
        assertThat(credentials.getPassword()).isEqualTo(password);
    }

    @Test
    void isJsonOk() {
        String expected = String.format("{\"username\":\"%s\",\"password\":\"%s\"}", username, password);
        assertThat(credentials.getJsonString()).isEqualTo(expected);
    }
}
