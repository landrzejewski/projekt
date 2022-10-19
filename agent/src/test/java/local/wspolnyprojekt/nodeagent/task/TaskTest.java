package local.wspolnyprojekt.nodeagent.task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TaskTest {

    @BeforeEach
    void init() {
    }

    @Test
    void shouldGiveTrueWhenSendingNextStatusIsDisabled() {
        Task task;
        task = new Task(null,null,null);
        task.disableSendingNextStatus();
        assertThat(task.getAndResetSendingNextStatus()).isTrue();
    }

    @Test
    void shouldGiveFalseWhenSendingNextStatusIsDefault() {
        Task task;
        task = new Task(null,null,null);
        assertThat(task.getAndResetSendingNextStatus()).isFalse();
    }

    @Test
    void shouldResetToDefaultAfterCallingGetAndResetSendingNextStatusMethod() {
        Task task;
        task = new Task(null,null,null);
        task.disableSendingNextStatus();
        assertThat(task.getAndResetSendingNextStatus()).isTrue(); // Pierwsze wywołanie - enabled
        assertThat(task.getAndResetSendingNextStatus()).isFalse(); // Drugie wywołanie - disabled
    }
}
