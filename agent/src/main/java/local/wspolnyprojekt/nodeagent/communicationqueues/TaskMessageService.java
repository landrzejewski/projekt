package local.wspolnyprojekt.nodeagent.communicationqueues;

import local.wspolnyprojekt.nodeagent.task.TasksService;
import local.wspolnyprojekt.nodeagentlib.dto.TaskLogMessage;
import local.wspolnyprojekt.nodeagentlib.dto.TaskStatusMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Slf4j
@Service
@ApplicationScope
@RequiredArgsConstructor
public class TaskMessageService implements MessageService {
    private static final Queue<TaskLogMessage> taskLogs = new ConcurrentLinkedQueue<>();
    private static final Queue<TaskStatusMessage> taskStatus = new ConcurrentLinkedQueue<>();

    @Override
    public void save(TaskLogMessage logEntry) {
        log.info("TaskLogMessage: {}", logEntry);
        taskLogs.add(logEntry);
    }

    @Override
    public void save(TaskStatusMessage taskStatusMessage) {
        log.info("TaskStatusMessage: {}", taskStatusMessage);
        taskStatus.add(taskStatusMessage);
    }

    @Override
    public boolean isLogQueueEmpty() {
        return taskLogs.isEmpty();
    }

    @Override
    public boolean isStatusQueueEmpty() {
        return taskStatus.isEmpty();
    }

    @Override
    public Optional<TaskLogMessage> getLogMessage() {
        return Optional.ofNullable(taskLogs.poll());
    }

    @Override
    public Optional<TaskStatusMessage> getStatusMessage() {
        return Optional.ofNullable(taskStatus.poll());
    }
}
