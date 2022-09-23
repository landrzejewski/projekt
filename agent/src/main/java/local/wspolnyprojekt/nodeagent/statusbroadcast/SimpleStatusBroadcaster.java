package local.wspolnyprojekt.nodeagent.statusbroadcast;

import local.wspolnyprojekt.nodeagent.task.TaskStatus;
import org.springframework.stereotype.Component;

import java.util.List;

// TODO Na razie testowa implementacja naiwna, docelowo kolejkowanie i asynchroniczna wysyłka aby wywołanie metody 'broadcast*' nie było blokujące
@Component
public class SimpleStatusBroadcaster implements StatusBroadcaster {
    List<SoutStatusListener> statusListeners;

    /**
     * Na potrzeby testowania dodany listener wypisujący na standardowe wyjście
     */
    private SimpleStatusBroadcaster() {
        statusListeners = List.of(new SoutStatusListener());
    }

    @Override
    public void broadcastStatusChange(String taskId, TaskStatus taskStatus) {
        statusListeners.forEach(listener -> listener.receiveStatus(taskId,taskStatus));
    }
}
