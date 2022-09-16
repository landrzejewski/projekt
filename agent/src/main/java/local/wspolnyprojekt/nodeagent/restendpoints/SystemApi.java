package local.wspolnyprojekt.nodeagent.restendpoints;

import local.wspolnyprojekt.nodeagent.common.NodeLoad;
import local.wspolnyprojekt.nodeagent.common.RestEndpoints;
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
}
