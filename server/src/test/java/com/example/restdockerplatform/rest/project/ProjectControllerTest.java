package com.example.restdockerplatform.rest.project;

import com.example.restdockerplatform.domain.ProcessStatus;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Slf4j
@WebMvcTest(ProjectController.class)
@ExtendWith(SpringExtension.class)
class ProjectControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ProjectController projectController;

    private static final String API_URL = "/api/project/";

    @Test
    void given_user_when_get_all_tasks_for_user_then_returns_list_of_projects() throws Exception {

        final String userName = "user1";
        final List<String> tasks = List.of("task1", "task2");

        given(projectController.getAllTasksForUser(userName)).willReturn(tasks);


        mvc.perform(
                        get(API_URL + "{user}", userName)
                                .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0]", is(tasks.get(0))))
                .andExpect(jsonPath("$[1]", is(tasks.get(1))));
    }


    @Test
    void given_incorrect_user_when_get_all_tasks_for_user_then_returns_empty_list_of_projects() throws Exception {

        final String userName = "user1";
        final String incorrectUserName = "user2";
        final List<String> tasks = List.of("task1", "task2");

        given(projectController.getAllTasksForUser(userName)).willReturn(tasks);

        mvc.perform(
                        get(API_URL + "{user}", incorrectUserName)
                                .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }


//    @Test
//    void getFilesForUserProject() {
//    }

//    @Test
//    void putFilesForUserProject() {
//    }


    @Test
    void given_user_and_project_when_get_save_status_for_user_and_project_then_return_message() throws Exception {

        final String user = "user1";
        final String project = "task1";
        final String expectedValue = ProcessStatus.READY.name();
        final ProcessStatusDTO expectedMessage = new ProcessStatusDTO(expectedValue);

        given(projectController.getSaveStatusForUserProject(user, project))
                .willReturn(ResponseEntity.ok().body(expectedMessage));

        mvc.perform(
                        get(API_URL + "saveStatus/{user}/{project}", user, project)
                                .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(expectedValue));
    }


    @Test
    void given_user_and_project_when_execute_task_for_user_and_project_then_return_message() throws Exception {

        final String user = "user1";
        final String project = "task1";
        final String expectedMessage = String.format("Cannot execute, error uploading task,\nuser = %s, project = %s", user, project);

        given(projectController.executeUserProject(user, project))
                .willReturn(ResponseEntity.badRequest().body(expectedMessage));

        mvc.perform(
                        post(API_URL + "execute/{user}/{project}", user, project)
                                .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value(expectedMessage));
    }


//    @Test
//    void getStatusForUserProject() {
//    }

//    @Test
//    void assignUserForProject() {
//    }

}