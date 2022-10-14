package local.wspolnyprojekt.nodeagent.communicationqueues;

import local.wspolnyprojekt.nodeagentlib.dto.JsonString;
import lombok.Value;

@Value
public class LogEntity implements JsonString {
    String timestamp;
    String senderId;
    String payload;
}
