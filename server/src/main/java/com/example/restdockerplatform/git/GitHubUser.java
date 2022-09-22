package com.example.restdockerplatform.git;

import lombok.extern.slf4j.Slf4j;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class GitHubUser {
    private final GitHub gitHub;
    private final GHUser gitHubUser;

    GitHubUser(GitHubConfigurationConfig configurationConfig) {
        try {
            gitHub = GitHub.connectUsingOAuth(configurationConfig.getGitHubToken());
            gitHubUser = gitHub.getUser(configurationConfig.getGitHubUser());
        } catch (IOException ex) {
            log.info("Error when connecting to repository");
            throw new GitHubConnectionError(String.format("Could not connect to repository %s. Reason: %s",
                    configurationConfig.getGitHubUser(), ex.getMessage()));
        }
    }

    GHUser getGitHubUser() {
        return gitHubUser;
    }
}
