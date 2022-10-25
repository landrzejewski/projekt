package local.wspolnyprojekt.nodeagent.task.state;

import local.wspolnyprojekt.nodeagentlib.dto.TaskStatus;

public interface TaskState {
    TaskStatus getDtoTaskStatus();

    default TaskState ftp(boolean success) {
        return gitPull(success);
    }

    default TaskState downloaded(boolean success) {
        throw new IllegalStateException();
    }

    default TaskState gitClone(boolean success) {
        throw new IllegalStateException();
    }

    default TaskState gitPull(boolean success) {
        throw new IllegalStateException();
    }

    default TaskState start(boolean success) {
        throw new IllegalStateException();
    }

    default TaskState stop(boolean success) {
        throw new IllegalStateException();
    }

    default TaskState delete(boolean success) {
        throw new IllegalStateException();
    }

    default TaskState done(boolean success) {
        throw new IllegalStateException();
    }
}
