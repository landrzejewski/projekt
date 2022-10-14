package local.wspolnyproject.nodeagent.task.state;

import local.wspolnyprojekt.nodeagent.task.state.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static local.wspolnyprojekt.nodeagentlib.dto.TaskStatus.TASK_STATUS_DONE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskStateDoneTest {
    private TaskState taskState;

    @BeforeEach
    void init() {
        taskState = new TaskStateDone();
    }

    @Test
    void shouldGiveDonedDtoStatus() {
        assertThat(taskState.getDtoTaskStatus()).isEqualTo(TASK_STATUS_DONE);
    }

    @Test
    void shouldFtpGiveReadyOrFailTaskState() {
        assertThat(taskState.ftp(true)).isInstanceOf(TaskStateReady.class);
        assertThat(taskState.ftp(false)).isInstanceOf(TaskStateFail.class);
    }

    @Test
    void shouldGitPullGiveReadyOrFailTaskState() {
        assertThat(taskState.gitPull(true)).isInstanceOf(TaskStateReady.class);
        assertThat(taskState.gitPull(false)).isInstanceOf(TaskStateFail.class);
    }

    @Test
    void shouldStartGiveRunningOrFailTaskState() {
        assertThat(taskState.start(true)).isInstanceOf(TaskStateRunning.class);
        assertThat(taskState.start(false)).isInstanceOf(TaskStateFail.class);
    }

    @Test
    void shouldGitResqurcesThrowIllegalStateException() {
        assertThatThrownBy(() -> taskState.downloaded(true)).isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(() -> taskState.downloaded(false)).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldGitCloneThrowIllegalStateException() {
        assertThatThrownBy(() -> taskState.gitClone(true)).isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(() -> taskState.gitClone(false)).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldStopThrowIllegalStateException() {
        assertThatThrownBy(() -> taskState.stop(true)).isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(() -> taskState.stop(false)).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldDeleteThrowIllegalStateException() {
        assertThatThrownBy(() -> taskState.delete(true)).isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(() -> taskState.delete(false)).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldDoneThrowIllegalStateException() {
        assertThatThrownBy(() -> taskState.done(true)).isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(() -> taskState.done(false)).isInstanceOf(IllegalStateException.class);
    }
}
