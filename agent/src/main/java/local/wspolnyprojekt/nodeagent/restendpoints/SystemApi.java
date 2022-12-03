package local.wspolnyprojekt.nodeagent.restendpoints;

import local.wspolnyprojekt.nodeagentlib.dto.RestEndpoints;
import local.wspolnyprojekt.nodeagent.nodeinfo.NodeLoad;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SystemApi {

    public final NodeLoad nodeLoad;

    @GetMapping(RestEndpoints.SYSTEM_LOAD)
    local.wspolnyprojekt.nodeagentlib.dto.NodeLoad getNodeLoad() {
        return nodeLoad.getLoadData();
    }

    @GetMapping(RestEndpoints.SYSTEM_PING)
    String pingResponse() {
        return "PING";
    }
}
