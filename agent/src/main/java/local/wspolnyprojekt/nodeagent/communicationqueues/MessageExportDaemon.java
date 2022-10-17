package local.wspolnyprojekt.nodeagent.communicationqueues;

import local.wspolnyprojekt.nodeagent.server.ServerCommunicationService;
import local.wspolnyprojekt.nodeagentlib.dto.TaskLogMessage;
import local.wspolnyprojekt.nodeagentlib.dto.TaskStatusMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageExportDaemon {
    private final TaskMessageService taskMessageService;
    private final ServerCommunicationService serverCommunicationService;


    @Scheduled(fixedDelay = 100, timeUnit = TimeUnit.MILLISECONDS)
    void exportTaskStatus() {
        if(!serverCommunicationService.isRegistered())
            return;
        if (!taskMessageService.isStatusQueueEmpty()) {
            Optional<TaskStatusMessage> entry;
            while ((entry = taskMessageService.getStatusMessage()).isPresent()) {
                serverCommunicationService.sendTaskStatus(entry.get());
            }
        }
    }

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.SECONDS)
    void exportTaskLogs() {
        if(!serverCommunicationService.isRegistered()) {
            serverCommunicationService.registerAgent();
            return;
        }

        if (!taskMessageService.isLogQueueEmpty()) {
            Optional<TaskLogMessage> entry;
            while ((entry = taskMessageService.getLogMessage()).isPresent()) {
                serverCommunicationService.sendTaskLog(entry.get());
            }
        }

    }

}
