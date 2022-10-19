package local.wspolnyprojekt.nodeagent.task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TaskTest {

    private Task task;

    @BeforeEach
    void init() {
        task = new Task(null,null,null);
    }

    @Test
    void shouldGiveTrueWhenSendingNextStatusIsDisabled() {
        task.disableSendingNextStatus();
        assertThat(task.getAndResetSendingNextStatus()).isTrue();
    }

    @Test
    void shouldGiveFalseWhenSendingNextStatusIsDefault() {
        assertThat(task.getAndResetSendingNextStatus()).isFalse();
    }

    @Test
    void shouldResetToDefaultAfterCallingGetAndResetSendingNextStatusMethod() {
        task.disableSendingNextStatus();
        assertThat(task.getAndResetSendingNextStatus()).isTrue(); // Pierwsze wywołanie - enabled
        assertThat(task.getAndResetSendingNextStatus()).isFalse(); // Drugie wywołanie - disabled
    }
}
