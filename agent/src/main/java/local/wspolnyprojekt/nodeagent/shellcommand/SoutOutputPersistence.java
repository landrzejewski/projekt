package local.wspolnyprojekt.nodeagent.shellcommand;

import java.time.LocalDateTime;

public class SoutOutputPersistence implements OutputPersistence {
    @Override
    public void save(String taskid, String text) {
        System.out.println(LocalDateTime.now() + ": " + text);
    }
}
