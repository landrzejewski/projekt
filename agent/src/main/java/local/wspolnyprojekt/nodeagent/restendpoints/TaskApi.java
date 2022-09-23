package local.wspolnyprojekt.nodeagent.restendpoints;

import jdk.jshell.spi.ExecutionControl.NotImplementedException;
import local.wspolnyprojekt.nodeagentlib.common.RestEndpoints;
import local.wspolnyprojekt.nodeagentlib.common.TaskCommand;
import local.wspolnyprojekt.nodeagent.docker.DockerService;
import local.wspolnyprojekt.nodeagent.task.TaskStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.FileNotFoundException;

import static local.wspolnyprojekt.nodeagentlib.common.RestEndpoints.TASKID_PATH_VARIABLE;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TaskApi {

    private final DockerService dockerService;

// TODO Docelowo będzie TaskService i funkcjonalności będą przeniesione do niego. Na razie zostawione aby zachować "uruchamialność" kodu.

    //    @GetMapping(RestEndpoints.TASK_START)
    void buildAndRun(@PathVariable(name = TASKID_PATH_VARIABLE) String taskid) {
        dockerService.buildAndRun(taskid);
    }

    //    @GetMapping(RestEndpoints.TASK_STOP)
    void down(@PathVariable(name = TASKID_PATH_VARIABLE) String taskid) {
        dockerService.down(taskid);
    }

    @GetMapping(value = RestEndpoints.TASK_LOG, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    InputStreamResource getLog(@PathVariable(name = TASKID_PATH_VARIABLE) String taskid) throws FileNotFoundException {
        return dockerService.getLog(taskid);
    }

    //    @GetMapping(RestEndpoints.TASK_CLEANUP)
    void cleanup(@PathVariable(name = TASKID_PATH_VARIABLE) String taskid) {
        dockerService.cleanup(taskid);
    }

    @GetMapping(RestEndpoints.TASK_STATUS)
    TaskStatus status(@PathVariable(name = TASKID_PATH_VARIABLE) String taskId) {
        return TaskStatus.TASK_OK; // TODO Zaimplementować, na razie puste.
    }

    @GetMapping(RestEndpoints.TASK_DELETE)
    void delete(@PathVariable(name = TASKID_PATH_VARIABLE) String taskId) {
        // TODO Zaimplementować, na razie puste
    }

    @PostMapping(RestEndpoints.TASK_ENDPOINT)
    void taskEndpoint(@PathVariable(name = TASKID_PATH_VARIABLE) String taskId, @RequestBody String taskCommand) throws NotImplementedException {
        TaskCommand command = TaskCommand.valueOf(taskCommand.replace("\"", "")); // TODo Docelowo zrobić porządnego mappera z obsługą błędów :D
        switch (command) {
            case TASK_COMMAND_START:
                buildAndRun(taskId);
                break;
            case TASK_COMMAND_STOP:
                down(taskId);
                break;
            case TASK_COMMAND_CLEANUP:
                cleanup(taskId);
                break;
//            case TASK_COMMAND_STATUS:
//                status(taskId);
//                break;
//            case TASK_COMMAND_LOG:
//                getLog(taskId);
//                break;
            case TASK_COMMAND_DELETE:
                delete(taskId);
                break;
            default:
                throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
        }

    }

}
