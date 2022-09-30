package local.wspolnyprojekt.nodeagent.restendpoints;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GitApiExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(GitAPIException.class)
    String gitApiExceptionHandler(GitAPIException gitAPIException) {
        return gitAPIException.getMessage();
    }
}
