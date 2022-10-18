package local.wspolnyprojekt.nodeagent.restendpoints;

import local.wspolnyprojekt.nodeagent.task.Task;
import local.wspolnyprojekt.nodeagent.task.TasksService;
import local.wspolnyprojekt.nodeagent.task.state.TaskStateReady;
import local.wspolnyprojekt.nodeagentlib.dto.RestEndpoints;
import local.wspolnyprojekt.nodeagent.workspaceutils.WorkspaceUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.*;

import static local.wspolnyprojekt.nodeagentlib.dto.RestEndpoints.FILENAME_PATH_VARIABLE;
import static local.wspolnyprojekt.nodeagentlib.dto.RestEndpoints.TASKID_PATH_VARIABLE;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FtpApi {

    private final WorkspaceUtils workspaceUtils;
    private final TasksService tasksService;

    @GetMapping(value = RestEndpoints.FTP_ENDPOINT, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    InputStreamResource getFile(@PathVariable(name = TASKID_PATH_VARIABLE) String taskid, @PathVariable(name = FILENAME_PATH_VARIABLE) String filename) throws FileNotFoundException {
        return workspaceUtils.getFileAsInputStreamResource(taskid, filename);
    }

    @PostMapping(value = RestEndpoints.FTP_ENDPOINT)
    void postFile(@PathVariable(name = TASKID_PATH_VARIABLE) String taskid, @PathVariable(name = FILENAME_PATH_VARIABLE) String filename, InputStream inputStream) throws IOException {
        if(!tasksService.hasTask(taskid)) {
            tasksService.addTask(taskid);
        }
        try (inputStream; FileOutputStream outputStream = workspaceUtils.getFileAsFileOutputStream(taskid, filename)) {
            IOUtils.copy(inputStream, outputStream);
            tasksService.getTask(taskid).setStatus(new TaskStateReady());
        }
    }

    @DeleteMapping(RestEndpoints.FTP_ENDPOINT)
    void deleteFile(@PathVariable(name = TASKID_PATH_VARIABLE) String taskid, @PathVariable(name = FILENAME_PATH_VARIABLE) String filename) throws IOException {
        File file = workspaceUtils.getFileInWorkspaceAsFile(taskid, filename);
        if (!file.delete()) {
            throw new IOException("Can't delete " + filename);
        }
    }

}
