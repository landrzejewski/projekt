package local.wspolnyprojekt.nodeagent.docker;

import local.wspolnyprojekt.nodeagent.task.Task;
import local.wspolnyprojekt.nodeagent.task.state.TaskStateDone;
import local.wspolnyprojekt.nodeagent.task.state.TaskStateFail;
import local.wspolnyprojekt.nodeagent.task.state.TaskStateRunning;
import local.wspolnyprojekt.nodeagentlib.dto.ShellCommand;
import local.wspolnyprojekt.nodeagent.shellcommand.CommandExecutorService;
import local.wspolnyprojekt.nodeagent.workspaceutils.WorkspaceUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

import java.io.*;

@Slf4j
@ApplicationScope
@Service
@RequiredArgsConstructor
public class DockerService {

    private final CommandExecutorService commandExecutorService;
    private final WorkspaceUtils workspaceUtils;

    @Async
    public void buildAndRun(Task task) {
        if (task.getSemaphore().tryAcquire()) {
            task.setStatus(new TaskStateRunning());
            var exitCode = executeDockerComposeCommand(new String[]{"up", "--build"}, task);
            task.getSemaphore().release();
            if (exitCode == 0) {
                task.setStatus(new TaskStateDone());
            } else {
                task.setStatus(new TaskStateFail(),"details in log");
            }
        } else {
            log.error("{} BUSY",task.getTaskId());
            throw new RuntimeException("BUSY"); //TODO Co ma dostawać serwer jeśli zadanie działa a pójdzie polecenie ponownego uruchomienia?
        }
    }

    @Async
    public void down(Task task) {
        var exitCode = executeDockerComposeCommand(new String[]{"down"}, task);
    }

    public InputStreamResource getLog(Task task) throws FileNotFoundException {
        return workspaceUtils.getFileAsInputStreamResource(task.getTaskId(), "output.log");
    }

    @Async
    public void cleanup(Task task) {
        executeDockerCommand(new String[]{"volume", "prune", "-f"}, task);
        executeDockerCommand(new String[]{"builder", "prune", "-f"}, task);
        executeDockerCommand(new String[]{"image", "prune", "--all", "-f"}, task);
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

    @Async
    public void delete(Task task) {
        workspaceUtils.deleteWorkspace(task.getTaskId());
    }
}
