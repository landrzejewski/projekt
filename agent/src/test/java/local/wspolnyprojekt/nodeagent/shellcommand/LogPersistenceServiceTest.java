package local.wspolnyprojekt.nodeagent.shellcommand;

import local.wspolnyprojekt.nodeagent.communicationqueues.TaskMessageService;
import local.wspolnyprojekt.nodeagentlib.dto.TaskLogMessage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LogPersistenceServiceTest {

    private FileOutputPersistence fileOutputPersistence;
    private TaskMessageService taskMessageService;

    private LogPersistenceService logPersistenceService;
    private String taskId;
    private final String text = "Test output";

    @BeforeEach
    void init() {
        fileOutputPersistence = Mockito.mock(FileOutputPersistence.class);
        taskMessageService = Mockito.mock(TaskMessageService.class);
        logPersistenceService = new LogPersistenceService(fileOutputPersistence, taskMessageService);
        taskId = UUID.randomUUID().toString();
    }

    @Test
    void shouldSaveInvokeFileOptputPersistenceService() {
        logPersistenceService.save(taskId, text);
        verify(fileOutputPersistence, times(1)).save(taskId, text);
    }
    @Test
    void shouldSaveInvokeTaskMessageService() {
        String localtime = LocalDateTime.now().toString();
        logPersistenceService.save(taskId, text);
        verify(taskMessageService, times(1)).save(any(TaskLogMessage.class));
    }
}
