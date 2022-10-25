package local.wspolnyprojekt.nodeagentlib.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Value
public class TaskStatusMessage implements JsonString {
    String timestamp;
    String taskId;
    TaskStatus taskStatus;
    String description;

    @JsonCreator
    public TaskStatusMessage(@JsonProperty("timestamp") String timestamp,
                             @JsonProperty("taskId") String taskId,
                             @JsonProperty("taskStatus") TaskStatus taskStatus,
                             @JsonProperty("description") String description) {
        this.timestamp = timestamp;
        this.taskId = taskId;
        this.taskStatus = taskStatus;
        this.description = description;
    }
}
