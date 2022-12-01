package local.wspolnyprojekt.nodeagent.task.state;

import local.wspolnyprojekt.nodeagent.task.state.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static local.wspolnyprojekt.nodeagentlib.dto.TaskStatus.TASK_STATUS_FAIL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskStateFailTest {
    private TaskState taskState;

    @BeforeEach
    void init() {
        taskState = new TaskStateFail();
    }

    @Test
    void shouldGiveFailDtoStatus() {
        assertThat(taskState.getDtoTaskStatus()).isEqualTo(TASK_STATUS_FAIL);
    }

    @Test
    void shouldStartGiveRunningOrFailTaskState() {
        assertThat(taskState.start(true)).isInstanceOf(TaskStateRunning.class);
        assertThat(taskState.start(false)).isInstanceOf(TaskStateFail.class);
    }

    @Test
    void shouldGitPullGiveReadyOrFailTaskState() {
        assertThat(taskState.gitPull(true)).isInstanceOf(TaskStateReady.class);
        assertThat(taskState.gitPull(false)).isInstanceOf(TaskStateFail.class);
    }

    @Test
    void shouldFtpGiveReadyOrFailTaskState() {
        assertThat(taskState.ftp(true)).isInstanceOf(TaskStateReady.class);
        assertThat(taskState.ftp(false)).isInstanceOf(TaskStateFail.class);
    }

    @Test
    void shouldDeleteGiveDeletedOrFailTaskState() {
        assertThat(taskState.delete(true)).isInstanceOf(TaskStateDeleted.class);
        assertThat(taskState.delete(false)).isInstanceOf(TaskStateFail.class);
    }

    @Test
    void shouldGitCloneThrowIllegalStateException() {
        assertThatThrownBy(() -> taskState.gitClone(true)).isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(() -> taskState.gitClone(false)).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldDownloadedThrowIllegalStateException() {
        assertThatThrownBy(() -> taskState.downloaded(true)).isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(() -> taskState.downloaded(false)).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldStopThrowIllegalStateException() {
        assertThatThrownBy(() -> taskState.stop(true)).isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(() -> taskState.stop(false)).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldDoneThrowIllegalStateException() {
        assertThatThrownBy(() -> taskState.done(true)).isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(() -> taskState.done(false)).isInstanceOf(IllegalStateException.class);
    }
}
