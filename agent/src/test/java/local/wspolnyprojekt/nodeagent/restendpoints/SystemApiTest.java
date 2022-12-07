package local.wspolnyprojekt.nodeagent.restendpoints;

import local.wspolnyprojekt.nodeagent.nodeinfo.NodeLoad;
import local.wspolnyprojekt.nodeagent.task.TasksService;
import local.wspolnyprojekt.nodeagent.workspaceutils.WorkspaceUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

class SystemApiTest {


    private SystemApi systemApi;

    @BeforeEach
    void init() {
        var workspaceUtils = Mockito.mock(WorkspaceUtils.class);
        var tasksService = Mockito.mock(TasksService.class);
        when(workspaceUtils.getWorkspaceDirAsFile()).thenReturn(new File(""));
        when(tasksService.getNumberOfTasks()).thenReturn(5);
        when(tasksService.getNumberOfRunningTasks()).thenReturn(3);
        var nodeLoad = new NodeLoad(workspaceUtils, tasksService);
        systemApi = new SystemApi(nodeLoad);
    }

    @Test
    void shouldReturnStringOnPingEndpoint() {
        assertThat(systemApi.pingResponse()).isEqualTo("PING");
    }

    @Test
    void shouldReturnNodeLoadDtoObjectOnLoadEndpoint() {
        assertThat(systemApi.getNodeLoad())
                .isNotNull()
                .isInstanceOf(local.wspolnyprojekt.nodeagentlib.dto.NodeLoad.class);
    }
}
