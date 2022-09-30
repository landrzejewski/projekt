package local.wspolnyprojekt.nodeagent.statusbroadcast;

import local.wspolnyprojekt.nodeagent.task.TaskStatus;

public interface StatusListener {
    void receiveStatus(String taskId, TaskStatus status) ;

}
