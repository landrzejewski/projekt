package local.wspolnyprojekt.nodeagent.restendpoints;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;

@ControllerAdvice
public class FtpApiExceptionHandler {
    @ExceptionHandler(IOException.class)
    String gitApiExceptionHandler(IOException ioException) {
        return ioException.getMessage();
    }

}
