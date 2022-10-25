package local.wspolnyprojekt.nodeagentlib.dto;

import lombok.Value;

@Value
public class TaskLogMessage implements JsonString {
    String timestamp;
    String senderId;
    String payload;
}
