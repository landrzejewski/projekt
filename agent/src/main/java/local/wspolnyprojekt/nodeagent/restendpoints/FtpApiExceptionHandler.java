package local.wspolnyprojekt.nodeagent.restendpoints;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;

@ControllerAdvice
public class FtpApiExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(IOException.class)
    String gitApiExceptionHandler(IOException ioException) {
        return ioException.getMessage();
    }

}
