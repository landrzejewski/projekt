package local.wspolnyprojekt.nodeagent.server;

import local.wspolnyprojekt.nodeagentlib.dto.TaskLogMessage;
import local.wspolnyprojekt.nodeagentlib.dto.TaskStatusMessage;

public interface ServerCommunicationService {
    void sendTaskLog(TaskLogMessage taskLogMessage);

    void sendTaskStatus(TaskStatusMessage taskStatusMessage);

    void registerAgent();

    boolean isRegistered();
}
