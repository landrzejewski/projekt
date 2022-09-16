package local.wspolnyprojekt.nodeagent.common;

import lombok.Data;

@Data
public class ShellCommand {
    private String command;
    private String[] args;
    private long timeoutInSeconds; // TimeUnit.SECONDS; -1 == brak timeout
}
