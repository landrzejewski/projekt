package com.example.restdockerplatform.git;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.RepositoryNotFoundException;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class GitHubTaskRepository implements TaskRepository {
    private final GitHubConfigurationConfig configurationConfig;
    private final CredentialsProvider credentialsProvider;
    private final ProjectContentProvider gitHubUser;

    private final RepositoryContentProvider repositoryContentProvider;
    private final RepositoryUriParser repositoryUriParser;

    @Override
    public List<String> listTasks() {
        return gitHubUser.listTasks();
    }

    @Override
    public List<String> listUserTasks(String userId) {
        return gitHubUser.listUserTasks(userId);
    }

    @Override
    public void getTask(String userId, String taskId) throws RepositoryNotFoundException {
        var uri = repositoryUriParser.createUri(configurationConfig.getRepositoryURI(), taskId);
        var path = Paths.get(configurationConfig.getWorkDirectory(), userId, taskId);

        if (!listUserTasks(userId).contains(taskId)) {
            log.info("Task {} not assigned to user {}", taskId, userId);
            return;
        }

        if (!repositoryContentProvider.repositoryExists(path)) {
            repositoryContentProvider.cloneRepository(uri, userId, path);
        }
        if (!repositoryContentProvider.repositoryOnBranch(userId, path)) {
            repositoryContentProvider.checkoutBranch(path, userId);
        } else {
            repositoryContentProvider.pullChanges(path);
        }
    }

    @Override
    public void assignTaskToUser(String userId, String taskId) throws RepositoryNotFoundException {
        var uri = repositoryUriParser.createUri(configurationConfig.getRepositoryURI(), taskId);
        var path = Paths.get(configurationConfig.getWorkDirectory(), userId, taskId);

        if (listUserTasks(userId).contains(taskId)) {
            log.info("Task {} already assigned to user {}", taskId, userId);
            return;
        }

        if (!repositoryContentProvider.repositoryExists(path)) {
            Git git = repositoryContentProvider.cloneRepository(uri, null, path);
            repositoryContentProvider.checkoutBranch(path, userId);
            try {
                git.push().setCredentialsProvider(credentialsProvider).call();
            } catch (GitAPIException ex) {
                log.error("Failed to push. Reason: {}", ex.toString());
            }
        }
    }

    @Override
    public void saveTask(String userId, String taskId) throws RepositoryNotFoundException {
        var path = Paths.get(configurationConfig.getWorkDirectory(), userId, taskId);

        if (repositoryContentProvider.addModifiedFiles(path) == 0) {
            log.info("No files modified in repo {}", path);
            return;
        }
        repositoryContentProvider.commit(path);
        repositoryContentProvider.push(path);
    }
}
