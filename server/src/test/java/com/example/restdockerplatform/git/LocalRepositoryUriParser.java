package com.example.restdockerplatform.git;

public class LocalRepositoryUriParser implements RepositoryUriParser {
    @Override
    public String createUri(String base, String taskId) {
        return base + ".git";
    }
}
