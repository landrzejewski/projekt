package local.wspolnyprojekt.nodeagent.docker;

import local.wspolnyprojekt.nodeagent.shellcommand.CommandExecutorService;
import local.wspolnyprojekt.nodeagent.task.Task;
import local.wspolnyprojekt.nodeagent.task.state.TaskStateDone;
import local.wspolnyprojekt.nodeagent.task.state.TaskStateStopped;
import local.wspolnyprojekt.nodeagent.workspaceutils.WorkspaceUtils;
import local.wspolnyprojekt.nodeagent.shellcommand.ShellCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.FileNotFoundException;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DockerServiceTest {
    @Mock
    private CommandExecutorService commandExecutorService;
    @Mock
    private WorkspaceUtils workspaceUtils;
    private DockerService dockerService;
    private final String taskId = UUID.randomUUID().toString();
    private Task task;

    @BeforeEach
    void init() {
        dockerService = new DockerService(commandExecutorService, workspaceUtils);
        task = new Task(null, workspaceUtils, taskId);
    }

    @Test
    void shouldExecuteDockerComposeAfterBuildAndRun() {
        dockerService.buildAndRun(task);
        ShellCommand shellCommand = new ShellCommand();
        shellCommand.setCommand("docker-compose");
        shellCommand.setArgs(new String[]{"up", "--build"});
        shellCommand.setTimeoutInSeconds(-1);
        verify(commandExecutorService, times(1))
                .executeCommand(shellCommand, taskId);
    }

    @Test
    void shouldChangeTaskStatusAfterBuildAndRun() {
        dockerService.buildAndRun(task);
        assertThat(task.getStatus()).isInstanceOf(TaskStateDone.class);
    }

    @Test
    void shouldExecuteDockerComposeAfterDown() {
        dockerService.down(task);
        ShellCommand shellCommand = new ShellCommand();
        shellCommand.setCommand("docker-compose");
        shellCommand.setArgs(new String[]{"down"});
        shellCommand.setTimeoutInSeconds(-1);
        verify(commandExecutorService, times(1))
                .executeCommand(shellCommand, taskId);
    }

    @Test
    void shouldChangeTaskStatusAfterDown() {
        dockerService.down(task);
        assertThat(task.getStatus()).isInstanceOf(TaskStateStopped.class);
    }

    @Test
    void shouldCallWorkspaceUtilsAfterGetLog() {
        try {
            dockerService.getLog(task);
            verify(workspaceUtils, times(1))
                    .getFileAsInputStreamResource(taskId, "output.log");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void shouldExecuteDockerCommandsAfterCleanup() {
        dockerService.cleanup(task);

        ShellCommand shellCommandVolumePrune = new ShellCommand();
        shellCommandVolumePrune.setCommand("docker");
        shellCommandVolumePrune.setArgs(new String[]{"volume", "prune", "-f"});
        shellCommandVolumePrune.setTimeoutInSeconds(-1);

        ShellCommand shellCommandBuilderPrune = new ShellCommand();
        shellCommandBuilderPrune.setCommand("docker");
        shellCommandBuilderPrune.setArgs(new String[]{"builder", "prune", "-f"});
        shellCommandBuilderPrune.setTimeoutInSeconds(-1);

        ShellCommand shellCommandImagePrune = new ShellCommand();
        shellCommandImagePrune.setCommand("docker");
        shellCommandImagePrune.setArgs(new String[]{"image", "prune", "--all", "-f"});
        shellCommandImagePrune.setTimeoutInSeconds(-1);

        verify(commandExecutorService, times(1))
                .executeCommand(shellCommandVolumePrune, taskId);

        verify(commandExecutorService, times(1))
                .executeCommand(shellCommandBuilderPrune, taskId);

        verify(commandExecutorService, times(1))
                .executeCommand(shellCommandImagePrune, taskId);
    }

    @Test
    void shouldCallWorkspaceUtilsAfterDelete() {
        dockerService.delete(task);
        verify(workspaceUtils, times(1)).deleteWorkspace(taskId);
    }
}
