package local.wspolnyprojekt.nodeagent.restendpoints;

import local.wspolnyprojekt.nodeagentlib.dto.RestEndpoints;
import local.wspolnyprojekt.nodeagent.workspaceutils.WorkspaceUtils;
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
public class FtpApi {

    @GetMapping(value = RestEndpoints.FTP_ENDPOINT, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    InputStreamResource getFile(@PathVariable(name = TASKID_PATH_VARIABLE) String taskid, @PathVariable(name = FILENAME_PATH_VARIABLE) String filename) throws FileNotFoundException {
        return WorkspaceUtils.getFileAsInputStreamResource(taskid, filename);
    }

    @PostMapping(value = RestEndpoints.FTP_ENDPOINT)
    void postFile(@PathVariable(name = TASKID_PATH_VARIABLE) String taskid, @PathVariable(name = FILENAME_PATH_VARIABLE) String filename, InputStream inputStream) throws IOException {
        try (FileOutputStream outputStream = WorkspaceUtils.getFileAsFileOutputStream(taskid, filename)) {
            IOUtils.copy(inputStream, outputStream);
        } finally {
            inputStream.close();
        }
    }

    @DeleteMapping(RestEndpoints.FTP_ENDPOINT)
    void deleteFile(@PathVariable(name = TASKID_PATH_VARIABLE) String taskid, @PathVariable(name = FILENAME_PATH_VARIABLE) String filename) throws IOException {
        File file = WorkspaceUtils.getFileInWorkspaceAsFile(taskid, filename);
        if (!file.delete()) {
            throw new IOException("Can't delete " + filename);
        }
    }

}
