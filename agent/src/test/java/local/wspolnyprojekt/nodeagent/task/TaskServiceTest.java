package local.wspolnyprojekt.nodeagent.task;

import local.wspolnyprojekt.nodeagent.docker.DockerService;
import local.wspolnyprojekt.nodeagent.git.GitClient;
import local.wspolnyprojekt.nodeagent.statusbroadcast.StatusBroadcaster;
import local.wspolnyprojekt.nodeagent.task.state.TaskStateDeleted;
import local.wspolnyprojekt.nodeagent.workspaceutils.WorkspaceUtils;
import local.wspolnyprojekt.nodeagentlib.dto.TaskCommand;
import local.wspolnyprojekt.nodeagentlib.dto.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.FileNotFoundException;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class TaskServiceTest {
    private final String taskId = UUID.randomUUID().toString();
    private DockerService dockerService;
    private GitClient gitClient;
    private StatusBroadcaster statusBroadcaster;
    private WorkspaceUtils workspaceUtils;
    private TasksService tasksService;
    private Task task;

    @BeforeEach
    void init() {
        dockerService = Mockito.mock(DockerService.class);
        gitClient = Mockito.mock(GitClient.class);
        statusBroadcaster = Mockito.mock(StatusBroadcaster.class);
        workspaceUtils = Mockito.mock(WorkspaceUtils.class);
        task = new Task(statusBroadcaster, workspaceUtils, taskId);
        tasksService = new TasksService(dockerService, gitClient, statusBroadcaster, workspaceUtils);
        tasksService.addTask(task);
    }

    @Test
    void shouldExecuteStopCommand() {
        try {
            tasksService.executeCommand(task, TaskCommand.TASK_COMMAND_STOP);
            verify(dockerService, times(1)).down(task);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void shouldExecuteStartCommand() {
        try {
            tasksService.executeCommand(task, TaskCommand.TASK_COMMAND_START);
            verify(dockerService, times(1)).buildAndRun(task);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void shouldExecuteCleanupCommand() {
        try {
            tasksService.executeCommand(task, TaskCommand.TASK_COMMAND_CLEANUP);
            verify(dockerService, times(1)).cleanup(task);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void shouldExecuteDeleteCommand() {
        try {
            tasksService.executeCommand(task, TaskCommand.TASK_COMMAND_DELETE);
            verify(dockerService, times(1)).delete(task);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void shouldDeleteTaskFromTasksCollection() {
        try {
            task.setStatus(new TaskStateDeleted());
            tasksService.executeCommand(task, TaskCommand.TASK_COMMAND_DELETE);
            assertThat(tasksService.hasTask(task.getTaskId())).isFalse();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void shouldReturnCurrentTaskStatus() {
        task.setStatus(new TaskStateDeleted());
        assertThat(tasksService.getStatus(task.getTaskId())).isEqualTo(TaskStatus.TASK_STATUS_DELETED);

    }

    @Test
    void shouldInvokeGitPull() {
        tasksService.pull(task);
        verify(gitClient, times(1)).pull(task);
    }

    @Test
    void shouldInvokeGitClone() {
        tasksService.clone(null, task.getTaskId());
        verify(gitClient, times(1)).clone(null, task);
    }

    @Test
    void shouldInvokeGetLogOnDockerService() {
        try {
            tasksService.getLog(task);
            verify(dockerService,times(1)).getLog(task);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
