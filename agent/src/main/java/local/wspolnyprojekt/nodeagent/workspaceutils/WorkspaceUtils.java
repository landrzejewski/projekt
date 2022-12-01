package local.wspolnyprojekt.nodeagent.workspaceutils;

import local.wspolnyprojekt.nodeagent.configuration.NodeConfigurationProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

@Slf4j
@Component
@ApplicationScope
@RequiredArgsConstructor
public class WorkspaceUtils {

    private final NodeConfigurationProperties nodeConfigurationProperties;

    public File getWorkspaceDirAsFile(String taskid) {
        return new File(getWorkspaceDirAsString(taskid));
    }

    public File getWorkspaceDirAsFile() {
        return getWorkspaceDirAsFile(null);
    }

    public File getFileInWorkspaceAsFile(String taskid, String filename) {
        return new File(getWorkspaceDirAsString(taskid) + filename);
    }

    public InputStreamResource getFileAsInputStreamResource(String taskid, String filename) throws FileNotFoundException {
        File file = getFileInWorkspaceAsFile(taskid, filename);
        return new InputStreamResource(new FileInputStream(file));
    }

    public FileOutputStream getFileAsFileOutputStream(String taskid, String filename) throws IOException {
        File file = getFileInWorkspaceAsFile(taskid, filename);
        file.getParentFile().mkdirs();
        return new FileOutputStream(file);
    }

    /**
     * Usuwanie workspace taska (wszystkie pliki)
     * @param taskId - identyfikator taska
     * @return - false jeśli nie dało się czegoś usunąć i jakieś "śmieci" pozostały na dysku
     */
    public boolean deleteWorkspace(String taskId) {
        return deleteDirectory(getWorkspaceDirAsFile(taskId));
    }

    private String getWorkspaceDirAsString(String taskid) {
        return nodeConfigurationProperties.getWorkspaceDirectory()
                + (taskid == null ? "" : File.separator + taskid + File.separator);
    }

    private boolean deleteDirectory(File directory) {
        try {
            Files.walk(directory.toPath()).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

}
