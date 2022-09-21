package com.example.restdockerplatform.git;

import org.junit.Test;


public class GitHubTaskRepositoryTest {
    static final String WORK_DIR = "C:\\project_workspace";
    static final String GIT_HUB_REPOSITORY_URL = "https://github.com/";
    static final String GIT_HUB_USER = "java-dev-pro-project";
    static final String GH_TOKEN = "";
    static final GitHubConfigurationConfig  gitHubConfigurationConfig = new GitHubConfigurationConfig(GIT_HUB_REPOSITORY_URL, GIT_HUB_USER, GH_TOKEN);
    GitHubTaskRepository tr = new GitHubTaskRepository(
            gitHubConfigurationConfig,
            new GitHubCredentialsProvider(gitHubConfigurationConfig),
            new GitHubUser(gitHubConfigurationConfig));

    @Test
    public void shouldListAllRepositories() {
        tr.listTasks().forEach(System.out::println);
    }

    @Test
    public void shouldListAllTasksAvailableForUser() {
        String userName = "user_jan";
        tr.listUserTasks(userName).forEach(System.out::println);
    }

    @Test
    public void shouldCloneUserBranchFromRepository() {
        String userId = "user_tomasz";
        String taskId = "task3";

        tr.getTask(userId, taskId, WORK_DIR);
    }

    @Test
    public void shouldCreateBranchForUserInTask() {
        String userId = "user_adam";
        String taskId = "task3";

        tr.assignTaskToUser(userId, taskId, WORK_DIR);
    }

    @Test
    public void shouldCommitAndPushUserChangesInBranch() {
        String userId = "user_adam";
        String taskId = "task3";

        tr.saveTask(userId, taskId, WORK_DIR);
    }
}
