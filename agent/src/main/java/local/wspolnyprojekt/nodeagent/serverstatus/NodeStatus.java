package local.wspolnyprojekt.nodeagent.serverstatus;

import local.wspolnyprojekt.nodeagent.common.NodeLoad;
import local.wspolnyprojekt.nodeagent.workspaceutils.WorkspaceUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

import com.sun.management.OperatingSystemMXBean;

import java.io.File;
import java.lang.management.ManagementFactory;


@Service
@ApplicationScope
public class NodeStatus {

    public NodeLoad getLoadData() {
        NodeLoad nodeLoad = new NodeLoad();
        File workspace = WorkspaceUtils.getWorkspaceDirAsFile("");
        var sysinfo = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        nodeLoad.setTotalMemorySize(sysinfo.getTotalPhysicalMemorySize());
        nodeLoad.setFreeMemorySize(sysinfo.getFreePhysicalMemorySize());
        nodeLoad.setCpuLoad(sysinfo.getSystemCpuLoad());
        nodeLoad.setAverageCpuLoad(sysinfo.getSystemLoadAverage());   // <- nie działa pod Windowsem
        nodeLoad.setNumberOfProcessors(sysinfo.getAvailableProcessors());
        nodeLoad.setTotalSwapSpace(sysinfo.getTotalSwapSpaceSize());
        nodeLoad.setFreeSwapSpace(sysinfo.getFreeSwapSpaceSize());
        nodeLoad.setTotalDiskSpace(workspace.getTotalSpace());
        nodeLoad.setAvailableDiskSpace(workspace.getUsableSpace());
        return nodeLoad;
    }

}
