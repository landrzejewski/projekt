package local.wspolnyprojekt.nodeagent.task;

import local.wspolnyprojekt.nodeagentlib.dto.TaskCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.FileNotFoundException;
import java.util.UUID;

import static org.mockito.Mockito.*;

class TaskAutorunServiceTest {
    private TaskAutorunService taskAutorunService;
    private TasksService tasksService;
    private final String taskId = UUID.randomUUID().toString();

    @BeforeEach
    void init() {
        tasksService = Mockito.mock(TasksService.class);
        taskAutorunService = new TaskAutorunService(tasksService);
    }

    @Test
    void shouldAutostartAddedTask() {
        Task task = new Task(null, null, taskId);
        taskAutorunService.addToAutorun(task);
        taskAutorunService.autorun();
        try {
            verify(tasksService, times(1)).executeCommand(task, TaskCommand.TASK_COMMAND_START);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
