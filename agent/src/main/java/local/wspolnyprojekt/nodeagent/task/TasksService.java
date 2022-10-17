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
            case TASK_COMMAND_START -> start(taskId);
            case TASK_COMMAND_STOP -> down(taskId);
            case TASK_COMMAND_CLEANUP -> cleanup(taskId);
            case TASK_COMMAND_STATUS -> ResponseEntity.ok(getStatus(taskId));
            case TASK_COMMAND_LOG -> ResponseEntity.accepted()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(getLog(taskId));
            case TASK_COMMAND_DELETE -> delete(taskId);
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

    public void deleteTask(String taskId) {
        tasks.remove(taskId);
    }

    public void clone(GitResource gitResource, String taskid) {
        Task task = new Task(statusBroadcaster, workspaceUtils, taskid);
        try {
            addTask(task);
            gitClient.clone(gitResource, task);
        } catch (Exception e) {
            task.setStatus(new TaskStateFail(), e.getMessage());
        }
    }

    public ResponseEntity start(String taskid) {
        Task task = getTask(taskid);
        dockerService.buildAndRun(task);
        return ResponseEntity.ok().build(); // TODO
    }

    public ResponseEntity down(String taskid) {
        dockerService.down(taskid);
        return ResponseEntity.ok().build(); // TODO
    }

    public ResponseEntity cleanup(String taskid) {
        dockerService.cleanup(taskid);
        return ResponseEntity.ok().build(); // TODO
    }

    public ResponseEntity delete(String taskId) {
        dockerService.delete(taskId);
        return ResponseEntity.ok().build(); // TODO
    }

    public InputStreamResource getLog(String taskid) throws FileNotFoundException {
        return dockerService.getLog(taskid);
    }

    public TaskStatus getStatus(String taskId) {
        if (!hasTask(taskId)) {
            return TaskStatus.TASK_STATUS_NULL;
        }
        return getTask(taskId).getStatus().getDtoTaskStatus();
    }

    public void pull(String taskid) throws GitAPIException, IOException {
        gitClient.pull(getTask(taskid));
    }
}
