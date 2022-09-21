package com.example.restdockerplatform.git;

public class GitHubConnectionError extends RuntimeException  {
    public GitHubConnectionError(String message) {
        super(message);
    }
}
