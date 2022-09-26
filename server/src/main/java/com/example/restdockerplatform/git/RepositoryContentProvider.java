package com.example.restdockerplatform.git;

import org.eclipse.jgit.api.Git;

import java.nio.file.Path;

public interface RepositoryContentProvider {
    Git cloneRepository(String uri, String userId, Path path);
    boolean repositoryExists(Path path);
    void checkoutBranch(Path path, String userId);
    boolean repositoryOnBranch(String userId, Path path);
    void pullChanges(Path path);
}
