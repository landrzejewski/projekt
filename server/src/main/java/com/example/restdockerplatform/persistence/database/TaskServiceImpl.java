package com.example.restdockerplatform.persistence.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;


@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;


    @Override
    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }


    @Override
    public List<Task> fetchTaskList() {
        return (List<Task>) taskRepository.findAll();
    }


    @Override
    public Task findById(String id) {
        return taskRepository
                .findById(id)
                .get();
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


}
