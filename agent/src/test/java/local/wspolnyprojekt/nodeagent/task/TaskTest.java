package local.wspolnyprojekt.nodeagent.task;

import local.wspolnyprojekt.nodeagent.statusbroadcast.StatusBroadcaster;
import local.wspolnyprojekt.nodeagent.task.state.TaskState;
import local.wspolnyprojekt.nodeagent.task.state.TaskStateFail;
import local.wspolnyprojekt.nodeagent.task.state.TaskStateReady;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;

class TaskTest {

    @BeforeEach
    void init() {
    }

    @Test
    void shouldGiveTrueWhenSendingNextStatusIsDisabled() {
        Task task;
        task = new Task(null, null, null);
        task.disableSendingNextStatus();
        assertThat(task.getAndResetSendingNextStatus()).isFalse();
    }

    @Test
    void shouldGiveFalseWhenSendingNextStatusIsDefault() {
        Task task;
        task = new Task(null, null, null);
        assertThat(task.getAndResetSendingNextStatus()).isTrue();
    }

    @Test
    void shouldResetToDefaultAfterCallingGetAndResetSendingNextStatusMethod() {
        Task task;
        task = new Task(null, null, null);
        task.disableSendingNextStatus();
        assertThat(task.getAndResetSendingNextStatus()).isFalse(); // Pierwsze wywołanie - disabled
        assertThat(task.getAndResetSendingNextStatus()).isTrue(); // Drugie wywołanie - enabled
    }

    @Test
    void shouldSetState() {
        StatusBroadcaster statusBroadcaster = Mockito.mock(StatusBroadcaster.class);
        Task task = new Task(statusBroadcaster, null, null);
        TaskState stateReady = new TaskStateReady();
        task.setStatus(stateReady);
        assertThat(task.getStatus()).isEqualTo(stateReady);
    }

    @Test
    void shouldBroadcastState() {
        StatusBroadcaster statusBroadcaster = Mockito.mock(StatusBroadcaster.class);
        Task task = new Task(statusBroadcaster, null, null);
        task.setStatus(new TaskStateReady());
        task.setStatus(new TaskStateFail(),"FAIL");
        Mockito.verify(statusBroadcaster,times(1)).broadcastStatusChange(task,"");
        Mockito.verify(statusBroadcaster,times(1)).broadcastStatusChange(task,"FAIL");
    }
}
