package com.example.restdockerplatform.git;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.RepositoryNotFoundException;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class GitHubRepositoryContentProvider implements RepositoryContentProvider {

    private final CredentialsProvider credentialsProvider;

    @Override
    public Git cloneRepository(String uri, String userId, Path path) {
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
            throw new RuntimeException(String.format("Could not clone repository %s", uri));
        }
    }

    @Override
    public boolean repositoryExists(Path path) {
        FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
        return repositoryBuilder.findGitDir(path.toFile()).getGitDir() != null;
    }

    @Override
    public void checkoutBranch(Path path, String userId) throws RepositoryNotFoundException {
        Git git = openRepository(path.toFile());
        try {
            git.checkout()
                    .setCreateBranch(true)
                    .setName(userId)
                    .call();
        } catch (GitAPIException ex) {
            log.error("Could not checkout branch {}. Reason: {}", userId, ex.toString());
        }
    }

    @Override
    public boolean repositoryOnBranch(String userId, Path path) throws RepositoryNotFoundException {
        Git git = openRepository(path.toFile());
        try {
            git.fetch().call();
            return userId.equals(git.getRepository().getBranch());
        } catch (GitAPIException | IOException ex) {
            log.error("Failed to load repository in: {}. Reason: {}", path, ex.toString());
        }

        return false;
    }

    @Override
    public void pullChanges(Path path) {
        try {
            Git git = Git.open(path.toFile());
            git.pull().call();
        } catch (IOException | GitAPIException ex) {
            log.error("Could not pull changes to repository {}. Reason: {}", path, ex.toString());
        }
    }

    @Override
    public int addModifiedFiles(Path path) throws RepositoryNotFoundException {
        Git git = openRepository(path.toFile());

        int filesModifiedCount;
        try {
            Status status = git.status().call();
            AddCommand addCommand = git.add();

            var modifiedFiles = status.getModified();
            if (modifiedFiles.isEmpty()) {
                log.info("No files modified in: {}", path);
                return 0;
            }
            filesModifiedCount = modifiedFiles.size();

            status.getModified().forEach(addCommand::addFilepattern);

            addCommand.call();
        } catch (GitAPIException ex) {
            log.error("Failed to add modified files in repository: {}. Reason: {}", path, ex.toString());
            return 0;
        }

        return filesModifiedCount;
    }
    @Override
    public void commit(Path path) throws RepositoryNotFoundException {
        Git git = openRepository(path.toFile());

        try {
            git.commit().setMessage(UUID.randomUUID().toString()).call();
        } catch (GitAPIException ex) {
            log.error("Could not commit files in repository: {}. Reason: {}", path, ex.toString());
        }
    }
    @Override
    public void push(Path path) throws RepositoryNotFoundException {
        Git git = openRepository(path.toFile());

        try {
            git.push().setCredentialsProvider(credentialsProvider).call();
        } catch (GitAPIException ex) {
            log.error("Failed to push. Reason: {}", ex.toString());
        }
    }

    private Git openRepository(File file) throws RepositoryNotFoundException {
        try {
            return Git.open(file);
        } catch (IOException ex) {
            log.error("Failed to load repository in: {}. Reason: {}", file.toString(), ex.toString());
            throw new RepositoryNotFoundException(String.format("Could not find repository %s", file.toPath()));
        }
    }
}
