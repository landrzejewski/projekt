package local.wspolnyproject.nodeagent.task.state;

import local.wspolnyprojekt.nodeagent.task.state.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static local.wspolnyprojekt.nodeagentlib.dto.TaskStatus.TASK_STATUS_NULL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskStateNullTest {
    private TaskState taskState;

    @BeforeEach
    void init() {
        taskState = new TaskStateNull();
    }

    @Test
    void shouldGiveNullDtoStatus() {
        assertThat(taskState.getDtoTaskStatus()).isEqualTo(TASK_STATUS_NULL);
    }

    @Test
    void shouldFtpGiveReadyOrNullTaskState() {
        assertThat(taskState.ftp(true)).isInstanceOf(TaskStateReady.class);
        assertThat(taskState.ftp(false)).isInstanceOf(TaskStateNull.class);
    }

    @Test
    void shouldGitCloneGiveAllocatedOrNullTaskState() {
        assertThat(taskState.gitClone(true)).isInstanceOf(TaskStateAllocated.class);
        assertThat(taskState.gitClone(false)).isInstanceOf(TaskStateNull.class);
    }

    @Test
    void shouldDownloadedThrowIllegalStateException() {
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
