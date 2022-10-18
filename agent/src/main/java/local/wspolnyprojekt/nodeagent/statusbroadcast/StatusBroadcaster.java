package local.wspolnyprojekt.nodeagent.statusbroadcast;

import local.wspolnyprojekt.nodeagent.task.Task;

public interface StatusBroadcaster {
    void broadcastStatusChange(Task task, String description);
}
