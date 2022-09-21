package com.example.restdockerplatform.git;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class GitHubConfigurationConfig {

    static final String URI_SEPARATOR = "/";
    private final String gitHubRepositoryUrl;
    private final String gitHubUser;
    private final String gitHubToken;


    public GitHubConfigurationConfig(@Value("${github.url}") String gitHubUrl,
                                     @Value("${github.user}") String userName,
                                     @Value("${github.token}") String gitHubToken) {
        this.gitHubRepositoryUrl = gitHubUrl;
        this.gitHubUser = userName;
        this.gitHubToken = gitHubToken;
    }

    public String getRepositoryURI() {
        return gitHubRepositoryUrl + URI_SEPARATOR + gitHubUser + URI_SEPARATOR;
    }
}
