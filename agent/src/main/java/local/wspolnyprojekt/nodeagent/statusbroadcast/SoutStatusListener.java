package local.wspolnyprojekt.nodeagent.statusbroadcast;

import local.wspolnyprojekt.nodeagent.task.Task;

public class SoutStatusListener implements StatusListener {
    @Override
    public void receiveStatus(Task task, String description) {
        System.out.println(task.getTaskId() + " -> " + task.getStatus().getDtoTaskStatus().toString() + ": " + description);
    }
}
