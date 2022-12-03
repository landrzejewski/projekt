package local.wspolnyprojekt.nodeagent.task;

import local.wspolnyprojekt.nodeagentlib.dto.TaskCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

import java.io.FileNotFoundException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

@Service
@ApplicationScope
@RequiredArgsConstructor
@Slf4j
public class TaskAutorunService {
    private static final Queue<Task> tasksToAutorun = new ConcurrentLinkedQueue<>();
    private final TasksService tasksService;

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.SECONDS)
    void autorun() {
        while (!tasksToAutorun.isEmpty()) {
            Task task = tasksToAutorun.poll();
            try {
                log.info("Autorun: {}", task);
                tasksService.executeCommand(task, TaskCommand.TASK_COMMAND_START);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void addToAutorun(Task task) {
        log.info("Add to autorun: {}", task);
        tasksToAutorun.add(task);
    }

}
