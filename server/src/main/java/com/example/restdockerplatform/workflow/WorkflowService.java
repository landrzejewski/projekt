package com.example.restdockerplatform.workflow;

import local.wspolnyprojekt.nodeagentlib.dto.TaskStatus;

public interface WorkflowService {
    void appendLog(String id, String log);
    void updateStatus(String id, TaskStatus status);
}
