package com.example.restdockerplatform.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


@Component
public class FileUtil {
    private static String USER_ZIPFILE_SPACE;

    @Value("${user.zipfile.space}")
    public void setStaticUserZipfileSpace(String userZipfileSpace){
        USER_ZIPFILE_SPACE = userZipfileSpace;
    }


    public static Resource getFileAsResource(String fileCode) throws IOException {

        final Path dirPath = Paths.get(USER_ZIPFILE_SPACE);

        Path foundFile = Files.list(dirPath)
                .filter(file -> fileCode.equals(file.getFileName().toString()))
                .findFirst()
                .orElse(null);


        if (foundFile != null) {
            return new UrlResource(foundFile.toUri());
        }

        return null;
    }


    public static String saveFile(String user, String project, MultipartFile multipartFile)
            throws IOException {

        Path uploadPath = Paths.get(USER_ZIPFILE_SPACE);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileCode = getFileCode(user, project);

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileCode);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Could not save file: " + fileCode, ioe);
        }

        return fileCode;
    }


    public static String getFileCode(String part1, String part2) {
        return part1 + "__" + part2 + ".zip";
    }

}
