package com.example.restdockerplatform.git;

import lombok.extern.slf4j.Slf4j;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class GitHubUser implements ProjectContentProvider {

    private final GHUser gitHubUser;

    GitHubUser(GitHubConfigurationConfig configurationConfig) {
        try {
            final GitHub gitHub = GitHub.connectUsingOAuth(configurationConfig.getGitHubToken());
            gitHubUser = gitHub.getUser(configurationConfig.getGitHubUser());
        } catch (IOException ex) {
            log.info("Error when connecting to repository");
            throw new GitHubConnectionError(String.format("Could not connect to repository %s. Reason: %s",
                    configurationConfig.getGitHubUser(), ex.getMessage()));
        }
    }

    @Override
    public List<String> listTasks() {
        log.info("Listing all tasks available");
        var repositories = gitHubUser.listRepositories();
        try {
            return repositories
                    .toList()
                    .stream()
                    .map(GHRepository::getName)
                    .collect(Collectors.toList());
        } catch (IOException ex) {
            log.error("Could not list tasks available in repository: {}", ex.toString());
            return new ArrayList<>();
        }
    }

    @Override
    public List<String> listUserTasks(String userId) {
        log.info("Listing all tasks available for user {}", userId);

        List<String> branches = new ArrayList<>();

        var repositories = gitHubUser.listRepositories().iterator();
        while (repositories.hasNext()) {
            try {
                var repository = repositories.next();
                if (repository.getBranches().containsKey(userId)) {
                    branches.add(repository.getName());
                }
            } catch (IOException ex) {
                log.error(String.format("Error while listing task for user: %s", ex.getMessage()));
            }
        }

        return branches;
    }
}
