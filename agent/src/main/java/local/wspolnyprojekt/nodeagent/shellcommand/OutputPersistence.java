package local.wspolnyprojekt.nodeagent.shellcommand;


public interface OutputPersistence {
    void save(String taskid, String text);
}
