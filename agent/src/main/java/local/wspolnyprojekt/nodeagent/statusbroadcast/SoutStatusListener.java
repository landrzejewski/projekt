package local.wspolnyprojekt.nodeagent.statusbroadcast;

import local.wspolnyprojekt.nodeagent.task.TaskStatus;

/**
 * Na razie na standardowe wyjście, konkretna implementacja będzie jak dostanę endpoint serwerowy
 */
public class SoutStatusListener implements StatusListener {
    @Override
    public void receiveStatus(String taskId, TaskStatus status) {
        System.out.println(taskId + " -> " + status);
    }
}
