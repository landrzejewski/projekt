package local.wspolnyprojekt.nodeagent.task;

import local.wspolnyprojekt.nodeagent.statusbroadcast.StatusBroadcaster;
import local.wspolnyprojekt.nodeagent.task.state.TaskState;
import local.wspolnyprojekt.nodeagent.task.state.TaskStateNull;
import local.wspolnyprojekt.nodeagent.workspaceutils.WorkspaceUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.File;
import java.util.concurrent.Semaphore;

@RequiredArgsConstructor
public class Task {

    private final StatusBroadcaster statusBroadcaster;
    private final WorkspaceUtils workspaceUtils;

    @Getter
    private final String taskId;

    @Getter
    private TaskState status = new TaskStateNull();

    @Getter
    private Semaphore semaphore = new Semaphore(1);

    /**
     * Na potrzeby Dockera: gdy zadanie jest uruchomione i pójdzie polecenie STOP, to zadanie w Dockerze wyśle
     * FAIL (bo się nie powiedzie, ale to jest porządane zachowanie, więc ten FAIL nie powinien iść do serwera)
     */
    volatile private boolean sendNextStatusFlag = true;

    public void disableSendingNextStatus() {
        sendNextStatusFlag = false;
    }

    public boolean getAndResetSendingNextStatus() {
        return sendNextStatusFlag ? sendNextStatusFlag : (sendNextStatusFlag = true) && false;
    }

    public File getWorkspaceAsFile() {
        return workspaceUtils.getWorkspaceDirAsFile(taskId);
    }

    public void setStatus(TaskState status, String description) {
        this.status = status;
        if (statusBroadcaster != null)
            statusBroadcaster.broadcastStatusChange(this, description);
    }

    public void setStatus(TaskState status) {
        setStatus(status, "");
    }

}
