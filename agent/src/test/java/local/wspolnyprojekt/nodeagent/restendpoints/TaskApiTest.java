package local.wspolnyprojekt.nodeagent.restendpoints;

import local.wspolnyprojekt.nodeagent.task.TasksService;
import local.wspolnyprojekt.nodeagentlib.dto.TaskCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static local.wspolnyprojekt.nodeagentlib.dto.RestEndpoints.TASKID_PATH_VARIABLE;
import static local.wspolnyprojekt.nodeagentlib.dto.RestEndpoints.TASK_ENDPOINT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TaskApi.class)
class TaskApiTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    TasksService tasksService;

    @Captor
    ArgumentCaptor<TaskCommand> commandCaptor;

    @Captor
    ArgumentCaptor<String> taskIdCaptor;

    @Test
    void shouldCallTaskServiceOnPostedCommand() throws Exception {
        String taskId = UUID.randomUUID().toString();
        String taskEndpoint = TASK_ENDPOINT.replace("{" + TASKID_PATH_VARIABLE + "}", taskId);

        when(tasksService.executeCommand(anyString(), eq(TaskCommand.TASK_COMMAND_START)))
                .thenReturn(new ResponseEntity(HttpStatus.OK));

        // Wszystkie komendy w enumie w pętli
        for (TaskCommand taskCommand : TaskCommand.values()) {
            clearInvocations(tasksService); // Resetowanie licznika wywołań, bo domyślnie jest times(1) i potem przy verify() się wykłada
            mvc.perform(post(taskEndpoint)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(taskCommand.getJsonString()))
                    .andExpect(status().isOk());

            verify(tasksService).executeCommand(taskIdCaptor.capture(), commandCaptor.capture());
            assertThat(commandCaptor.getValue()).isEqualTo(taskCommand);
            assertThat(taskIdCaptor.getValue()).isEqualTo(taskId);
        }

    }

}
