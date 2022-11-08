package com.example.restdockerplatform.persistence.database;

import local.wspolnyprojekt.nodeagentlib.dto.TaskStatus;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static local.wspolnyprojekt.nodeagentlib.dto.TaskStatus.TASK_STATUS_DONE;
import static local.wspolnyprojekt.nodeagentlib.dto.TaskStatus.TASK_STATUS_FAIL;
import static local.wspolnyprojekt.nodeagentlib.dto.TaskStatus.TASK_STATUS_RUNNING;
import static local.wspolnyprojekt.nodeagentlib.dto.TaskStatus.TASK_STATUS_UNKNOWN;

@RequiredArgsConstructor
@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Override
    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }


    @Override
    public List<Task> fetchTaskList() {
        return taskRepository.findAll();
    }

    @Override
    public List<Task> findByNodeUUId(String nodeUUId) {
        return taskRepository.findByNodeUUId(nodeUUId);
    }



    @Override
    public Optional<Task> findById(String id) {
        return taskRepository
                .findById(id);
    }


    @Override
    public Task updateTaskStatus(TaskStatus taskStatus, String taskId) {
        Task taskToUpdate =
                taskRepository
                        .findById(taskId)
                        .get();
        if (Objects.nonNull(taskStatus))
            taskToUpdate.setStatus(taskStatus);
        return taskRepository.save(taskToUpdate);
    }


    @Override
    public Task updateTaskTextResult(String textResult, String taskId) {
        Task taskToUpdate =
                taskRepository
                        .findById(taskId)
                        .get();
        if (Objects.nonNull(textResult))
            taskToUpdate.setTextResult(textResult);
        return taskRepository.save(taskToUpdate);
    }


    @Override
    public Task updateTaskBytesResult(byte[] bytesResult, String taskId) {
        Task taskToUpdate =
                taskRepository
                        .findById(taskId)
                        .get();
        if (Objects.nonNull(bytesResult))
            taskToUpdate.setBytesResult(bytesResult);
        return taskRepository.save(taskToUpdate);
    }


    @Override
    public void deleteTaskById(String taskId) {
        taskRepository.deleteById(taskId);
    }

    @Override
    public List<Task> findByUserNameAndProject(String username, String project) {
        return taskRepository.findByUsernameAndProject(username, project);
    }


    public void updateStatusesOfNotFinishedTasks(String nodeUUId) {
        final List<Task> tasksToVerifyAndChangeStatus = findByNodeUUId(nodeUUId);

        tasksToVerifyAndChangeStatus.stream()
                .filter(this::filerTaskByStatus)
                .forEach(task -> updateTaskStatus(
                        TASK_STATUS_FAIL, task.getId()));
    }

    private boolean filerTaskByStatus(Task task) {

        // TODO change statuses to change

        return !task.getStatus().equals(TASK_STATUS_DONE)
                && (task.getStatus().equals(TASK_STATUS_RUNNING) || task.getStatus().equals(TASK_STATUS_UNKNOWN));
    }

}
