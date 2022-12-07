package local.wspolnyprojekt.nodeagent.restendpoints;

import local.wspolnyprojekt.nodeagent.task.Task;
import local.wspolnyprojekt.nodeagent.task.TasksService;
import local.wspolnyprojekt.nodeagent.workspaceutils.WorkspaceUtils;
import local.wspolnyprojekt.nodeagentlib.dto.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class FtpApiTest {
    private WorkspaceUtils workspaceUtils;
    private TasksService tasksService;
    private String taskid;
    private Task task;

    private FtpApi ftpApi;

    @BeforeEach
    void init() {
        workspaceUtils = Mockito.mock(WorkspaceUtils.class);
        tasksService = Mockito.mock(TasksService.class);
        taskid = UUID.randomUUID().toString();
        task = new Task(null, workspaceUtils, taskid);
        ftpApi = new FtpApi(workspaceUtils, tasksService);
        when(tasksService.hasTask(anyString())).thenReturn(true);
        when(tasksService.getTask(taskid)).thenReturn(task);
    }

    @Test
    void shouldGetFileCallWorkspaceUtils() {
        String filename = "filename";
        try {
            ftpApi.getFile(taskid, filename);
            verify(workspaceUtils, times(1)).getFileAsInputStreamResource(taskid, filename);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void shouldPostFileCallWorkspaceUtils() {
        String filename = "filename";
        try {
            ftpApi.postFile(taskid, filename, null);
            verify(workspaceUtils, times(1))
                    .saveInputStreamToWorkspace(taskid, null, filename);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void shouldPostFileCallCreateTask() {
        String filename = "filename";
        when(tasksService.hasTask(anyString())).thenReturn(false);
        try {
            ftpApi.postFile(taskid, filename, null);
            verify(tasksService, times(1)).addTask(taskid);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void shouldPostFileChangeStatusOfTaskToReady() {
        String filename = "filename";
        try {
            ftpApi.postFile(taskid, filename, null);
            assertThat(task.getStatus().getDtoTaskStatus()).isEqualTo(TaskStatus.TASK_STATUS_READY);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
