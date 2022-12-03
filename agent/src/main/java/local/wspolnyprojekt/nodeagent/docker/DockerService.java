package local.wspolnyprojekt.nodeagent.docker;

import local.wspolnyprojekt.nodeagent.task.Task;
import local.wspolnyprojekt.nodeagent.task.state.*;
import local.wspolnyprojekt.nodeagent.shellcommand.ShellCommand;
import local.wspolnyprojekt.nodeagent.shellcommand.CommandExecutorService;
import local.wspolnyprojekt.nodeagent.workspaceutils.WorkspaceUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

import java.io.*;
import java.util.Arrays;

@Slf4j
@ApplicationScope
@Service
@RequiredArgsConstructor
public class DockerService {

    private final CommandExecutorService commandExecutorService;
    private final WorkspaceUtils workspaceUtils;

    private boolean isClean = false;

    @Async
    public void buildAndRun(Task task) {
        log.info("Docker build and run: {}", task);
        if (task.getSemaphore().tryAcquire()) {
            task.setStatus(new TaskStateRunning());
            var exitCode = executeDockerComposeCommand(new String[]{"up", "--build"}, task);
            task.getSemaphore().release();
            if (exitCode == 0) {
                task.setStatus(new TaskStateDone());
            } else {
                if (task.getAndResetSendingNextStatus()) {
                    task.setStatus(new TaskStateFail(), "details in log");
                }
            }
        } else {
            log.error("{} BUSY", task.getTaskId());
            throw new RuntimeException("BUSY"); //TODO Co ma dostawać serwer jeśli zadanie działa a pójdzie polecenie ponownego uruchomienia?
        }
    }

    @Async
    public void down(Task task) {
        task.disableSendingNextStatus();
        var exitCode = executeDockerComposeCommand(new String[]{"down"}, task);
        if (exitCode == 0) {
            task.setStatus(new TaskStateStopped());
        } else {
            task.setStatus(new TaskStateFail());
        }
    }

    public InputStreamResource getLog(Task task) throws FileNotFoundException {
        return workspaceUtils.getFileAsInputStreamResource(task.getTaskId(), "output.log");
    }

    @Async
    public void cleanup(Task task) {
        if (task.getSemaphore().tryAcquire()) {
            executeDockerCommand(new String[]{"volume", "prune", "-f"}, task);
            executeDockerCommand(new String[]{"builder", "prune", "-f"}, task);
            executeDockerCommand(new String[]{"image", "prune", "--all", "-f"}, task);
            task.getSemaphore().release();
            task.setStatus(new TaskStateReady());
        } else {
            log.error("{} BUSY", task.getTaskId());
            throw new RuntimeException("BUSY"); //TODO Co ma dostawać serwer jeśli zadanie działa a pójdzie polecenie czyszczenia kontenerów, voluminów i obrazów?
        }
    }

    @Async
    public void delete(Task task) {
        if (task.getSemaphore().tryAcquire()) {
            workspaceUtils.deleteWorkspace(task.getTaskId());
            task.setStatus(new TaskStateDeleted());
        } else {
            log.error("{} BUSY", task.getTaskId());
            throw new RuntimeException("BUSY"); //TODO Co ma dostawać serwer jeśli zadanie działa a pójdzie polecenie usunięcia workspace?
        }
    }

    public void cleanupAfterRestart() {
        if (!isClean) {
            File workspaceRoot = workspaceUtils.getWorkspaceDirAsFile();
            var oldTasks = Arrays.stream(workspaceRoot.listFiles()).filter(File::isDirectory).map(File::getName).toList();
            for (String taskId : oldTasks) {
                log.info("Cleanup old task: {}", taskId);
                Task task = new Task(null, workspaceUtils, taskId);
                down(task);
                cleanup(task);
                delete(task);
            }
            isClean = true;
        }
    }

    private int executeDockerComposeCommand(String[] args, Task task) {
        ShellCommand shellCommand = new ShellCommand();
        shellCommand.setCommand("docker-compose");
        shellCommand.setArgs(args);
        shellCommand.setTimeoutInSeconds(-1);
        return commandExecutorService.executeCommand(shellCommand, task.getTaskId());
    }

    private int executeDockerCommand(String[] args, Task task) {
        ShellCommand shellCommand = new ShellCommand();
        shellCommand.setCommand("docker");
        shellCommand.setArgs(args);
        shellCommand.setTimeoutInSeconds(-1);
        return commandExecutorService.executeCommand(shellCommand, task.getTaskId());
    }


}
