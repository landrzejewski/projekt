package com.example.restdockerplatform.rest.project;

import com.example.restdockerplatform.domain.ProcessStatus;
import com.example.restdockerplatform.domain.UploadStatus;
import com.example.restdockerplatform.domain.UserTask;
import com.example.restdockerplatform.event.SaveUserTaskEvent;
import com.example.restdockerplatform.file.FileService;
import com.example.restdockerplatform.git.TaskRepository;
import com.example.restdockerplatform.persistence.database.Task;
import com.example.restdockerplatform.persistence.database.TaskService;
import com.example.restdockerplatform.persistence.inMemory.processing.ProcessRepository;
import com.example.restdockerplatform.rest.node.NodeService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.errors.RepositoryNotFoundException;
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
@Slf4j
class ProjectService {


    private final TaskService taskService;
    private final ApplicationEventPublisher publisher;
    private final FileService fileService;
    private final TaskRepository taskRepository;
    private final ProcessRepository processingRepository;

    private final NodeService nodeService;


    ProjectService(TaskService taskService,
                   ApplicationEventPublisher publisher,
                   FileService fileService,
                   TaskRepository taskRepository,
                   ProcessRepository processRepository,
                   NodeService nodeService) {
        this.taskService = taskService;
        this.publisher = publisher;
        this.fileService = fileService;
        this.taskRepository = taskRepository;
        this.processingRepository = processRepository;
        this.nodeService = nodeService;
    }


    Resource getFile(String user, String project) throws IOException {

        // get files from Git
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

        if (multipartFile != null) {

            response.setFileName(filecode);

            long size = multipartFile.getSize();
            response.setSize(size);
        }

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


        final ProcessStatus processStatus = processingRepository.getStatus(new UserTask(user, project));

        switch (processStatus) {
            case READY -> {
                final String taskId = nodeService.orderExecute(user, project);

                return ResponseEntity.ok().body(String.format("Ordered execution,\nuser = %s, project = %s, id = %s", user, project, taskId));
            }
            case ERROR -> {
                return ResponseEntity.badRequest().body(String.format("Cannot execute, error uploading task,\nuser = %s, project = %s", user, project));
            }
            default -> { // NOT_READY
                return ResponseEntity.badRequest().body(String.format("Cannot execute, upload not finished,\nuser = %s, project = %s", user, project));
            }
        }

    }

    ResponseEntity<ProcessStatusDTO> getSaveStatus(String user, String project) {

        final ProcessStatus processStatus = processingRepository.getStatus(new UserTask(user, project));

        return ResponseEntity
                .ok()
                .body(new ProcessStatusDTO(processStatus.name()));
    }


    ResponseEntity<List<Task>> getExecuteStatus(String user, String project) {

        List<Task> userTasks = taskService.findByUserNameAndProject(user, project);

        return ResponseEntity.ok().body(userTasks);
    }

    ResponseEntity<?> asssignUser(String user, String project) {

        try {
            taskRepository.assignTaskToUser(project, user);

        } catch (RepositoryNotFoundException e) {

            log.error("ERROR, cannot assign user to project, project {} not found ", project, e);

            return ResponseEntity.badRequest().body(String.format("Cannot assign user to project,\nuser = %s, project = %s", user, project));
        }

        return ResponseEntity.ok().body(RestUtil.getDownloadUri(user, project));
    }

}
