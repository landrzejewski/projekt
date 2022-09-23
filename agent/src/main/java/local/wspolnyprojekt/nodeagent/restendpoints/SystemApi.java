package local.wspolnyprojekt.nodeagent.restendpoints;

import local.wspolnyprojekt.nodeagentlib.common.NodeLoad;
import local.wspolnyprojekt.nodeagentlib.common.RestEndpoints;
import local.wspolnyprojekt.nodeagent.serverstatus.NodeStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SystemApi {

    public final NodeStatus nodeStatus;

    @GetMapping(RestEndpoints.SYSTEM_LOAD)
    NodeLoad getNodeLoad() {
        return nodeStatus.getLoadData();
    }

    @GetMapping(RestEndpoints.SYSTEM_PING)
    String pingResponse() {
        return "PING";
    }
}
