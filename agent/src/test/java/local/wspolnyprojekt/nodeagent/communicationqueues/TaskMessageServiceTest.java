package local.wspolnyprojekt.nodeagent.communicationqueues;

import local.wspolnyprojekt.nodeagentlib.dto.TaskLogMessage;
import local.wspolnyprojekt.nodeagentlib.dto.TaskStatusMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class TaskMessageServiceTest {

    private TaskMessageService taskMessageService;

    @BeforeEach
    void createTaskMessageService() {
        taskMessageService = new TaskMessageService();
    }

    @Test
    void shouldLogQueueBeEmpty() {
        assertThat(taskMessageService.isLogQueueEmpty()).isTrue();
    }

    @Test
    void shouldStatusQueueBeEmpty() {
        assertThat(taskMessageService.isStatusQueueEmpty()).isTrue();
    }

    @Test
    void shouldStatusQueueHasTwoMessages() {
        taskMessageService.save(new TaskStatusMessage(null, null, null, null));
        taskMessageService.save(new TaskStatusMessage(null, null, null, null));
        assertThat(taskMessageService.getStatusMessage()).isPresent();
        assertThat(taskMessageService.getStatusMessage()).isPresent();
        assertThat(taskMessageService.isStatusQueueEmpty()).isTrue();
    }

    @Test
    void shouldLogQueueHasTwoMessages() {
        taskMessageService.save(new TaskLogMessage(null, null, null));
        taskMessageService.save(new TaskLogMessage(null, null, null));
        assertThat(taskMessageService.getLogMessage()).isPresent();
        assertThat(taskMessageService.getLogMessage()).isPresent();
        assertThat(taskMessageService.isLogQueueEmpty()).isTrue();
    }

}
