package local.wspolnyprojekt.nodeagent.configuration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


// Plik z konfiguracją na potrzeby testów w src/test/resources/application.properties
@SpringBootTest(classes = NodeConfigurationProperties.class)
class NodeConfigurationPropertiesTest {

    @Autowired
    private NodeConfigurationProperties nodeConfigurationProperties;

    @Test
    void shouldReturnCorrectWorkspaceDirectory() {
        assertThat(nodeConfigurationProperties.getWorkspaceDirectory()).isEqualTo("tmp");
    }

    @Test
    void shouldReturnCorrectServerUrl() {
        assertThat(nodeConfigurationProperties.getServerUrl()).isEqualTo("http://127.0.0.1:8080");
    }

    @Test
    void shouldReturnCorrectRegisterUrl() {
        assertThat(nodeConfigurationProperties.getRegisterUrl()).contains("http://127.0.0.1:8080/api/node/\\{nodeid\\}");
    }

    @Test
    void shouldReturnCorrectTaskLogUrlWithTaskIdInIt() {
        String taskId = UUID.randomUUID().toString();
        assertThat(nodeConfigurationProperties.getTaskLogUrl(taskId)).isEqualTo("http://127.0.0.1:8080/tasklog/"+taskId);
    }

    @Test
    void shouldReturnCorrectTaskStatusUrlWithTaskIdInIt() {
        String taskId = UUID.randomUUID().toString();
        assertThat(nodeConfigurationProperties.getTaskStatusUrl(taskId)).isEqualTo("http://127.0.0.1:8080/taskstatus/"+taskId);
    }

    @Test
    void shouldReturnCorrectConfigurationPersistenceFileName() {
        assertThat(nodeConfigurationProperties.getConfigurationPersistenceFileName()).isEqualTo("tmp\\configuration.properties");
    }

    @Test
    void shouldReturnCorrectConfigurationAgentIdKey() {
        assertThat(nodeConfigurationProperties.getConfigurationAgentIdKey()).isEqualTo("agent.id");
    }

    @Test
    void shouldReturnCorrectTaskAutorunState() {
        assertThat(nodeConfigurationProperties.getTaskAutorun()).isTrue();
    }
}
