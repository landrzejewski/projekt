package com.example.restdockerplatform.workflow;

import local.wspolnyprojekt.nodeagentlib.dto.TaskLogMessage;
import local.wspolnyprojekt.nodeagentlib.dto.TaskStatusMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WorkflowController {

    private final WorkflowService workflowService;

    public WorkflowController(WorkflowService workflowService) {
        this.workflowService = workflowService;
    }


    @PostMapping("/taskstatus/{id}")
    public ResponseEntity<String> updateTaskStatus(@RequestBody TaskStatusMessage taskStatusMessage,
                                                   @PathVariable("id") String taskId) {
        try {
            workflowService.updateStatus(taskId, taskStatusMessage.getTaskStatus());
        } catch (TaskNotFoundException ex) {
            return ResponseEntity.status(500).body(ex.getMessage());
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/tasklog/{id}")
    public ResponseEntity<String> appendLog(@RequestBody TaskLogMessage taskLogMessage,
                                            @PathVariable("id") String taskId) {
        try {
            workflowService.appendLog(taskId, taskLogMessage.getPayload());
        } catch (TaskNotFoundException ex) {
            return ResponseEntity.status(500).body(ex.getMessage());
        }

        return ResponseEntity.ok().build();
    }
}
