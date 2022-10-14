package local.wspolnyprojekt.nodeagentlib.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;

@Value
public class TaskStatusMessage implements JsonString {
    String timestamp = LocalDateTime.now().toString();
    String taskId;
    TaskStatus taskStatus;
    String description;
}
