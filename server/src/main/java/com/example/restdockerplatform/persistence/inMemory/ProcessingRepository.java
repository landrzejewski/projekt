package com.example.restdockerplatform.persistence.inMemory;

import com.example.restdockerplatform.domain.ProcessStatus;
import com.example.restdockerplatform.domain.UploadStatus;
import com.example.restdockerplatform.domain.UserTask;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

import static com.example.restdockerplatform.domain.ProcessStatus.ERROR;
import static com.example.restdockerplatform.domain.ProcessStatus.NOT_READY;
import static com.example.restdockerplatform.domain.ProcessStatus.READY;


@Repository
public class ProcessingRepository {

    private static final Map<UserTask, UploadStatus> USER_UPLOAD_STATUS_MAP = new HashMap<>();

    public void setStatus(UserTask userTask, UploadStatus status) {

        USER_UPLOAD_STATUS_MAP.put(userTask, status);
    }

    public ProcessStatus getStatus(UserTask usertask) {

        if (!USER_UPLOAD_STATUS_MAP.containsKey(usertask)) {
            // may not be uploaded, but still ready to execute
            return READY;
        }

        final UploadStatus status = USER_UPLOAD_STATUS_MAP.get(usertask);

        switch (status) {
            case ERROR -> {
                return ERROR;
            }
            case FINISHED -> {
                return READY;
            }
            default -> {
                return NOT_READY;
            }

        }

    }

}
