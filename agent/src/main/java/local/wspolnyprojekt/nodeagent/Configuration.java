package local.wspolnyprojekt.nodeagent;

import lombok.Data;
import lombok.Getter;

import java.io.File;

// TODO Tymczasowo na potrzeby prototypowania, docelowo plik z Properties ewentualnie nadpisywany przez requesty REST
public enum Configuration {
    WORKSPACE_DIRECTORY("C:\\tmp\\workspace");

    @Getter
    private String value;

    Configuration(String value) {
        this.value = value;
    }

    public static File getWorkspaceDir(String taskid) {
        return new File(WORKSPACE_DIRECTORY.getValue() + File.separator + taskid);
    }

}
