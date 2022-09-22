package com.example.restdockerplatform.git;

import lombok.Getter;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.stereotype.Component;

@Getter
@Component
public class GitHubCredentialsProvider extends UsernamePasswordCredentialsProvider {
    GitHubCredentialsProvider(GitHubConfigurationConfig configurationConfig) {
        super(configurationConfig.getGitHubToken(), "");
    }
}
