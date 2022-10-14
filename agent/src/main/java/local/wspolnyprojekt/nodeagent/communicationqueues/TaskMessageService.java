package local.wspolnyprojekt.nodeagent.communicationqueues;

import local.wspolnyprojekt.nodeagentlib.dto.TaskStatusMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Slf4j
@Service
@ApplicationScope
public class TaskMessageService implements MessageService {
    private static final Queue<LogEntity> logs = new ConcurrentLinkedQueue<>();
    private static final Queue<TaskStatusMessage> taskStatus = new ConcurrentLinkedQueue<>();

    @Override
    public void save(LogEntity logEntry) {
        logs.add(logEntry);
    }

    @Override
    public void save(TaskStatusMessage taskStatusMessage) {
        log.info("#{}",taskStatusMessage);
        taskStatus.add(taskStatusMessage);
    }

    @Override
    public boolean isLogEmpty() {
        return logs.isEmpty();
    }

    @Override
    public boolean isStatusEmpty() {
        return taskStatus.isEmpty();
    }

    @Override
    public Optional<LogEntity> getLog() {
        return Optional.ofNullable(logs.poll());
    }

    @Override
    public Optional<TaskStatusMessage> getStatusMessage() {
        return Optional.ofNullable(taskStatus.poll());
    }
}
