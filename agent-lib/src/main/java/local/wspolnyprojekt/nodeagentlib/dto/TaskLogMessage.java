package local.wspolnyprojekt.nodeagentlib.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class TaskLogMessage implements JsonString {
    String timestamp;
    String senderId;
    String payload;

    @JsonCreator
    public TaskLogMessage(@JsonProperty("timestamp") String timestamp,
                          @JsonProperty("senderId") String senderId,
                          @JsonProperty("payload") String payload) {
        this.timestamp = timestamp;
        this.senderId = senderId;
        this.payload = payload;
    }
}
