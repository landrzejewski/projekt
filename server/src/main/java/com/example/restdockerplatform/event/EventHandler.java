package com.example.restdockerplatform.event;


import com.example.restdockerplatform.domain.UploadStatus;
import com.example.restdockerplatform.domain.UserTask;
import com.example.restdockerplatform.file.FileService;
import com.example.restdockerplatform.git.TaskRepository;
import com.example.restdockerplatform.persistence.inMemory.ProcessingRepository;
import com.example.restdockerplatform.utils.UnZipException;
import org.eclipse.jgit.errors.RepositoryNotFoundException;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


@Component
public class EventHandler {

    private final ProcessingRepository processingRepository;
    private final FileService fileService;
    private final TaskRepository taskRepository;


    public EventHandler(ProcessingRepository processingRepository, FileService fileService, TaskRepository taskRepository) {
        this.processingRepository = processingRepository;
        this.fileService = fileService;
        this.taskRepository = taskRepository;
    }


    @Async
    @EventListener
    void handleAsyncSaveUserTaskEvent(SaveUserTaskEvent event) {

        UserTask userTask = new UserTask(event.getUser(), event.getProject());

        try {
            // unzip zip file in proper place
            fileService.unzipFile(event.getUser(), event.getProject());
        } catch (UnZipException ex) {
            processingRepository.setStatus(userTask, UploadStatus.ERROR);

        }

        try {
            // commit and push changes to Git repository
            taskRepository.saveTask(event.getUser(), event.getProject());
        } catch (RepositoryNotFoundException ex) {
            processingRepository.setStatus(userTask, UploadStatus.ERROR);
        }

        // save status
        processingRepository.setStatus(userTask, UploadStatus.FINISHED);
    }

}
