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
import java.util.stream.Collectors;

@Slf4j
@ApplicationScope
@Service
@RequiredArgsConstructor
public class CommandExecutorService {

    private final OutputPersistence outputPersistence;

    public void executeCommand(ShellCommand shellCommand, String taskid) {
        log.info("task: {} command: {}", taskid, shellCommand);
        log.info("workspace: {}", WorkspaceUtils.getWorkspaceDirAsFile(taskid).getAbsolutePath());
        try {
            var process = new ProcessBuilder()
                    .directory(WorkspaceUtils.getWorkspaceDirAsFile(taskid))
                    .command(createCommand(shellCommand))
                    .start();
            var outputGrabber = new OutputGrabber(process.getInputStream(),
                    output -> outputPersistence.save(taskid, output)
            );
            var future = Executors.newSingleThreadExecutor().submit(outputGrabber);
// TODO Na razie bez implementacji timeoutu, bo nie ma jeszcze kolejkowania (request jest blokujący) i obsługi statusów
//            boolean waitResult = process.waitFor(shellCommand.getTimeoutInSeconds(), TimeUnit.SECONDS);
//            if (!waitResult) {
//                process.destroy();
//            }
            int waitResult = process.waitFor();
            int exitCode = process.exitValue();
            var futureResult = future.get();
            log.info("wait: {} exit: {} future: {}", waitResult, exitCode, futureResult);
        } catch (Exception exception) {
            // TODO Obsługa poszczególnych klas wyjątków - na razie jest "aby ruszyło"
            System.out.println(exception.getMessage());
        }

    }

    private static List<String> createCommand(ShellCommand shellCommand) {
        List<String> command = new LinkedList<>();
        command.add(shellCommand.getCommand());
//        command.addAll(Arrays.stream(shellCommand.getArgs()).toList()); // JDK17
        command.addAll(Arrays.stream(shellCommand.getArgs()).collect(Collectors.toList()));
        return command;
    }

}
