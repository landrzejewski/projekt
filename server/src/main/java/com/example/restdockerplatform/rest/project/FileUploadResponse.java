package com.example.restdockerplatform.rest.project;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public
class FileUploadResponse {

    private String fileName;
    private String downloadUri;
    private String saveStatusUri;
    private long size;

}
