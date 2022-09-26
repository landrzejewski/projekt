package com.example.restdockerplatform.git;

import java.util.List;

public interface ProjectContentProvider {
    List<String> listTasks();
    List<String> listUserTasks(String userId);
}
