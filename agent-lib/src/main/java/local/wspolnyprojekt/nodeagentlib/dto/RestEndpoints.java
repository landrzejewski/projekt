package local.wspolnyprojekt.nodeagentlib.dto;

public class RestEndpoints {
    public static final String FILENAME_PATH_VARIABLE = "filename";
    public static final String TASKID_PATH_VARIABLE = "taskid";
    /**
     * Metoda POST (jako body GitCredentials)
     */
    public static final String GIT_CREDENTIALS_ENDPOINT = "/system/gitcredentials";
    /**
     * Metoda POST (jako body GitResource)
     */
    public static final String GIT_ENDPOINT = "/{" + TASKID_PATH_VARIABLE + "}/git";
    /**
     * Metoda PUT, puste body
     */
    //public static final String GIT_PULL = GIT_ENDPOINT;

    public static final String TASK_ENDPOINT = "/{" + TASKID_PATH_VARIABLE + "}";

//    public static final String TASK_START = "/{" + TASKID_PATH_VARIABLE + "}/up";
//    public static final String TASK_STOP = "/{" + TASKID_PATH_VARIABLE + "}/down";
    public static final String TASK_LOG = "/{" + TASKID_PATH_VARIABLE + "}/log";
//    public static final String TASK_CLEANUP = "/{" + TASKID_PATH_VARIABLE + "}/cleanup";
    public static final String TASK_STATUS = "/{" + TASKID_PATH_VARIABLE + "}/status";
    public static final String TASK_DELETE = "/{" + TASKID_PATH_VARIABLE + "}/delete";

    public static final String FTP_ENDPOINT = "/{" + TASKID_PATH_VARIABLE + "}/ftp/{*" + FILENAME_PATH_VARIABLE + "}";

    public static final String SYSTEM_LOAD = "/system/load";
    public static final String SYSTEM_PING = "/system/ping";

    // Na razie proteza dla jeszcze nie zaimplementowanych funkcjonalności, docelowo
    // nie będzie możliwości zdalnego wykonywania dowolnego polecenia
    public static final String EXECUTE_SHELL_COMMAND_IN_WORKSPACE = "/{" + TASKID_PATH_VARIABLE + "}/execute";
}
