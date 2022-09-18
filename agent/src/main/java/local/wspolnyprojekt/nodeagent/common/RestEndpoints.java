package local.wspolnyprojekt.nodeagent.common;

public class RestEndpoints {

    /** Metoda POST (jako body GitCredentials) */
    public static final String GIT_CREDENTIALS = "/{taskid}/gitcredentials";
    /** Metoda POST (jako body GitResource) */
    public static final String GIT_CLONE = "/{taskid}/git";
    /** Metoda PUT, puste body */
    public static final String GIT_PULL = GIT_CLONE;

    public static final String DOCKER_UP = "/{taskid}/up";
    public static final String DOCKER_DOWN = "/{taskid}/down";
    public static final String DOCKER_LOG = "/{taskid}/log";
    public static final String DOCKER_CLEANUP = "/{taskid}/cleanup";

    public static final String FTP_GET_FILE = "/{taskid}/ftp/{*filename}";
    public static final String FTP_POST_FILE = FTP_GET_FILE;
    public static final String FTP_DELETE_FILE = FTP_GET_FILE;

    public static final String SYSTEM_LOAD = "/system/load";

    // Na razie proteza dla jeszcze nie zaimplementowanych funkcjonalności, docelowo
    // nie będzie możliwości zdalnego wykonywania dowolnego polecenia
    public static final String EXECUTE_SHELL_COMMAND_IN_WORKSPACE = "/{taskid}/execute";
}
