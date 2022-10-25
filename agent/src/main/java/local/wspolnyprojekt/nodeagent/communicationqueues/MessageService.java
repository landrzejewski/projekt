package local.wspolnyprojekt.nodeagent.communicationqueues;

import local.wspolnyprojekt.nodeagentlib.dto.TaskLogMessage;
import local.wspolnyprojekt.nodeagentlib.dto.TaskStatusMessage;

import java.util.Optional;

public interface MessageService {
    void save(TaskLogMessage logEntry);
    void save(TaskStatusMessage taskStatusMessage);
    boolean isLogQueueEmpty();
    boolean isStatusQueueEmpty();
    Optional<TaskLogMessage> getLogMessage();
    Optional<TaskStatusMessage> getStatusMessage();
}
