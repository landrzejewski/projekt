package local.wspolnyprojekt.nodeagent.task;

import local.wspolnyprojekt.nodeagentlib.common.GitCredentials;
import local.wspolnyprojekt.nodeagentlib.common.GitResource;
import local.wspolnyprojekt.nodeagent.statusbroadcast.StatusBroadcaster;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

@RequiredArgsConstructor
public class Task {

    @Autowired
    private StatusBroadcaster statusListener;

    @Getter
    private final String taskId;

    @Getter
    private TaskStatus status;

    @Setter
    private GitResource gitResource;

    @Setter
    private GitCredentials gitCredentials;

    public void setStatus(TaskStatus status) {
        this.status = status;
        // TODO callListener
    }

}
