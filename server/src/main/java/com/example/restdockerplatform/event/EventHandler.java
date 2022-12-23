package com.example.restdockerplatform.event;


import com.example.restdockerplatform.domain.UploadStatus;
import com.example.restdockerplatform.domain.UserTask;
import com.example.restdockerplatform.file.FileService;
import com.example.restdockerplatform.git.TaskRepository;
import com.example.restdockerplatform.persistence.inMemory.processing.ProcessRepository;
import com.example.restdockerplatform.utils.UnZipException;
import org.eclipse.jgit.errors.RepositoryNotFoundException;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


@Component
public class EventHandler {

    private final ProcessRepository processRepository;
    private final FileService fileService;
    private final TaskRepository taskRepository;


    public EventHandler(ProcessRepository processRepository, FileService fileService, TaskRepository taskRepository) {
        this.processRepository = processRepository;
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
            // todo warto się zastanowić nad określeniem przyczyny błędu (może dodać jakiś komunikat)
            processRepository.setStatus(userTask, UploadStatus.ERROR);

        }

        try {
            // commit and push changes to Git repository
            taskRepository.saveTask(event.getUser(), event.getProject());
        } catch (RepositoryNotFoundException ex) {
            processRepository.setStatus(userTask, UploadStatus.ERROR);
        }

        // save status
        processRepository.setStatus(userTask, UploadStatus.FINISHED);
    }

}
