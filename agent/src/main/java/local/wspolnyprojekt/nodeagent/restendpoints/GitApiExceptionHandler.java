package local.wspolnyprojekt.nodeagent.restendpoints;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GitApiExceptionHandler {
    @ExceptionHandler(GitAPIException.class)
    String gitApiExceptionHandler(GitAPIException gitAPIException) {
        return gitAPIException.getMessage();
    }
}
