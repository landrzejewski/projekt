package local.wspolnyprojekt.nodeagent.shellcommand;

import local.wspolnyprojekt.nodeagentlib.dto.TaskLogMessage;
import local.wspolnyprojekt.nodeagent.communicationqueues.TaskMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LogPersistenceService implements OutputPersistence {

    private final FileOutputPersistence fileOutputPersistence;
    private final TaskMessageService taskMessageService;

    @Override
    public void save(String taskid, String text) {
        fileOutputPersistence.save(taskid, text);
        taskMessageService.save(new TaskLogMessage(LocalDateTime.now().toString(), taskid, text));
    }

}
