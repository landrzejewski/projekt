package local.wspolnyprojekt.nodeagentlib.dto;

public enum TaskStatus {
    TASK_STATUS_UNKNOWN, // Status na potrzeby prototypowania
    TASK_STATUS_NULL,
    TASK_STATUS_READY,
    TASK_STATUS_FAIL,
    TASK_STATUS_DELETED,
    TASK_STATUS_RUNNING,
    TASK_STATUS_DONE,
    TASK_STATUS_STOPPED,
    TASK_STATUS_ALLOCATED
}
