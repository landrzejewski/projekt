package com.example.restdockerplatform.persistence.inMemory;

import com.example.restdockerplatform.domain.UserTask;
import com.example.restdockerplatform.domain.UploadStatus;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

import static com.example.restdockerplatform.domain.UploadStatus.FINISHED;


@Repository
public class ProcessingRepository {

    private static final Map<UserTask, UploadStatus> USER_UPLOAD_STATUS_MAP = new HashMap<>();

    public void setStatus(UserTask userTask, UploadStatus status) {

        USER_UPLOAD_STATUS_MAP.put(userTask, status);
    }

    public boolean isFinished(UserTask usertask) {

        return FINISHED == USER_UPLOAD_STATUS_MAP.get(usertask)
                || !USER_UPLOAD_STATUS_MAP.containsKey(usertask); // may not be uploaded, but still ready to execute
    }

}
