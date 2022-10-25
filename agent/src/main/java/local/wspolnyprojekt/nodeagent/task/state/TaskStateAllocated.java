package local.wspolnyprojekt.nodeagent.task.state;

import local.wspolnyprojekt.nodeagentlib.dto.TaskStatus;

public class TaskStateAllocated implements TaskState {
    @Override
    public TaskStatus getDtoTaskStatus() {
        return TaskStatus.TASK_STATUS_ALLOCATED;
    }

    @Override
    public TaskState downloaded(boolean success) {
        if (success) {
            return new TaskStateReady();
        }
        return new TaskStateFail();
    }
}
