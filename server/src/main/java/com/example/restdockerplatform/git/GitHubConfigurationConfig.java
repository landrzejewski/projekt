package com.example.restdockerplatform.git;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class GitHubConfigurationConfig {

    public static final String GIT_EXTENSION = ".git";

    private static final String URI_SEPARATOR = "/";
    private final String gitHubRepositoryUrl;
    private final String gitHubUser;
    private final String gitHubToken;
    private final String workDirectory;

    GitHubConfigurationConfig(@Value("${github.url}") String gitHubUrl,
                              @Value("${github.user}") String userName,
                              @Value("${github.token}") String gitHubToken,
                              @Value("${user.file.space}") String workDirectory) {
        this.gitHubRepositoryUrl = gitHubUrl;
        this.gitHubUser = userName;
        this.gitHubToken = gitHubToken;
        this.workDirectory = workDirectory;
    }

    public String getRepositoryURI() {
        return gitHubRepositoryUrl + URI_SEPARATOR + gitHubUser + URI_SEPARATOR;
    }

    public String getRepositoryToken() {
        return gitHubToken;
    }

}
