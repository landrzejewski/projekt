package local.wspolnyprojekt.nodeagent.workspaceutils;

import local.wspolnyprojekt.nodeagent.Configuration;
import org.springframework.core.io.InputStreamResource;

import java.io.*;

// TODO Docelowo pobieranie z ustawień taska, na razie robocza proteza (workspace na podstawie Configuration i id taska)
public class WorkspaceUtils {

    public static File getWorkspaceDirAsFile(String taskid) {
        return new File(getWorkspaceDirAsString(taskid));
    }

    private static String getWorkspaceDirAsString(String taskid) {
        return Configuration.WORKSPACE_DIRECTORY.getValue() + File.separator + taskid + File.separator;
    }

    public static File getFileInWorkspaceAsFile(String taskid, String filename) {
        return new File(getWorkspaceDirAsString(taskid) + filename);
    }

    public static InputStreamResource getFileAsInputStreamResource(String taskid, String filename) throws FileNotFoundException {
        File file = getFileInWorkspaceAsFile(taskid, filename);
        return new InputStreamResource(new FileInputStream(file));
    }
    public static FileOutputStream getFileAsFileOutputStream(String taskid, String filename) throws IOException {
        File file = getFileInWorkspaceAsFile(taskid, filename);
        file.getParentFile().mkdirs();
        return new FileOutputStream(file);
    }

}
