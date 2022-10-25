package local.wspolnyprojekt.nodeagent.task.state;

import local.wspolnyprojekt.nodeagentlib.dto.TaskStatus;

public class TaskStateRunning implements TaskState {
    @Override
    public TaskStatus getDtoTaskStatus() {
        return TaskStatus.TASK_STATUS_RUNNING;
    }

    @Override
    public TaskState stop(boolean success) {
        if (success) {
            return new TaskStateStopped();
        }
        return new TaskStateFail();
    }

    @Override
    public TaskState done(boolean success) {
        if (success) {
            return new TaskStateDone();
        }
        return new TaskStateFail();
    }
}
