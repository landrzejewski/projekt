package local.wspolnyprojekt.nodeagent.docker;

import local.wspolnyprojekt.nodeagentlib.dto.ShellCommand;
import local.wspolnyprojekt.nodeagent.shellcommand.CommandExecutorService;
import local.wspolnyprojekt.nodeagent.workspaceutils.WorkspaceUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

import java.io.*;

@Slf4j
@ApplicationScope
@Service
@RequiredArgsConstructor
public class DockerService {

    private final CommandExecutorService commandExecutorService;

    public void buildAndRun(String taskid) {
        executeDockerComposeCommand(new String[]{"up", "--build"}, taskid);
    }

    public void down(String taskid) {
        executeDockerComposeCommand(new String[]{"down"}, taskid);
    }

    public InputStreamResource getLog(String taskid) throws FileNotFoundException {
        return WorkspaceUtils.getFileAsInputStreamResource(taskid, "output.log");
    }

    public void cleanup(String taskid) {
        executeDockerCommand(new String[]{"volume", "prune", "-f"}, taskid);
        executeDockerCommand(new String[]{"builder", "prune", "-f"}, taskid);
        executeDockerCommand(new String[]{"image", "prune", "--all", "-f"}, taskid);
    }

    private void executeDockerComposeCommand(String[] args, String taskid) {
        ShellCommand shellCommand = new ShellCommand();
        shellCommand.setCommand("docker-compose");
        shellCommand.setArgs(args);
        shellCommand.setTimeoutInSeconds(-1);
        commandExecutorService.executeCommand(shellCommand, taskid);
    }

    private void executeDockerCommand(String[] args, String taskid) {
        ShellCommand shellCommand = new ShellCommand();
        shellCommand.setCommand("docker");
        shellCommand.setArgs(args);
        shellCommand.setTimeoutInSeconds(-1);
        commandExecutorService.executeCommand(shellCommand, taskid);
    }

}
