package com.example.restdockerplatform.git;

import org.springframework.stereotype.Component;

@Component
public class GitHubRepositoryUriParser implements RepositoryUriParser {

    @Override
    public String createUri(String base, String taskId) {
        return base + taskId + ".git";
    }
}
