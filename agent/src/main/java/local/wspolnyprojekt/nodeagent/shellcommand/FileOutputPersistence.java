package local.wspolnyprojekt.nodeagent.shellcommand;

import local.wspolnyprojekt.nodeagent.workspaceutils.WorkspaceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.*;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class FileOutputPersistence implements OutputPersistence {
    private final WorkspaceUtils workspaceUtils;
    @Override
    public void save(String taskid, String text) {
        File file = workspaceUtils.getFileInWorkspaceAsFile(taskid, "output.log");
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
