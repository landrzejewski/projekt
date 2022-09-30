package com.example.restdockerplatform.rest;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
class FileUploadResponse {

    private String fileName;
    private String downloadUri;
    private String saveStatusUri;
    private long size;

}
