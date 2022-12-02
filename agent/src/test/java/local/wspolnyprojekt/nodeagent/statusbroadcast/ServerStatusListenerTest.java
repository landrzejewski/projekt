package local.wspolnyprojekt.nodeagent.statusbroadcast;

import local.wspolnyprojekt.nodeagent.communicationqueues.TaskMessageService;
import local.wspolnyprojekt.nodeagent.task.Task;
import local.wspolnyprojekt.nodeagentlib.dto.TaskStatusMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

class ServerStatusListenerTest {

    private TaskMessageService taskMessageService;
    private ServerStatusListener serverStatusListener;
    @Captor
    ArgumentCaptor<TaskStatusMessage> taskStatusMessageCaptor;

    @BeforeEach
    void init() {
        taskMessageService = Mockito.mock(TaskMessageService.class);
        serverStatusListener = new ServerStatusListener(taskMessageService);
    }

    @Test
    void shouldInvokeTaskMessageServiceAfterReceivingTaskStatus() {
        Task task = new Task(null, null, null);
        serverStatusListener.receiveStatus(task, null);
        verify(taskMessageService, times(1)).save(any(TaskStatusMessage.class));
    }

    @Test
    void shouldInvokeTaskMessageServiceWithCorrectDescriptionAndTaskId() {
        // TODO Zbadać czemu bez ręcznego uruchomienia inicjalizacji mocków nie chce działać ArgumentCapture
        openMocks(this);

        String taskId = UUID.randomUUID().toString();
        String description = "example description";

        Task task = new Task(null, null, taskId);

        serverStatusListener.receiveStatus(task, description);
        verify(taskMessageService).save(taskStatusMessageCaptor.capture());

        assertThat(taskStatusMessageCaptor.getValue().getDescription()).isEqualTo(description);
        assertThat(taskStatusMessageCaptor.getValue().getTaskId()).isEqualTo(taskId);
    }
}
