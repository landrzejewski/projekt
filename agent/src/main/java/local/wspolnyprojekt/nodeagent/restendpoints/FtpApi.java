package local.wspolnyprojekt.nodeagent.restendpoints;

import local.wspolnyprojekt.nodeagent.common.RestEndpoints;
import local.wspolnyprojekt.nodeagent.workspaceutils.WorkspaceUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.*;

@Slf4j
@RestController
public class FtpApi {

    @GetMapping(value = RestEndpoints.FTP_GET_FILE, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    InputStreamResource getFile(@PathVariable String taskid, @PathVariable String filename) throws FileNotFoundException {
        return WorkspaceUtils.getFileAsInputStreamResource(taskid, filename);
    }

    @PostMapping(value = RestEndpoints.FTP_POST_FILE)
    void postFile(@PathVariable String taskid, @PathVariable String filename, InputStream inputStream) throws IOException {
        try (FileOutputStream outputStream = WorkspaceUtils.getFileAsFileOutputStream(taskid, filename)) {
            IOUtils.copy(inputStream, outputStream);
        } finally {
            inputStream.close();
        }
    }

    @DeleteMapping(RestEndpoints.FTP_DELETE_FILE)
    void deleteFile(@PathVariable String taskid, @PathVariable String filename) throws IOException {
        File file = WorkspaceUtils.getFileInWorkspaceAsFile(taskid, filename);
        if (!file.delete()) {
            throw new IOException("Can't delete " + filename);
        }
    }

}
