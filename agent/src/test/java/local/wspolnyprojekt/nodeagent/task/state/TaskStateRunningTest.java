package local.wspolnyprojekt.nodeagent.task.state;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static local.wspolnyprojekt.nodeagentlib.dto.TaskStatus.TASK_STATUS_RUNNING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskStateRunningTest {
    private TaskState taskState;

    @BeforeEach
    void init() {
        taskState = new TaskStateRunning();
    }

    @Test
    void shouldGiveRunningDtoStatus() {
        assertThat(taskState.getDtoTaskStatus()).isEqualTo(TASK_STATUS_RUNNING);
    }

    @Test
    void shouldStopGiveStoppedOrFailTaskState() {
        assertThat(taskState.stop(true)).isInstanceOf(TaskStateStopped.class);
        assertThat(taskState.stop(false)).isInstanceOf(TaskStateFail.class);
    }

    @Test
    void shouldDoneGiveDoneOrFailTaskState() {
        assertThat(taskState.done(true)).isInstanceOf(TaskStateDone.class);
        assertThat(taskState.done(false)).isInstanceOf(TaskStateFail.class);
    }

    @Test
    void shouldGitCloneThrowIllegalStateException() {
        assertThatThrownBy(() -> taskState.gitClone(true)).isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(() -> taskState.gitClone(false)).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldFtpThrowIllegalStateException() {
        assertThatThrownBy(() -> taskState.ftp(true)).isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(() -> taskState.ftp(false)).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldGitResqurcesThrowIllegalStateException() {
        assertThatThrownBy(() -> taskState.downloaded(true)).isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(() -> taskState.downloaded(false)).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldGitPullThrowIllegalStateException() {
        assertThatThrownBy(() -> taskState.gitPull(true)).isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(() -> taskState.gitPull(false)).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldStartThrowIllegalStateException() {
        assertThatThrownBy(() -> taskState.start(true)).isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(() -> taskState.start(false)).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldDeleteThrowIllegalStateException() {
        assertThatThrownBy(() -> taskState.delete(true)).isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(() -> taskState.delete(false)).isInstanceOf(IllegalStateException.class);
    }
}
