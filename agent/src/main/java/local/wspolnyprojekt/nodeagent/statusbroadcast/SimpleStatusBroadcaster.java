package local.wspolnyprojekt.nodeagent.statusbroadcast;

import local.wspolnyprojekt.nodeagent.communicationqueues.TaskMessageService;
import local.wspolnyprojekt.nodeagent.task.Task;
import local.wspolnyprojekt.nodeagent.task.state.TaskState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
@Service
@ApplicationScope
@RequiredArgsConstructor
public class SimpleStatusBroadcaster implements StatusBroadcaster {
    List<StatusListener> statusListeners;
    private final TaskMessageService taskMessageService;

    /**
     * Na potrzeby testowania dodany listener wypisujący na standardowe wyjście
     */
    @PostConstruct
    void init() {
        log.info("PostConstruct");
        statusListeners = List.of(new SoutStatusListener(), new ServerStatusListener(taskMessageService));
    }

    @Override
    public void broadcastStatusChange(Task task, TaskState taskState) {
        log.info("{} {}",task,taskState);
        statusListeners.forEach(listener -> listener.receiveStatus(task, taskState));
    }
}
