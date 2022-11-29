package com.example.restdockerplatform.workflow;

import local.wspolnyprojekt.nodeagentlib.dto.TaskLogMessage;
import local.wspolnyprojekt.nodeagentlib.dto.TaskStatusMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class WorkflowController {

    private final WorkflowService workflowService;

    public WorkflowController(WorkflowService workflowService) {
        this.workflowService = workflowService;
    }


    @PutMapping("/taskstatus/{id}")
    public ResponseEntity<String> updateTaskStatus(@RequestBody TaskStatusMessage taskStatusMessage,
                                                   @PathVariable("id") String taskId) {
        try {
            workflowService.updateStatus(taskId, taskStatusMessage.getTaskStatus());
        } catch (TaskNotFoundException ex) {
            return ResponseEntity.status(500).body(ex.getMessage());
        }

        return ResponseEntity.ok().build();
    }

    @PutMapping("/tasklog/{id}")
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
