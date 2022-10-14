package local.wspolnyprojekt.nodeagent.statusbroadcast;

import local.wspolnyprojekt.nodeagent.task.Task;
import local.wspolnyprojekt.nodeagent.task.state.TaskState;
import local.wspolnyprojekt.nodeagentlib.dto.TaskStatus;

/**
 * Na razie na standardowe wyjście, konkretna implementacja będzie jak dostanę endpoint serwerowy
 */
public class SoutStatusListener implements StatusListener {
    @Override
    public void receiveStatus(Task task, TaskState state) {
        System.out.println(task.getTaskId() + " -> " + state.getDtoTaskStatus().toString());
    }
}
