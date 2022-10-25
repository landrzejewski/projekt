package local.wspolnyprojekt.nodeagentlib.dto;

import lombok.Data;

//TODO Dopisać testy
@Data
public class NodeRegistrationEntity implements JsonString {
    String nodeId;
    String host;
    Integer port;
}
