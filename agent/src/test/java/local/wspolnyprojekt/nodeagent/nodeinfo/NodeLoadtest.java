package local.wspolnyprojekt.nodeagent.nodeinfo;

import local.wspolnyprojekt.nodeagent.task.TasksService;
import local.wspolnyprojekt.nodeagent.workspaceutils.WorkspaceUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class NodeLoadtest {
    private WorkspaceUtils workspaceUtils;
    private TasksService tasksService;
    private NodeLoad nodeLoad;

    @BeforeEach
    void init() {
        workspaceUtils = Mockito.mock(WorkspaceUtils.class);
        tasksService = Mockito.mock(TasksService.class);
        Mockito.when(workspaceUtils.getWorkspaceDirAsFile()).thenReturn(new File("."));
        Mockito.when(tasksService.getNumberOfTasks()).thenReturn(10);
        Mockito.when(tasksService.getNumberOfRunningTasks()).thenReturn(5);
        nodeLoad = new NodeLoad(workspaceUtils,tasksService);
    }

    @Test
    void shouldReturnNodeLoadDtoObject() {
        var loadDetails = nodeLoad.getLoadData();
        assertThat(loadDetails).isInstanceOf(local.wspolnyprojekt.nodeagentlib.dto.NodeLoad.class);
        assertThat(loadDetails).isNotNull();
    }
}
