package com.example.restdockerplatform.rest.project;


import com.example.restdockerplatform.persistence.database.Task;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/api/project")
@AllArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    /**
     * Returns all project associated with user
     * branch (user) of project
     *
     * @param user user
     * @return list of projects
     */
    @GetMapping("/{user}")
    List<String> getAllTasksForUser(@PathVariable final String user) {

        log.info(" -> getAllTasksForUser, user = {}", user);
        var userTasks = projectService.getUserTasks(user);
        log.info("Tasks available for {}: {}", user, userTasks);

        return userTasks;
    }

    /**
     * Downloads zipped files of user project -> branch (user) of project
     *
     * @param user    user
     * @param project project
     * @return zip file of user branch of project
     */
    @GetMapping("/{user}/{project}")
    ResponseEntity<?> getFilesForUserProject(
            @PathVariable final String user,
            @PathVariable final String project) throws IOException {
        log.info(" -> getFilesForUserProject, user = {}, project = {}", user, project);

        Resource resource = projectService.getFile(user, project);

        if (resource == null) {
            return projectService.prepareFileNotFoundReponse();
        }

        return projectService.prepareResponseEntityWithFile(resource);
    }


    /**
     * Uploads zipped files of user project -> branch (user) of project
     *
     * @param user          user
     * @param project       project
     * @param multipartFile zip file
     * @return
     * @throws IOException
     */
    @PutMapping("/{user}/{project}")
    ResponseEntity<FileUploadResponse> putFilesForUserProject(
            @PathVariable final String user,
            @PathVariable final String project,
            @RequestParam("file") MultipartFile multipartFile) throws IOException {
        log.info(" -> putFilesForUserProject, user = {}, project = {}", user, project);

        String fileCode = projectService.saveFile(user, project, multipartFile);


        FileUploadResponse response = projectService.prepareFileUploadResponse(user, project, multipartFile, fileCode);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    /**
     * Gets save status executed asynchronicly
     *
     * @param user    user
     * @param project project
     * @return status of user task uploading
     */
    @GetMapping("/saveStatus/{user}/{project}")
    ResponseEntity<ProcessStatusDTO> getSaveStatusForUserProject(
            @PathVariable final String user,
            @PathVariable final String project) {

        log.info(" -> getSaveStatusForUserProject, user = {}, project = {}", user, project);
        return projectService.getSaveStatus(user, project);
    }


    /**
     * Orders execute task
     *
     * @param user    user
     * @param project project
     * @return
     */
    @PostMapping("/execute/{user}/{project}")
    ResponseEntity<String> executeUserProject(
            @PathVariable final String user,
            @PathVariable final String project) {

        log.info(" -> executeUserProject, user = {}, project = {}", user, project);
        return projectService.orderExecute(user, project);
    }


    /**
     * Gets execute status
     *
     * @param user    user
     * @param project project
     * @return list of tasks with statuses
     */
    @GetMapping("/status/{user}/{project}")
    ResponseEntity<List<Task>> getStatusForUserProject(
            @PathVariable final String user,
            @PathVariable final String project) {

        log.info(" -> getStatusForUserProject, user = {}, project = {}", user, project);
        return projectService.getExecuteStatus(user, project);
    }


    /**
     * Assigns user to project
     *
     * @param user    user
     * @param project project
     * @return
     */
    @PostMapping("/{user}/{project}")
    ResponseEntity<?> assignUserForProject(
            @PathVariable final String user,
            @PathVariable final String project) {
        log.info(" -> assignUserForProject, user = {}, project = {}", user, project);

        return projectService.asssignUser(user, project);
    }

}
