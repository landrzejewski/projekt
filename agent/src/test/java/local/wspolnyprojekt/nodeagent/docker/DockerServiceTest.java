package local.wspolnyprojekt.nodeagent.docker;

import local.wspolnyprojekt.nodeagent.shellcommand.CommandExecutorService;
import local.wspolnyprojekt.nodeagent.task.Task;
import local.wspolnyprojekt.nodeagent.workspaceutils.WorkspaceUtils;
import local.wspolnyprojekt.nodeagentlib.dto.ShellCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.UUID;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class DockerServiceTest {
    private CommandExecutorService commandExecutorService;
    private WorkspaceUtils workspaceUtils;
    private DockerService dockerService;
    private final String taskId = UUID.randomUUID().toString();
    private Task task;

    @BeforeEach
    void init() {
        commandExecutorService = Mockito.mock(CommandExecutorService.class);
        workspaceUtils = Mockito.mock(WorkspaceUtils.class);
        dockerService = new DockerService(commandExecutorService, workspaceUtils);
        task = new Task(null,workspaceUtils,taskId);
    }

    @Test
    void shouldExecuteDockerComposeAfterBuildAndRunCommand() {
        dockerService.buildAndRun(task);
        ShellCommand shellCommand = new ShellCommand();
        shellCommand.setCommand("docker-compose");
        shellCommand.setArgs(new String[]{"up", "--build"});
        shellCommand.setTimeoutInSeconds(-1);
        verify(commandExecutorService,times(1))
                .executeCommand(shellCommand,taskId);
    }

}
