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
    private final GitHubUser gitHubUser;

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

        if (!listUserTasks(userId).contains(taskId)) {
            log.info("Task {} not assigned to user {}", taskId, userId);
            return;
        }

        if (!repositoryExists(path)) {
            cloneRepository(uri, null, path);
        }
        if (!repositoryOnBranch(userId, path)) {
            checkoutBranch(path, userId);
        } else {
            pullChanges(path);
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
        var uri = configurationConfig.getRepositoryURI() + taskId + ".git";
        var path = Paths.get(workDir, userId, taskId);

        if (listUserTasks(userId).contains(taskId)) {
            log.info("Task {} already assigned to user {}", taskId, userId);
            return;
        }

        if (!repositoryExists(path)) {
            Git git = cloneRepository(uri, null, path);
            checkoutBranch(path, userId);
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

    private void pullChanges(Path path) {
        try {
            Git git = Git.open(path.toFile());
            git.pull().call();
        } catch (IOException | GitAPIException ex) {
            log.error("Could not pull changes to repository {}. Reason: {}", path, ex.toString());
        }
    }

    private Git cloneRepository(String uri, String userId, Path path) {
        try {
            CloneCommand command = Git.cloneRepository()
                    .setCredentialsProvider(credentialsProvider)
                    .setURI(uri)
                    .setDirectory(path.toFile());
            if (userId != null) {
                command.setBranch(userId);
            }
            return command.call();
        } catch (GitAPIException ex) {
            log.error("Could not clone and checkout branch {} from repository {} to location {}. Reason: {}",
                    userId, uri, path, ex.toString());
            throw new RuntimeException(String.format("Could not clone repository {}", uri));
        }
    }

    private boolean repositoryExists(Path path) {
        FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
        return repositoryBuilder.findGitDir(path.toFile()).getGitDir() != null;
    }

    private boolean repositoryOnBranch(String userId, Path path) {
        try {
            Git git = Git.open(path.toFile());
            git.fetch().call();
            return userId.equals(git.getRepository().getBranch());
        } catch (GitAPIException | IOException ex) {
            log.error("Failed to load repository in: {}. Reason: {}", path, ex.toString());
        }

        return false;
    }

    private void checkoutBranch(Path path, String userId) {
        try {
            Git git = Git.open(path.toFile());
            git.checkout()
                    .setCreateBranch(true)
                    .setName(userId)
                    .call();
        } catch (IOException | GitAPIException ex) {
            log.error("Could not checkout branch {}. Reason: {}", userId, ex.toString());
        }
    }
}
