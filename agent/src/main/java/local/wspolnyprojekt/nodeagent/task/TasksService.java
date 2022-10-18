package local.wspolnyprojekt.nodeagent.task;

import local.wspolnyprojekt.nodeagent.docker.DockerService;
import local.wspolnyprojekt.nodeagent.git.GitClient;
import local.wspolnyprojekt.nodeagent.statusbroadcast.StatusBroadcaster;
import local.wspolnyprojekt.nodeagent.task.state.*;
import local.wspolnyprojekt.nodeagent.workspaceutils.WorkspaceUtils;
import local.wspolnyprojekt.nodeagentlib.dto.GitResource;
import local.wspolnyprojekt.nodeagentlib.dto.TaskCommand;
import local.wspolnyprojekt.nodeagentlib.dto.TaskStatus;
import lombok.RequiredArgsConstructor;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.synchronizedMap;

@RequiredArgsConstructor
@Service
@ApplicationScope
public class TasksService {

    private final DockerService dockerService;
    private final GitClient gitClient;
    private final StatusBroadcaster statusBroadcaster;
    private final WorkspaceUtils workspaceUtils;
    Map<String, Task> tasks = synchronizedMap(new HashMap<>());

    public ResponseEntity executeCommand(String taskId, TaskCommand taskCommand) throws FileNotFoundException {

        return switch (taskCommand) {
            case TASK_COMMAND_START -> start(getTask(taskId));
            case TASK_COMMAND_STOP -> down(getTask(taskId));
            case TASK_COMMAND_CLEANUP -> cleanup(getTask(taskId));
            case TASK_COMMAND_STATUS -> ResponseEntity.ok(getStatus(taskId));
            case TASK_COMMAND_LOG -> ResponseEntity.accepted()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(getLog(getTask(taskId)));
            case TASK_COMMAND_DELETE -> delete(getTask(taskId));
        };

    }

    public boolean hasTask(String taskId) {
        return tasks.containsKey(taskId);
    }

    public Task getTask(String taskId) {
        if (hasTask(taskId)) {
            return tasks.get(taskId);
        }
        throw new RuntimeException("No task");
    }

    public void addTask(Task task) {
        tasks.put(task.getTaskId(), task);
    }

    public void deleteTask(Task task) {
        tasks.remove(task);
    }

    public void clone(GitResource gitResource, String taskid) {
        Task task;
        if (!hasTask(taskid)) {
            task = new Task(statusBroadcaster, workspaceUtils, taskid);
            addTask(task);
        } else {
            task = getTask(taskid);
        }
        if (task.getSemaphore().tryAcquire()) {
            gitClient.clone(gitResource, task);
            task.getSemaphore().release();
        } else {
            throw new RuntimeException("BUSY");
        }
    }

    public ResponseEntity start(Task task) {
        dockerService.buildAndRun(task);
        return ResponseEntity.ok().build(); // TODO
    }

    public ResponseEntity down(Task task) {
        dockerService.down(task);
        return ResponseEntity.ok().build(); // TODO
    }

    public ResponseEntity cleanup(Task task) {
        dockerService.cleanup(task);
        return ResponseEntity.ok().build(); // TODO
    }

    public ResponseEntity delete(Task task) {
        dockerService.delete(task);
        return ResponseEntity.ok().build(); // TODO
    }

    public InputStreamResource getLog(Task task) throws FileNotFoundException {
        return dockerService.getLog(task);
    }

    public TaskStatus getStatus(String taskId) {
        if (!hasTask(taskId)) {
            return TaskStatus.TASK_STATUS_NULL;
        }
        return getTask(taskId).getStatus().getDtoTaskStatus();
    }

    public void pull(Task task) {
        if (task.getSemaphore().tryAcquire()) {
            gitClient.pull(task);
            task.getSemaphore().release();
        } else {
            throw new RuntimeException("BUSY");
        }
    }
}
