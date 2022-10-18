package local.wspolnyprojekt.nodeagent.shellcommand;

import local.wspolnyprojekt.nodeagentlib.dto.ShellCommand;
import local.wspolnyprojekt.nodeagent.workspaceutils.WorkspaceUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;

@Slf4j
@ApplicationScope
@Service
@RequiredArgsConstructor
public class CommandExecutorService {

    private final LogPersistenceService logPersistenceService;
    private final WorkspaceUtils workspaceUtils;

    public int executeCommand(ShellCommand shellCommand, String taskid) {
//        log.info("task: {} command: {}", taskid, shellCommand);
//        log.info("workspace: {}", workspaceUtils.getWorkspaceDirAsFile(taskid).getAbsolutePath());
        try {
            var process = new ProcessBuilder()
                    .directory(workspaceUtils.getWorkspaceDirAsFile(taskid))
                    .command(createCommand(shellCommand))
                    .start();
            var outputGrabber = new OutputGrabber(process.getInputStream(),
                    output -> logPersistenceService.save(taskid, output)
            );
            var future = Executors.newSingleThreadExecutor().submit(outputGrabber);
            int exitCode = process.waitFor();
            var futureResult = future.get();
//            log.info("exit: {} future: {}", exitCode, futureResult);
            return exitCode;
        } catch (Exception exception) {
            // TODO Obsługa poszczególnych klas wyjątków - na razie jest "aby ruszyło"
            System.out.println(exception.getMessage());
            return -1;
        }
    }

    private static List<String> createCommand(ShellCommand shellCommand) {
        List<String> command = new LinkedList<>();
        command.add(shellCommand.getCommand());
        command.addAll(Arrays.stream(shellCommand.getArgs()).toList());
        return command;
    }

}
