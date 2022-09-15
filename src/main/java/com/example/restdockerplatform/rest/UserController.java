package com.example.restdockerplatform.rest;


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
import java.util.Collections;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/api/project")
@AllArgsConstructor
public class UserController {

    private final RestService restService;


    @GetMapping("/{user}")
    List<String> getAllTasksForUser(@PathVariable final String user) {

        // TODO return all repositories where user has own branch
        //     GET zadania użytkownika - projekty z branchem uzytkownika
        log.info(" -> getAllTasksForUser, user = {}", user);
        return Collections.emptyList();
    }

    /**
     * Downloads zipped files of user project -> branch (user) of project
     *
     * @param user
     * @param project
     * @return
     */
    @GetMapping("/{user}/{project}")
    ResponseEntity<?> getFilesForUserProject(
            @PathVariable final String user,
            @PathVariable final String project) throws IOException {
        log.info(" -> getFilesForUserProject, user = {}, project = {}", user, project);

        Resource resource = restService.getFile(user, project);

        if (resource == null) {
            return restService.prepareFileNotFoundReponse();
        }

        return restService.prepareResponseEntityWithFile(resource);
    }


    /**
     * Uploads zipped files of user project -> branch (user) of project
     *
     * @param user
     * @param project
     * @param multipartFile
     * @return
     * @throws IOException
     */
    @PutMapping("/{user}/{project}")
    ResponseEntity<FileUploadResponse> putFilesForUserProject(
            @PathVariable final String user,
            @PathVariable final String project,
            @RequestParam("file") MultipartFile multipartFile) throws IOException {
        log.info(" -> putFilesForUserProject, user = {}, project = {}", user, project);

        String fileCode = restService.saveFile(user, project, multipartFile);
        FileUploadResponse response = restService.prepareFileUploadResponse(user, project, multipartFile, fileCode);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PostMapping("/execute/{user}/{project}")
    void executeUserProject(
            @PathVariable final String user,
            @PathVariable final String project) {

        // TODO    POST zlecenie wykonania - wywołanie przetwarzania przez docker konkretnego zadania
        log.info(" -> executeUserProject, user = {}, project = {}", user, project);
    }


    @GetMapping("/status/{user}/{project}")
    String getStatusForUserProject(
            @PathVariable final String user,
            @PathVariable final String project) {

        // TODO    GET pobieranie informacji zwrotnej z servera o statusie zadań użytkownika
        log.info(" -> getStatusForUserProject, user = {}, project = {}", user, project);
        return "ok";
    }


}
