package com.example.restdockerplatform.persistence.database;

import local.wspolnyprojekt.nodeagentlib.dto.TaskStatus;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
        return (List<Task>) taskRepository.findAll();
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

}
