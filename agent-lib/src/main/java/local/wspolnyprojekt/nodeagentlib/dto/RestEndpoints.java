package local.wspolnyprojekt.nodeagentlib.dto;

public class RestEndpoints {

    public static final String FILENAME_PATH_VARIABLE = "filename";
    public static final String TASKID_PATH_VARIABLE = "taskid";

    /**
     * Metoda POST (jako body GitCredentials)
     * Wyciągnięte jako osobny endpoint (a nie w ramach GIT_ENDPOINT), bo credentials są globalne a nie per task
     */
    public static final String GIT_CREDENTIALS_ENDPOINT = "/system/gitcredentials";

    public static final String GIT_ENDPOINT = "/{" + TASKID_PATH_VARIABLE + "}/git";

    public static final String TASK_ENDPOINT = "/{" + TASKID_PATH_VARIABLE + "}";

    public static final String FTP_ENDPOINT = "/{" + TASKID_PATH_VARIABLE + "}/ftp/{*" + FILENAME_PATH_VARIABLE + "}";

    public static final String SYSTEM_LOAD = "/system/load";

    public static final String SYSTEM_PING = "/system/ping";

    // TODO Na razie proteza dla jeszcze nie zaimplementowanych funkcjonalności, docelowo
    // nie będzie możliwości zdalnego wykonywania dowolnego polecenia
    public static final String EXECUTE_SHELL_COMMAND_IN_WORKSPACE = "/{" + TASKID_PATH_VARIABLE + "}/execute";
}
