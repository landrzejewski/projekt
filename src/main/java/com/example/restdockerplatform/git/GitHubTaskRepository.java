package com.example.restdockerplatform.git;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;
import org.springframework.beans.factory.annotation.Autowired;
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


    static final String GIT_HUB_REPOSITORY_URL = "https://github.com/";
    static final String GIT_HUB_USER = "java-dev-pro-project";
    static final String GH_TOKEN = "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";
    static final String URI_SEPARATOR = "/";

    static final UsernamePasswordCredentialsProvider CREDENTIALS_PROVIDER
            = new UsernamePasswordCredentialsProvider(GH_TOKEN, "");

    GitHub gitHub;
    GHUser user;

    @Autowired
    public GitHubTaskRepository() {
        try {
            gitHub = GitHub.connectUsingOAuth(GH_TOKEN);
            user = gitHub.getUser(GIT_HUB_USER);
        } catch (IOException ex) {
            log.info(String.format("Error: %s", ex.getMessage()));
        }
    }

    @Override
    public List<String> listTasks() {
        log.info("Listing all tasks available");
        var repositories = user.listRepositories();
        try {
            return repositories
                    .toList()
                    .stream()
                    .map(GHRepository::getName)
                    .collect(Collectors.toList());
        } catch (IOException ex) {
            log.error("Could not get repositories for user: {}. Reason: {}", GIT_HUB_USER, ex.toString());
            return new ArrayList<>();
        }
    }

    @Override
    public List<String> listUserTasks(String userId) {

        log.info("Listing all tasks available for user {}", userId);

        List<String> branches = new ArrayList<>();

        var repositories = user.listRepositories().iterator();
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
        var uri = GIT_HUB_REPOSITORY_URL + URI_SEPARATOR + GIT_HUB_USER + URI_SEPARATOR + taskId + ".git";
        var path = Paths.get(workDir, userId, taskId);

        log.info("Cloning branch {} from repo {} to {}", taskId, uri, workDir);

        try {
            Git.cloneRepository()
                    .setCredentialsProvider(CREDENTIALS_PROVIDER)
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
    public void createTask(String taskName, String dir) {

    }

    @Override
    public void assignTaskToUser(String userId, String taskId, String workDir) {
        var uri = GIT_HUB_REPOSITORY_URL + URI_SEPARATOR + GIT_HUB_USER + URI_SEPARATOR + taskId + ".git";
        var path = Paths.get(workDir, userId, taskId);

        Git git;
        try {
            git = Git.cloneRepository()
                    .setCredentialsProvider(CREDENTIALS_PROVIDER)
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
    public void saveTask(String userId, String taskId, String workDir) {
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
            git.push().setCredentialsProvider(CREDENTIALS_PROVIDER).call();
        } catch (GitAPIException ex) {
            log.error("Failed to push. Reason: {}", ex.toString());
        }
    }
}
