package local.wspolnyprojekt.nodeagent.statusbroadcast;

import local.wspolnyprojekt.nodeagent.communicationqueues.TaskMessageService;
import local.wspolnyprojekt.nodeagent.task.Task;
import local.wspolnyprojekt.nodeagent.task.state.TaskState;
import local.wspolnyprojekt.nodeagentlib.dto.TaskStatusMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

import java.time.LocalDateTime;

@Service
@ApplicationScope
@RequiredArgsConstructor
public class ServerStatusListener implements StatusListener {
    private final TaskMessageService taskMessageService;

    @Override
    public void receiveStatus(Task task, TaskState state) {
        //TODO Na razie czas leci jako LocalDateTime->String - docelowo ma lecieć w jakimś konkretnym formacie albo jako "podobiekt"?
        taskMessageService.save(new TaskStatusMessage(LocalDateTime.now().toString(), task.getTaskId(), state.getDtoTaskStatus(), ""));
    }
}
