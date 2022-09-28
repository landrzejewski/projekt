package com.example.restdockerplatform.git;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.errors.RepositoryNotFoundException;

import java.io.IOException;
import java.nio.file.Path;

public interface RepositoryContentProvider {
    Git cloneRepository(String uri, String userId, Path path);
    boolean repositoryExists(Path path);
    void checkoutBranch(Path path, String userId) throws RepositoryNotFoundException;
    boolean repositoryOnBranch(String userId, Path path) throws RepositoryNotFoundException;
    void pullChanges(Path path);
    int addModifiedFiles(Path path) throws RepositoryNotFoundException;
    void commit(Path path) throws RepositoryNotFoundException;
    void push(Path path) throws RepositoryNotFoundException;
}
