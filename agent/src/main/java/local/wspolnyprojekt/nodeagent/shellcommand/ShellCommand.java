package local.wspolnyprojekt.nodeagent.shellcommand;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShellCommand {
    private String command;
    private String[] args;
    private long timeoutInSeconds; // TimeUnit.SECONDS; -1 == brak timeout
}
