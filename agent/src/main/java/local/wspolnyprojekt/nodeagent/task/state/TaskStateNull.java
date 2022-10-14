package local.wspolnyprojekt.nodeagent.task.state;

import local.wspolnyprojekt.nodeagentlib.dto.TaskStatus;

public class TaskStateNull implements TaskState {
    @Override
    public TaskStatus getDtoTaskStatus() {
        return TaskStatus.TASK_STATUS_NULL;
    }

    @Override
    public TaskState ftp(boolean success) {
        if (success) {
            return new TaskStateReady();
        }
        return this;
    }

    @Override
    public TaskState gitClone(boolean success) {
        if (success) {
            return new TaskStateAllocated();
        }
        return this;
    }
}
