package com.example.restdockerplatform.workflow;

import com.example.restdockerplatform.persistence.database.Task;
import com.example.restdockerplatform.persistence.database.TaskService;
import local.wspolnyprojekt.nodeagentlib.dto.TaskStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkflowServiceImpl implements WorkflowService {

    @Autowired
    TaskService taskService;

    @Override
    public void appendLog(String id, String logLine) {
        var taskOptional =  taskService.findById(id);
        if (!taskOptional.isPresent()) {
            throw new TaskNotFoundException(String.format("appendLog failed: could not find task with id: %s", id));
        }
        var task = taskOptional.get();
        task.setTextResult(createNewLogLine(task, logLine));
        taskService.saveTask(task);
    }

    @Override
    public void updateStatus(String id, TaskStatus status) {
        var taskOptional = taskService.findById(id);
        if (!taskOptional.isPresent()) {
            throw new TaskNotFoundException(String.format("updateStatus failed: could not find task with id: %s", id));
        }
        var task = taskOptional.get();
        task.setStatus(status);
        taskService.saveTask(task);
    }

    private String createNewLogLine(Task task, String log) {
        return task.getTextResult() + "," + log;
    }
}
