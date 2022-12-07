package local.wspolnyprojekt.nodeagent.restendpoints;

import local.wspolnyprojekt.nodeagent.task.TasksService;
import local.wspolnyprojekt.nodeagentlib.dto.RestEndpoints;
import local.wspolnyprojekt.nodeagentlib.dto.TaskCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;

import static local.wspolnyprojekt.nodeagentlib.dto.RestEndpoints.TASKID_PATH_VARIABLE;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TaskApi {

    private final TasksService tasksService;

    @PostMapping(RestEndpoints.TASK_ENDPOINT)
    ResponseEntity taskEndpoint(@PathVariable(name = TASKID_PATH_VARIABLE) String taskId, @RequestBody String taskCommandString) throws FileNotFoundException {
        log.info("{}", taskCommandString);
        return tasksService.executeCommand(taskId, TaskCommand.valueOf(taskCommandString.replace("\"", "")));
    }

}
