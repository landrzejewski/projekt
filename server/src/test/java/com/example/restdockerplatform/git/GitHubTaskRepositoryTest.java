package com.example.restdockerplatform.git;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.RepositoryNotFoundException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class GitHubTaskRepositoryTest {

    @Test
    public void shouldCloneUserBranchFromRepository() throws GitAPIException, IOException {
        // given
        String userId = "user_karol";
        String taskId = "task3";
        var helper = GitHubTestTaskHelper.init().withBranchForUser(userId);

        Path expectedPath = Paths.get(helper.targetRepositoryPath, userId, taskId);

        when(helper.getProjectContentProvider().listUserTasks(userId)).thenReturn(List.of(taskId));

        // when
        helper.getTestedTaskRepository().getTask(userId, taskId);

        // then
        Assertions.assertTrue(expectedPath.toFile().exists());
        Assertions.assertTrue(helper.getRepositoryContentProvider().repositoryOnBranch(userId, expectedPath));
        Mockito.verify(helper.getRepositoryContentProvider(), times(0))
                .pullChanges(any());
    }

    @Test
    public void whenRepositoryClonedAndBranchAlreadyCheckedOutShouldPull() throws GitAPIException, IOException {
        // given
        String userId = "user_karol";
        String taskId = "task3";
        var helper = GitHubTestTaskHelper.init().withBranchForUser(userId);

        when(helper.getProjectContentProvider().listUserTasks(userId)).thenReturn(List.of(taskId));

        // when
        helper.getTestedTaskRepository().getTask(userId, taskId);
        helper.getTestedTaskRepository().getTask(userId, taskId);

        // then
        Mockito.verify(helper.getRepositoryContentProvider(), times(1))
                .cloneRepository(any(), any(), any());
        Mockito.verify(helper.getRepositoryContentProvider(), times(1))
                .checkoutBranch(any(), any());
        Mockito.verify(helper.getRepositoryContentProvider(), times(1))
                .pullChanges(any());
    }

    @Test
    public void whenTaskNotAssignedToUserShouldNotClone() throws GitAPIException, IOException {
        // given
        String userId = "user_karol";
        String taskId = "task3";
        var helper = GitHubTestTaskHelper.init();

        when(helper.getProjectContentProvider().listUserTasks(userId)).thenReturn(Collections.emptyList());

        // when
        helper.getTestedTaskRepository().getTask(userId, taskId);

        // then
        Mockito.verify(helper.getRepositoryContentProvider(), times(0))
                .cloneRepository(any(), any(), any());
        Mockito.verify(helper.getRepositoryContentProvider(), times(0))
                .checkoutBranch(any(), any());
        Mockito.verify(helper.getRepositoryContentProvider(), times(0))
                .pullChanges(any());

    }

    @Test
    public void shouldAssignNewTaskToUser() throws GitAPIException, IOException {
        // given
        var helper = GitHubTestTaskHelper.init();
        String userId = "user_karol";
        String taskId = "task3";

        when(helper.getProjectContentProvider().listUserTasks(anyString())).thenReturn(Collections.emptyList());

        // when
        helper.getTestedTaskRepository().assignTaskToUser(userId, taskId);

        // then
        var remoteBranches = helper.getRemoteRepository().branchList().call()
                .stream().map(Ref::getName).collect(Collectors.toList());

        assertThat(remoteBranches).contains("refs/heads/master");
        assertThat(remoteBranches).contains("refs/heads/user_karol");
        assertThat(remoteBranches).hasSize(2);
    }

    @Test
    public void shouldNotAssignTaskToUserIfTaskAlreadyAssigned() throws GitAPIException, IOException {
        // given
        var helper = GitHubTestTaskHelper.init();
        String userId = "user_karol";
        String taskId = "task3";

        when(helper.getProjectContentProvider().listUserTasks(anyString())).thenReturn(List.of(taskId));

        // when
        helper.getTestedTaskRepository().assignTaskToUser(userId, taskId);

        // then
        Mockito.verify(helper.getRepositoryContentProvider(), times(0))
                .cloneRepository(any(), any(), any());
        Mockito.verify(helper.getRepositoryContentProvider(), times(0))
                .checkoutBranch(any(), any());
    }

    @Test
    public void shouldNotCommitWhenNoChangesInRepo() throws GitAPIException, IOException {
        // given
        String userId = "user_karol";
        String taskId = "task3";
        var helper = GitHubTestTaskHelper.init().withUserRepository(userId, taskId);

        // when
        helper.getTestedTaskRepository().saveTask(userId, taskId);

        // then
        Mockito.verify(helper.getRepositoryContentProvider(), times(1))
                .addModifiedFiles(any());
        Mockito.verify(helper.getRepositoryContentProvider(), times(0))
                .commit(any());
        Mockito.verify(helper.getRepositoryContentProvider(), times(0))
                .push(any());
    }

    @Test
    public void shouldCommitAndPushWhenFileChanged() throws GitAPIException, IOException {
        // given
        String userId = "user_karol";
        String taskId = "task3";
        var helper = GitHubTestTaskHelper.init()
                .withUserRepository(userId, taskId)
                .withUserFileModified(userId, taskId);

        // when
        helper.getTestedTaskRepository().saveTask(userId, taskId);

        // then
        Mockito.verify(helper.getRepositoryContentProvider(), times(1))
                .addModifiedFiles(any());
        Mockito.verify(helper.getRepositoryContentProvider(), times(1))
                .commit(any());
        Mockito.verify(helper.getRepositoryContentProvider(), times(1))
                .push(any());

        int count = 0;
        helper.getRemoteRepository().checkout().setName(userId).call();
        for( RevCommit ignored : helper.getRemoteRepository().log().call() ) {
            count++;
        }

        Assertions.assertEquals(2, count);
    }

    @Test
    public void shouldThrowWhenUserRepositoryDoesNotExist() throws GitAPIException, IOException {
        // given
        String userId = "user_karol";
        String taskId = "task3";
        var helper = GitHubTestTaskHelper.init();

        // when, then
        Assertions.assertThrows(RepositoryNotFoundException.class, () -> { helper.getTestedTaskRepository().saveTask(userId, taskId); });
    }
}
