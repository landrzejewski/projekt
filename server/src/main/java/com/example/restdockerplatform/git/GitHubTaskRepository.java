package com.example.restdockerplatform.git;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.kohsuke.github.GHRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    public void getTask(String userId, String taskId, String workDir) {
        var uri = repositoryUriParser.createUri(configurationConfig.getRepositoryURI(), taskId);
        var path = Paths.get(workDir, userId, taskId);

        if (!listUserTasks(userId).contains(taskId)) {
            log.info("Task {} not assigned to user {}", taskId, userId);
            return;
        }

        if (!repositoryContentProvider.repositoryExists(path)) {
            repositoryContentProvider.cloneRepository(uri, null, path);
        }
        if (!repositoryContentProvider.repositoryOnBranch(userId, path)) {
            repositoryContentProvider.checkoutBranch(path, userId);
        } else {
            repositoryContentProvider.pullChanges(path);
        }
    }

    @Override
    public void getTask(String userId, String taskId) {
        getTask(userId, taskId, configurationConfig.getWorkDirectory());
    }

    @Override
    public void createTask(String taskName, String dir) {
        // TO BE REMOVED
    }

    @Override
    public void assignTaskToUser(String userId, String taskId, String workDir) {
        var uri = repositoryUriParser.createUri(configurationConfig.getRepositoryURI(), taskId);
        var path = Paths.get(workDir, userId, taskId);

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
    public void saveTask(String userId, String taskId, String workDir) throws RepositoryNotFoundException {
        var path = Paths.get(workDir, userId, taskId);

        Git git;
        try {
            git = Git.open(path.toFile());
        } catch (IOException ex) {
            log.error("Failed to load repository in: {}. Reason: {}", path, ex.toString());
            return;
        }

        try {
            Status status = git.status().call();
            AddCommand addCommand = git.add();

            var modifiedFiles = status.getModified();
            if (modifiedFiles.isEmpty()) {
                log.info("No files modified in: {}", path);
                return;
            }

            status.getModified().forEach(addCommand::addFilepattern);

            addCommand.call();
            log.info(status.getModified().toString());
        } catch (GitAPIException ex) {
            log.error("Failed to add modified files in repository: {}. Reason: {}", path, ex.toString());
            return;
        }

        try {
            git.commit().setMessage(UUID.randomUUID().toString()).call();
        } catch (GitAPIException ex) {
            log.error("Could not commit files in repository: {}. Reason: {}", path, ex.toString());
        }

        try {
            git.push().setCredentialsProvider(credentialsProvider).call();
        } catch (GitAPIException ex) {
            log.error("Failed to push. Reason: {}", ex.toString());
        }
    }


    public void saveTask(String userId, String taskId) {
        saveTask(userId, taskId, configurationConfig.getWorkDirectory());
    }
}
