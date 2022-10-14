package local.wspolnyprojekt.nodeagent.nodeinfo;

import com.sun.management.OperatingSystemMXBean;
import local.wspolnyprojekt.nodeagent.workspaceutils.WorkspaceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

import java.io.File;
import java.lang.management.ManagementFactory;


@Service
@ApplicationScope
@RequiredArgsConstructor
public class NodeLoad {

    private final WorkspaceUtils workspaceUtils;

    public local.wspolnyprojekt.nodeagentlib.dto.NodeLoad getLoadData() {
        local.wspolnyprojekt.nodeagentlib.dto.NodeLoad nodeLoad = new local.wspolnyprojekt.nodeagentlib.dto.NodeLoad();
        File workspace = workspaceUtils.getWorkspaceDirAsFile("");
        var sysinfo = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        nodeLoad.setTotalMemorySize(sysinfo.getTotalMemorySize());
        nodeLoad.setFreeMemorySize(sysinfo.getFreeMemorySize());
        nodeLoad.setCpuLoad(sysinfo.getCpuLoad());
        nodeLoad.setAverageCpuLoad(sysinfo.getSystemLoadAverage());   // <- nie działa pod Windowsem
        nodeLoad.setNumberOfProcessors(sysinfo.getAvailableProcessors());
        nodeLoad.setTotalSwapSpace(sysinfo.getTotalSwapSpaceSize());
        nodeLoad.setFreeSwapSpace(sysinfo.getFreeSwapSpaceSize());
        nodeLoad.setTotalDiskSpace(workspace.getTotalSpace());
        nodeLoad.setAvailableDiskSpace(workspace.getUsableSpace());
        return nodeLoad;
    }

}
