package local.wspolnyprojekt.nodeagent.communicationqueues;

import local.wspolnyprojekt.nodeagentlib.dto.TaskStatusMessage;

import java.util.Optional;

public interface MessageService {
    void save(LogEntity logEntry);
    void save(TaskStatusMessage taskStatusMessage);
    boolean isLogEmpty();
    boolean isStatusEmpty();
    Optional<LogEntity> getLog();
    Optional<TaskStatusMessage> getStatusMessage();
}
