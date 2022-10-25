package local.wspolnyprojekt.nodeagent.statusbroadcast;

import local.wspolnyprojekt.nodeagent.task.Task;

public interface StatusListener {
    void receiveStatus(Task task, String description) ;
}
