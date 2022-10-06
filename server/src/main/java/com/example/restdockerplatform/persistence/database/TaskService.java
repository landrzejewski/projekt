package com.example.restdockerplatform.persistence.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface TaskService {

    // Save operation
    Task saveTask(Task task);

    // Read all
    List<Task> fetchTaskList();

    //Read one by Id
    Optional<Task> findById(String id);

    // Update task status operation
    Task updateTaskStatus(TaskStatus taskStatus, String taskId);

    // Update task text result operation
    Task updateTaskTextResult(String textResult, String taskId);

    // Update task bytes result operation
    Task updateTaskBytesResult(byte[] bytesResult, String taskId);

    // Delete operation
    void deleteTaskById(String taskId);

    List<Task> findByUserNameAndProject(String username, String project);
}
