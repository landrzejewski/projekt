package local.wspolnyprojekt.nodeagent.server;

import local.wspolnyprojekt.nodeagent.communicationqueues.LogEntity;
import local.wspolnyprojekt.nodeagentlib.dto.TaskStatus;
import local.wspolnyprojekt.nodeagentlib.dto.TaskStatusMessage;

public interface ServerCommunicationService {
    void sendTaskLog(LogEntity logEntity);
    void sendTaskStatus(TaskStatusMessage taskStatusMessage);
    void registerAgent();
    boolean isRegistered();
}
