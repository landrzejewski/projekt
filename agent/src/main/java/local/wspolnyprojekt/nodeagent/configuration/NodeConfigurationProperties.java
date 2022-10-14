package local.wspolnyprojekt.nodeagent.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@RequiredArgsConstructor
public class NodeConfigurationProperties {

    private final Environment environment;

    public String getWorkspaceDirectory() {
        return environment.getProperty("agent.workspace.dir", "C:\\tmp\\workspace");
    }

    public String getServerUrl() {
        return "http://%s:%s".formatted(
                environment.getProperty("server.rest.ip", "127.0.0.1"),
                environment.getProperty("server.rest.port", "8080")
        );

    }

    public String getRegisterUrl() {
        return getServerUrl() + environment.getProperty("server.rest.register", "/register");
    }

    public String getTaskLogUrl(String taskId) {
        return getServerUrl() + environment.getProperty("server.rest.tasklog", "/tasklog/{taskid}").replace("\\{taskid\\}", taskId);
    }

    public String getTaskStatusUrl(String taskId) {
        return getServerUrl() + environment.getProperty("server.rest.taskstatus", "/taskstatus/{taskid}").replace("\\{taskid\\}", taskId);
    }

    public String getConfigurationPersistenceFileName() {
        return getWorkspaceDirectory() + File.separatorChar + environment.getProperty("agent.configuration.persistence.file", "configuration.properties");
    }

    public String getConfigurationAgentIdKey() {
        return environment.getProperty("agent.configuration.id.key", "agent.id");
    }

}
