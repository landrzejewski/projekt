package com.example.restdockerplatform.rest;

import lombok.AllArgsConstructor;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Service
@AllArgsConstructor
class RestService {


    private FileService fileService;


    Resource getFile(String user, String project) throws IOException {

        // TODO get files from Git


        // prepare zip file
        final String fileCodeName = fileService.zipFiles(user, project);

        Resource resource = fileService.getFileAsResource(fileCodeName);

        return resource;
    }


    String saveFile(String user, String project, MultipartFile multipartFile) throws IOException {

        if (!fileService.verifyMultipartFileExtension(multipartFile)) {
            throw new IncorrectFileTypeException("Incorrect file type");
        }

        final String fileCodeName = fileService.saveFile(user, project, multipartFile);

        // TODO test if its a .zip file

        // unzip zip file in proper place
        fileService.unzipFile(user, project);


        // TODO commit and push changes to Git repository

        return fileCodeName;
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
