package local.wspolnyprojekt.nodeagentlib.dto;

public enum TaskCommand implements JsonString {
    TASK_COMMAND_START,
    TASK_COMMAND_STOP,
    TASK_COMMAND_LOG,
    TASK_COMMAND_CLEANUP,
    TASK_COMMAND_STATUS,
    TASK_COMMAND_DELETE
}
