package local.wspolnyprojekt.nodeagent.restendpoints;

import local.wspolnyprojekt.nodeagentlib.common.RestEndpoints;
import local.wspolnyprojekt.nodeagentlib.common.ShellCommand;
import local.wspolnyprojekt.nodeagent.shellcommand.CommandExecutorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static local.wspolnyprojekt.nodeagentlib.common.RestEndpoints.TASKID_PATH_VARIABLE;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ShellCommandsApi {

    private final CommandExecutorService commandExecutorService;

    @PostMapping(RestEndpoints.EXECUTE_SHELL_COMMAND_IN_WORKSPACE)
    ResponseEntity<String> execute(@RequestBody ShellCommand command, @PathVariable(name = TASKID_PATH_VARIABLE) String taskid) {
        log.info("{}", command);
        commandExecutorService.executeCommand(command, taskid); // TODO Na razie blokujące i bez kolejkowania
        return ResponseEntity.of(Optional.of(command.toString()));
    }

}
