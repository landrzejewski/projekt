package com.example.restdockerplatform.persistence.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TaskController {


    @Autowired
    private TaskService taskService;


    // Save operation
    @PostMapping("/tasks")
    public Task saveTask(@RequestBody Task task) {
        return taskService.saveTask(task);
    }


    // Read all operation
    @GetMapping("/tasks")
    public List<Task> fetchTaskList() {
        return taskService.fetchTaskList();
    }


    //Read one by Id
    @GetMapping("/tasks/{id}")
    public Task findById(String id) {
        return taskService.findById(id);
    }


    // Update task status operation
    @PutMapping("/tasks/{id}/status")
    public Task updateTaskStatus(@RequestBody TaskStatus taskStatus,
                                 @PathVariable("id") String taskId) {
        return taskService.updateTaskStatus(
                taskStatus, taskId);
    }


    // Update task text result operation
    @PutMapping("/tasks/{id}/result")
    public Task updateTaskTextResult(@RequestBody String textResult,
                                     @PathVariable("id") String taskId) {
        return taskService.updateTaskTextResult(
                textResult, taskId);
    }


    // Update task bytes result operation
    @PutMapping("/tasks/{id}/byteresult")
    public Task updateTaskBytesResult(@RequestBody byte[] bytesResult,
                                      @PathVariable("id") String taskId) {
        return taskService.updateTaskBytesResult(
                bytesResult, taskId);
    }


    // Delete operation
    @DeleteMapping("/tasks/{id}")
    public String deleteTaskById(@PathVariable("id")
                                 String taskId) {
        taskService.deleteTaskById(taskId);
        return "Task deleted Successfully";
    }


}
