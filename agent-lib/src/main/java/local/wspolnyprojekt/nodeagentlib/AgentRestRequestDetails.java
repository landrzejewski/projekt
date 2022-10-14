package local.wspolnyprojekt.nodeagentlib;

import local.wspolnyprojekt.nodeagentlib.dto.*;

import static local.wspolnyprojekt.nodeagentlib.dto.RestEndpoints.*;

public class AgentRestRequestDetails {

    /**
     * Bez taskId, bo credentials jest globalne, a nie per task
     */

    public static RequestDetails gitCredentialsRequestDetails(String username, String password) {
        return gitCredentialsRequestDetails(new GitCredentials(username, password));
    }

    private static RequestDetails gitCredentialsRequestDetails(GitCredentials gitCredentials) {
        return RequestDetails.builder()
                .requestMethod(AgentRequestMethod.POST)
                .jsonPayload(gitCredentials.getJsonString())
                .uriEndpoint(GIT_CREDENTIALS_ENDPOINT)
                .build();
    }

    public static RequestDetails gitCloneRequestDetails(String repositoryUrl, String branch, String taskId) {
        return gitCloneRequestDetails(new GitResource(repositoryUrl, branch), taskId);
    }

    private static RequestDetails gitCloneRequestDetails(GitResource gitResource, String taskId) {
        return RequestDetails.builder()
                .requestMethod(AgentRequestMethod.POST)
                .jsonPayload(gitResource.getJsonString())
                .uriEndpoint(GIT_ENDPOINT.replace("{" + TASKID_PATH_VARIABLE + "}", taskId))
                .build();
    }

    public static RequestDetails gitPullRequestDetails(String taskId) {
        return RequestDetails.builder()
                .requestMethod(AgentRequestMethod.PUT)
                .jsonPayload("")
                .uriEndpoint(GIT_ENDPOINT.replace("{" + TASKID_PATH_VARIABLE + "}", taskId))
                .build();
    }

    public static RequestDetails getSystemLoadRequestDetails() {
        return RequestDetails.builder()
                .requestMethod(AgentRequestMethod.GET)
                .jsonPayload("")
                .uriEndpoint(SYSTEM_LOAD)
                .build();
    }

    public static RequestDetails getPingRequestDetails() {
        return RequestDetails.builder()
                .requestMethod(AgentRequestMethod.GET)
                .jsonPayload("")
                .uriEndpoint(SYSTEM_PING)
                .build();
    }

    public static RequestDetails getFtpGetFileRequestDetaild(String file, String taskId) {
        return getFtpRequestDetails(AgentRequestMethod.GET, file, taskId);
    }

    /**
     * Jako body dane binarne z Content-Type: application/octet-stream
     */
    public static RequestDetails getFtpPostFileRequestDetaild(String file, String taskId) {
        return getFtpRequestDetails(AgentRequestMethod.POST, file, taskId);
    }

    public static RequestDetails getFtpDeleteFileRequestDetaild(String file, String taskId) {
        return getFtpRequestDetails(AgentRequestMethod.DELETE, file, taskId);
    }

    public static RequestDetails getTaskStartRequestDetails(String taskId) {
        return getTaskRequestDetails(TaskCommand.TASK_COMMAND_START, taskId);
    }

    public static RequestDetails getTaskStopRequestDetails(String taskId) {
        return getTaskRequestDetails(TaskCommand.TASK_COMMAND_STOP, taskId);
    }

    public static RequestDetails getTaskLogRequestDetails(String taskId) {
        return getTaskRequestDetails(TaskCommand.TASK_COMMAND_LOG, taskId);
    }

    public static RequestDetails getTaskCleanupRequestDetails(String taskId) {
        return getTaskRequestDetails(TaskCommand.TASK_COMMAND_CLEANUP, taskId);
    }

    public static RequestDetails getTaskStatusRequestDetails(String taskId) {
        return getTaskRequestDetails(TaskCommand.TASK_COMMAND_STATUS, taskId);
    }

    public static RequestDetails getTaskDeleteRequestDetails(String taskId) {
        return getTaskRequestDetails(TaskCommand.TASK_COMMAND_DELETE, taskId);
    }

    private static RequestDetails getFtpRequestDetails(AgentRequestMethod requestMethod, String file, String taskId) {
        return RequestDetails.builder()
                .requestMethod(requestMethod)
                .jsonPayload("")
                .uriEndpoint(FTP_ENDPOINT.replace("{" + TASKID_PATH_VARIABLE + "}", taskId).replace("{*" + FILENAME_PATH_VARIABLE + "}", file))
                .build();
    }

    private static RequestDetails getTaskRequestDetails(TaskCommand taskCommand, String taskId) {
        return RequestDetails.builder()
                .requestMethod(AgentRequestMethod.POST)
                .uriEndpoint(TASK_ENDPOINT.replace("{" + TASKID_PATH_VARIABLE + "}", taskId))
                .jsonPayload(taskCommand.getJsonString())
                .build();
    }

    private AgentRestRequestDetails() {
    }

}
