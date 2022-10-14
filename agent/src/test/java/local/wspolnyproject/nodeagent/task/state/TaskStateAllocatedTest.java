package local.wspolnyproject.nodeagent.task.state;

import local.wspolnyprojekt.nodeagent.task.state.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static local.wspolnyprojekt.nodeagentlib.dto.TaskStatus.TASK_STATUS_ALLOCATED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskStateAllocatedTest {
    private TaskState taskState;

    @BeforeEach
    void init() {
        taskState = new TaskStateAllocated();
    }

    @Test
    void shouldGiveAllocatedDtoStatus() {
        assertThat(taskState.getDtoTaskStatus()).isEqualTo(TASK_STATUS_ALLOCATED);
    }

    @Test
    void shouldDownloadedGiveReadyOrFailTaskState() {
        assertThat(taskState.downloaded(true)).isInstanceOf(TaskStateReady.class);
        assertThat(taskState.downloaded(false)).isInstanceOf(TaskStateFail.class);
    }

    @Test
    void shouldFtpThrowIllegalStateException() {
        assertThatThrownBy(() -> taskState.ftp(true)).isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(() -> taskState.ftp(false)).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldGitCloneThrowIllegalStateException() {
        assertThatThrownBy(() -> taskState.gitClone(true)).isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(() -> taskState.gitClone(false)).isInstanceOf(IllegalStateException.class);
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
