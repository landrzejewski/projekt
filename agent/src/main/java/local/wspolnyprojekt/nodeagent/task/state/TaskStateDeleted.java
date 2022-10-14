package local.wspolnyprojekt.nodeagent.task.state;

import local.wspolnyprojekt.nodeagentlib.dto.TaskStatus;

public class TaskStateDeleted implements TaskState {
    @Override
    public TaskStatus getDtoTaskStatus() {
        return TaskStatus.TASK_STATUS_DELETED;
    }

    @Override
    public TaskState delete(boolean success) {
        if (success) {
            return new TaskStateNull();
        }
        return this;
    }
}
