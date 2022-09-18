package local.wspolnyprojekt.nodeagent.shellcommand;

import local.wspolnyprojekt.nodeagent.workspaceutils.WorkspaceUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.*;
import java.time.LocalDateTime;

@Slf4j
@Component
public class FileOutputPersistence implements OutputPersistence {
    @Override
    public void save(String taskid, String text) {
        log.info("{} {}", taskid, text);
        File file = WorkspaceUtils.getFileInWorkspaceAsFile(taskid, "output.log");
        try (FileOutputStream fileOutputStream = new FileOutputStream(file, true);
             PrintWriter printWriter = new PrintWriter(fileOutputStream)) {
            printWriter.println(LocalDateTime.now() + ": " + text);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
