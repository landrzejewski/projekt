package local.wspolnyprojekt.nodeagent.communicationqueues;

import local.wspolnyprojekt.nodeagent.server.ServerCommunicationService;
import local.wspolnyprojekt.nodeagent.task.TasksService;
import local.wspolnyprojekt.nodeagentlib.dto.TaskLogMessage;
import local.wspolnyprojekt.nodeagentlib.dto.TaskStatus;
import local.wspolnyprojekt.nodeagentlib.dto.TaskStatusMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class MessageExportDaemonTest {

    private TaskMessageService taskMessageService;
    private ServerCommunicationService serverCommunicationService;
    private TasksService tasksService;

    private MessageExportDaemon messageExportDaemon;
    private TaskStatusMessage taskStatusMessage;
    private TaskLogMessage taskLogMessage;

    @BeforeEach
    void init() {
        taskStatusMessage = new TaskStatusMessage("timestamp",
                "taskid",
                TaskStatus.TASK_STATUS_NULL,
                "description");

        taskLogMessage = new TaskLogMessage("timestamp", "sender", "payload");

        taskMessageService = Mockito.mock(TaskMessageService.class);
        serverCommunicationService = Mockito.mock(ServerCommunicationService.class);
        tasksService = Mockito.mock(TasksService.class);

        when(serverCommunicationService.isRegistered()).thenReturn(true);
        when(tasksService.hasTask(anyString())).thenReturn(true);
        when(taskMessageService.isLogQueueEmpty()).thenReturn(false, true);
        when(taskMessageService.isStatusQueueEmpty()).thenReturn(false, true);

        when(taskMessageService.getLogMessage())
                .thenReturn(Optional.ofNullable(taskLogMessage), Optional.empty());

        when(taskMessageService.getStatusMessage())
                .thenReturn(Optional.ofNullable(taskStatusMessage), Optional.empty());

        messageExportDaemon = new MessageExportDaemon(taskMessageService, serverCommunicationService, tasksService);

    }

    @Test
    void shouldExportTaskStatusCallServerCommunicationService() {
        messageExportDaemon.exportTaskStatuses();
        verify(serverCommunicationService, times(1)).sendTaskStatus(taskStatusMessage);
    }

    @Test
    void shouldExportTaskLogsCallServerCommunicationService() {
        messageExportDaemon.exportTaskLogs();
        verify(serverCommunicationService, times(1)).sendTaskLog(taskLogMessage);
    }
}
