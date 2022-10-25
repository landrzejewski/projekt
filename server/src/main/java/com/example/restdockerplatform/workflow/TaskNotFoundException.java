package com.example.restdockerplatform.workflow;

public class TaskNotFoundException extends RuntimeException {
    TaskNotFoundException(String message) {
        super(message);
    }
}
