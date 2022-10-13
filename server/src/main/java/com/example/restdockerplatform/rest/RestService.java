package com.example.restdockerplatform.rest;

import com.example.restdockerplatform.domain.UploadStatus;
import com.example.restdockerplatform.domain.UserTask;
import com.example.restdockerplatform.event.SaveUserTaskEvent;
import com.example.restdockerplatform.file.FileService;
import com.example.restdockerplatform.git.TaskRepository;
import com.example.restdockerplatform.persistence.database.Task;
import com.example.restdockerplatform.persistence.database.TaskService;
import com.example.restdockerplatform.persistence.inMemory.ProcessingRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@Service
class RestService {


    private final TaskService taskService;
    private final ApplicationEventPublisher publisher;
    private final FileService fileService;
    private final TaskRepository taskRepository;
    private final ProcessingRepository processingRepository;


    RestService(TaskService taskService, ApplicationEventPublisher publisher, FileService fileService, TaskRepository taskRepository, ProcessingRepository processingRepository) {
        this.taskService = taskService;
        this.publisher = publisher;
        this.fileService = fileService;
        this.taskRepository = taskRepository;
        this.processingRepository = processingRepository;
    }


    Resource getFile(String user, String project) throws IOException {

        // TODO get files from Git
        taskRepository.getTask(user, project);

        return fileService.getFile(user, project);
    }


    String saveFile(String user, String project, MultipartFile multipartFile) throws IOException {

        // test if its a .zip file
        if (!fileService.verifyMultipartFileExtension(multipartFile)) {
            throw new IncorrectFileTypeException("Incorrect file type");
        }

        // save file
        final String fileCodeName = fileService.saveFile(user, project, multipartFile);

        // save status
        processingRepository.setStatus(new UserTask(user, project), UploadStatus.STARTED);

        // order unzipping and sending to git repository
        publisher.publishEvent(SaveUserTaskEvent.builder().user(user).project(project).build());

        return fileCodeName;
    }


    List<String> getUserTasks(String user) {

        return taskRepository.listUserTasks(user);
    }


    FileUploadResponse prepareFileUploadResponse(String user, String project, MultipartFile multipartFile, String filecode) {

        FileUploadResponse response = new FileUploadResponse();

        response.setFileName(filecode);

        long size = multipartFile.getSize();
        response.setSize(size);

        response.setDownloadUri(RestUtil.getDownloadUri(user, project));

        response.setSaveStatusUri(RestUtil.getSaveStatusUri(user, project));

        return response;
    }


    ResponseEntity<?> prepareFileNotFoundReponse() {
        return new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);
    }


    ResponseEntity<Resource> prepareResponseEntityWithFile(Resource resource) {
        String contentType = "application/octet-stream";
        String headerValue = "attachment; filename=\"" + resource.getFilename() + "\"";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                .body(resource);
    }

    ResponseEntity<String> orderExecute(String user, String project) {

        if (processingRepository.isFinished(new UserTask(user, project))) {
            // TODO order execute

            return ResponseEntity.ok().body(String.format("Ordered execution,\nuser = %s, project = %s", user, project));
        }

        return ResponseEntity.badRequest().body(String.format("Can not execute, upload not finished,\nuser = %s, project = %s", user, project));
    }

    ResponseEntity<String> getSaveStatus(String user, String project) {

        if (processingRepository.isFinished(new UserTask(user, project))) {

            return ResponseEntity.ok().body(String.format("Can be executed,\nuser = %s, project = %s", user, project));
        }

        return ResponseEntity.ok().body(String.format("Saving in progress, check again in a few minutes,\nuser = %s, project = %s", user, project));
    }


    ResponseEntity<List<Task>> getExecuteStatus(String user, String project) {
        List<Task> userTasks = taskService.findByUserNameAndProject(user, project);

        // TODO getExecutestatus from database --> TaskRepository
        return ResponseEntity.ok().body(userTasks);
    }

}
