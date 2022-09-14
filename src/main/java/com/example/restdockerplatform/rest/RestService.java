package com.example.restdockerplatform.rest;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Service
class RestService {

    Resource getFile(String user, String project) throws IOException {

        // TODO get files from Git
        // TODO prepare zip file

        final String fileCode = FileUtil.getFileCode(user, project);

        Resource resource = FileUtil.getFileAsResource(fileCode);

        return resource;
    }


    String saveFile(String user, String project, MultipartFile multipartFile) throws IOException {

        final String fileCode = FileUtil.saveFile(user, project, multipartFile);

        // TODO unzip zip file in proper place
        // TODO commit and push changes to Git repository

        return fileCode;
    }


    FileUploadResponse prepareFileUploadResponse(String user, String project, MultipartFile multipartFile, String filecode) {
        FileUploadResponse response = new FileUploadResponse();
        response.setFileName(filecode);
        long size = multipartFile.getSize();

        response.setSize(size);
        response.setDownloadUri(RestUtil.getDownloadUri(user, project));
        return response;
    }


    ResponseEntity<?> prepareFileNotFoundReponse() {
        return new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);
    }


    ResponseEntity<Resource> prepareResponseEntityWithFile(Resource resource) {
        String contentType = "application/octet-stream";
        String headerValue = "attachment; filename=\"" + resource.getFilename() + "\"";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                .body(resource);
    }


}
