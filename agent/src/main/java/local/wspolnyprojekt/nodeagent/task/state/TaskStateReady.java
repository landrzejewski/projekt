package local.wspolnyprojekt.nodeagent.task.state;

import local.wspolnyprojekt.nodeagentlib.dto.TaskStatus;

public class TaskStateReady implements TaskState {

    @Override
    public TaskStatus getDtoTaskStatus() {
        return TaskStatus.TASK_STATUS_READY;
    }

    @Override
    public TaskState start(boolean success) {
        if (success) {
            return new TaskStateRunning();
        }
        return new TaskStateFail();
    }

    @Override
    public TaskState gitPull(boolean success) {
        if (success) {
            return this;
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
