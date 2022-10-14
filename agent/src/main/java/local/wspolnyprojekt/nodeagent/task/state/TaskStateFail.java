package local.wspolnyprojekt.nodeagent.task.state;

import local.wspolnyprojekt.nodeagentlib.dto.TaskStatus;

public class TaskStateFail implements TaskState {
    @Override
    public TaskState start(boolean success) {
        if (success) {
            return new TaskStateRunning();
        }
        return new TaskStateFail();
    }

    @Override
    public TaskStatus getDtoTaskStatus() {
        return TaskStatus.TASK_STATUS_FAIL;
    }

    @Override
    public TaskState gitPull(boolean success) {
        if (success) {
            return new TaskStateReady();
        }
        return new TaskStateFail();
    }

    @Override
    public TaskState delete(boolean success) {
        if (success) {
            return new TaskStateDeleted();
        }
        return new TaskStateFail();
    }
}
