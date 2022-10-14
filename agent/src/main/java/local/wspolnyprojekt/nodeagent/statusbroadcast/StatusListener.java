package local.wspolnyprojekt.nodeagent.statusbroadcast;

import local.wspolnyprojekt.nodeagent.task.Task;
import local.wspolnyprojekt.nodeagent.task.state.TaskState;
import local.wspolnyprojekt.nodeagentlib.dto.TaskStatus;

public interface StatusListener {
    void receiveStatus(Task task, TaskState state) ;
}
