package local.wspolnyprojekt.nodeagent.statusbroadcast;

import local.wspolnyprojekt.nodeagent.task.TaskStatus;

public interface StatusBroadcaster {
    void broadcastStatusChange(String taskId, TaskStatus taskStatus);
}
