package com.example.restdockerplatform.git;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.Test;
import org.junit.jupiter.api.Disabled;

import java.io.File;


public class GitHubTaskRepositoryTest {
    static final String WORK_DIR = "C:\\project_workspace";
    static final String GIT_HUB_REPOSITORY_URL = "https://github.com/";
    static final String GIT_HUB_USER = "java-dev-pro-project";
    static final String GH_TOKEN = "";
    static final GitHubConfigurationConfig  gitHubConfigurationConfig = new GitHubConfigurationConfig(
            GIT_HUB_REPOSITORY_URL,
            GIT_HUB_USER,
            GH_TOKEN,
            WORK_DIR);
    GitHubTaskRepository tr = new GitHubTaskRepository(
            gitHubConfigurationConfig,
            new GitHubCredentialsProvider(gitHubConfigurationConfig),
            new GitHubUser(gitHubConfigurationConfig)
            );

    @Test
    @Disabled
    public void shouldCreateRepo() throws GitAPIException {
        Git.cloneRepository()
                .setURI("C:\\Users\\soker\\work\\testRepo")
                .setDirectory(new File("C:\\Users\\soker\\work\\gitWorkDir"))
                .setBranch("master").call().close();
    }

    @Test
    @Disabled
    public void shouldListAllRepositories() {
        tr.listTasks().forEach(System.out::println);
    }

    @Test
    @Disabled
    public void shouldListAllTasksAvailableForUser() {
        String userName = "user_jan";
        tr.listUserTasks(userName).forEach(System.out::println);
    }

    @Test
    @Disabled
    public void shouldCloneUserBranchFromRepository() {
        String userId = "user_karol";
        String taskId = "task3";

        tr.getTask(userId, taskId, WORK_DIR);
    }

    @Test
    @Disabled
    public void shouldCreateBranchForUserInTask() {
        String userId = "user_karol";
        String taskId = "task3";

        tr.assignTaskToUser(userId, taskId, WORK_DIR);
    }

    @Test
    @Disabled
    public void shouldCommitAndPushUserChangesInBranch() {
        String userId = "user_karol";
        String taskId = "task3";

        tr.saveTask(userId, taskId, WORK_DIR);
    }
}
