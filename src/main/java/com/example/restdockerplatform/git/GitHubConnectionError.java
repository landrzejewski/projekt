package com.example.restdockerplatform.git;

public class GitHubConnectionError extends RuntimeException  {
    GitHubConnectionError(String message) {
        super(message);
    }
}
