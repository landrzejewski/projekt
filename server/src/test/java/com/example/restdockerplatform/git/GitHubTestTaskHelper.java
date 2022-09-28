package com.example.restdockerplatform.git;

import lombok.Getter;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

@Getter
public class GitHubTestTaskHelper {
    static private final String TEMP_DIRECTORY_PREFIX = "repo_test_";
    static private final String SOURCE_REPOSITORY_DIRECTORY_NAME = "source_repo";
    static private final String TARGET_REPOSITORY_DIRECTORY_NAME = "target_repo";
    static private final String DUMMY_FILE_NAME = "Readme.md";
    static private final String DUMMY_FILE_CONTENT = "CONTENT";
    static private final String DUMMY_COMMIT_MESSAGE = "Init Commit";
    static private final String TOKEN = "admin";

    String tempDirectory;
    String targetRepositoryPath;
    Path sourceDirectoryPath;
    Git remoteRepository;
    TaskRepository testedTaskRepository;

    ProjectContentProvider projectContentProvider;
    RepositoryContentProvider repositoryContentProvider;

    private GitHubTestTaskHelper() {
    }

    static GitHubTestTaskHelper init() throws GitAPIException, IOException {
        GitHubTestTaskHelper tr = new GitHubTestTaskHelper();

        tr.setupRemoteRepository();
        tr.setupTargetRepository();
        tr.setupTestedTaskRepository();

        return tr;
    }

    public GitHubTestTaskHelper withBranchForUser(String userId) throws GitAPIException {
        remoteRepository
                .checkout()
                .setCreateBranch(true)
                .setName(userId).call();

        return this;
    }

    public GitHubTestTaskHelper withUserRepository(String userId, String taskId) {
        testedTaskRepository.assignTaskToUser(userId, taskId, targetRepositoryPath);
        return this;
    }

    public GitHubTestTaskHelper withUserFileModified(String userId, String taskId) throws IOException {
        try (PrintWriter writer = new PrintWriter(
                Paths.get(targetRepositoryPath, userId, taskId, DUMMY_FILE_NAME).toString(),
                StandardCharsets.UTF_8)) {
            writer.append("aa");
        }

        return this;
    }

    private void setupRemoteRepository() throws IOException, GitAPIException {
        tempDirectory = Files.createTempDirectory(TEMP_DIRECTORY_PREFIX).toFile().getAbsolutePath();

        sourceDirectoryPath = Paths.get(tempDirectory, SOURCE_REPOSITORY_DIRECTORY_NAME);

        Files.createDirectory(sourceDirectoryPath);
        remoteRepository = Git.init().setDirectory(sourceDirectoryPath.toFile()).call();

        try (PrintWriter writer = new PrintWriter(Paths.get(sourceDirectoryPath.toString(), DUMMY_FILE_NAME).toString(), StandardCharsets.UTF_8)) {
            writer.println(DUMMY_FILE_CONTENT);
        }

        remoteRepository.add().addFilepattern(".").call();
        remoteRepository.commit().setMessage(DUMMY_COMMIT_MESSAGE).call();
    }

    private void setupTargetRepository() {
        targetRepositoryPath = Paths.get(tempDirectory, TARGET_REPOSITORY_DIRECTORY_NAME).toString();
    }

    private void setupTestedTaskRepository() {
        GitHubConfigurationConfig  gitHubConfigurationConfig = new GitHubConfigurationConfig(
                tempDirectory,
                SOURCE_REPOSITORY_DIRECTORY_NAME,
                TOKEN,
                targetRepositoryPath);

        var credentialsProvider = new GitHubCredentialsProvider(gitHubConfigurationConfig);

        projectContentProvider = mock(ProjectContentProvider.class);
        repositoryContentProvider = spy(new GitHubRepositoryContentProvider(credentialsProvider));

        testedTaskRepository = new GitHubTaskRepository(
                gitHubConfigurationConfig,
                new GitHubCredentialsProvider(gitHubConfigurationConfig),
                projectContentProvider,
                repositoryContentProvider,
                new LocalRepositoryUriParser()
        );

    }
}
