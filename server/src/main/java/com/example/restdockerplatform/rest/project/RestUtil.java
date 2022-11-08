package com.example.restdockerplatform.rest.project;

public class RestUtil {

    public static String getDownloadUri(String user, String project) {

        return new StringBuilder()
                .append("/api/project/")
                .append(user)
                .append("/")
                .append(project)
                .toString();
    }

    public static String getSaveStatusUri(String user, String project) {

        return new StringBuilder()
                .append("/api/project/saveStatus/")
                .append(user)
                .append("/")
                .append(project)
                .toString();
    }

}
