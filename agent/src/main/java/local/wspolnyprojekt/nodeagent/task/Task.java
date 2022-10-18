package local.wspolnyprojekt.nodeagent.task;

import local.wspolnyprojekt.nodeagent.statusbroadcast.StatusBroadcaster;
import local.wspolnyprojekt.nodeagent.task.state.TaskState;
import local.wspolnyprojekt.nodeagent.task.state.TaskStateNull;
import local.wspolnyprojekt.nodeagent.workspaceutils.WorkspaceUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.io.File;
import java.util.concurrent.Semaphore;

@RequiredArgsConstructor
public class Task {

    private final StatusBroadcaster statusListener;
    private final WorkspaceUtils workspaceUtils;

    @Getter
    private final String taskId;

    @Getter
    private TaskState status = new TaskStateNull();

    @Getter
    private Semaphore semaphore = new Semaphore(1);

    public File getWorkspaceAsFile() {
        return workspaceUtils.getWorkspaceDirAsFile(taskId);
    }

    public void setStatus(TaskState status, String description) {
        this.status = status;
        statusListener.broadcastStatusChange(this, description);
    }

    public void setStatus(TaskState status) {
        setStatus(status,"");
    }

 }
