package com.example.restdockerplatform.git;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.RepositoryNotFoundException;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.kohsuke.github.GHRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Component
public class GitHubTaskRepository implements TaskRepository {
    private final GitHubConfigurationConfig configurationConfig;
    private final CredentialsProvider credentialsProvider;
    private final GitHubUser gitHubUser;

    private final String workDirectory;

    public GitHubTaskRepository(
            GitHubConfigurationConfig configurationConfig,
            CredentialsProvider credentialsProvider,
            GitHubUser gitHubUser,
            @Value("${user.file.space}") String workDirirectory) {
        this.configurationConfig = configurationConfig;
        this.credentialsProvider = credentialsProvider;
        this.gitHubUser = gitHubUser;
        this.workDirectory = workDirirectory;
    }

    @Override
    public List<String> listTasks() {
        log.info("Listing all tasks available");
        var repositories = gitHubUser.getGitHubUser().listRepositories();
        try {
            return repositories
                    .toList()
                    .stream()
                    .map(GHRepository::getName)
                    .collect(Collectors.toList());
        } catch (IOException ex) {
            log.error("Could not get repositories for user: {}. Reason: {}", configurationConfig.getGitHubUser(), ex.toString());
            return new ArrayList<>();
        }
    }

    @Override
    public List<String> listUserTasks(String userId) {

        log.info("Listing all tasks available for user {}", userId);

        List<String> branches = new ArrayList<>();

        var repositories = gitHubUser.getGitHubUser().listRepositories().iterator();
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

    @Override
    public void getTask(String userId, String taskId, String workDir) {

        var uri = configurationConfig.getRepositoryURI() + taskId + ".git";
        var path = Paths.get(workDir, userId, taskId);

        log.info("Cloning branch {} from repo {} to {}", taskId, uri, workDir);

        try {
            Git.cloneRepository()
                    .setCredentialsProvider(credentialsProvider)
                    .setURI(uri)
                    .setDirectory(path.toFile())
                    .setBranch(userId)
                    .call();
        } catch (GitAPIException ex) {
            log.error("Could not checkout branch {} from repository {} to location {}. Reason: {}",
                    userId, uri, workDir, ex.toString());
        }

    }


    @Override
    public void getTask(String userId, String taskId) {
        getTask(userId, taskId, workDirectory);
    }

    @Override
    public void createTask(String taskName, String dir) {

    }

    @Override
    public void assignTaskToUser(String userId, String taskId, String workDir) {
        var uri = configurationConfig.getRepositoryURI() + taskId + ".git";
        var path = Paths.get(workDir, userId, taskId);

        Git git;
        try {
            git = Git.cloneRepository()
                    .setCredentialsProvider(credentialsProvider)
                    .setURI(uri)
                    .setDirectory(path.toFile())
                    .call();
        } catch (GitAPIException ex) {
            log.error("Could not clone repository {} to location {}. Reason: {}", uri, workDir, ex.toString());
            return;
        }

        try {
            git.checkout()
                    .setCreateBranch(true)
                    .setName(userId)
                    .call();
        } catch (GitAPIException ex) {
            log.error("Could not checkout branch {} in repository {} in location {}. Reason: {}",
                    userId, uri, workDir, ex.toString());
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

    public void saveTask(String userId, String taskId) throws RepositoryNotFoundException {
        saveTask(userId, taskId, workDirectory);
    }

}
