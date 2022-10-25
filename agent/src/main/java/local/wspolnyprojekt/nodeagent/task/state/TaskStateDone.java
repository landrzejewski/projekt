package local.wspolnyprojekt.nodeagent.task.state;

import local.wspolnyprojekt.nodeagentlib.dto.TaskStatus;

public class TaskStateDone implements TaskState {
    @Override
    public TaskStatus getDtoTaskStatus() {
        return TaskStatus.TASK_STATUS_DONE;
    }

    @Override
    public TaskState gitPull(boolean success) {
        if (success) {
            return new TaskStateReady();
        }
        return new TaskStateFail();
    }

    @Override
    public TaskState start(boolean success) {
        if (success) {
            return new TaskStateRunning();
        }
        return new TaskStateFail();
    }

}
