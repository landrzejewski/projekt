package com.example.restdockerplatform.rest;

import com.example.restdockerplatform.utils.FileUtil;
import com.example.restdockerplatform.utils.ZipUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


@Component
@Slf4j
public class FileService {


    private static String USER_ZIPFILE_SPACE;
    private static String USER_FILE_SPACE;


    public FileService(
            @Value("${user.zipfile.space}") String userZipfileSpace,
            @Value("${user.file.space}") String userFileSpace) {

        USER_ZIPFILE_SPACE = userZipfileSpace;
        USER_FILE_SPACE = userFileSpace;
    }


    public static Resource getFileAsResource(String fileCodeName) throws IOException {

        log.info(" -> getFileAsResource, fileCode: {}", fileCodeName);
        final Path dirPath = Paths.get(USER_ZIPFILE_SPACE);

        Path foundFile = Files.list(dirPath)
                .filter(file -> fileCodeName.equals(file.getFileName().toString()))
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

        String fileCodeName = FileUtil.getFileCodeName(user, project);

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileCodeName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Could not save file: " + fileCodeName, ioe);
        }

        return fileCodeName;
    }


    public String zipFiles(String user, String project) throws IOException {

        final String fileCode = FileUtil.getFileCode(user, project);
        final String fileCodeName = FileUtil.getFileCodeName(user, project);

        final File directoryToZip = new File(USER_FILE_SPACE + File.separator + fileCode);
        final String zipFileName = USER_ZIPFILE_SPACE + File.separator + fileCodeName;

        ZipUtil.zipDirectory(directoryToZip, zipFileName);

        return fileCodeName;
    }

    public void unzipFile(String user, String project) {

        final String fileCode = FileUtil.getFileCode(user, project);
        final String fileCodeName = FileUtil.getFileCodeName(user, project);

        final String zipFilePath = USER_ZIPFILE_SPACE + File.separator + fileCodeName;
        final String destinationDirectory = USER_FILE_SPACE + File.separator + fileCode;

        ZipUtil.unzip(zipFilePath, destinationDirectory);
    }

    public boolean verifyMultipartFileExtension(MultipartFile multipartFile) {
        return multipartFile.getOriginalFilename().toLowerCase().endsWith(FileUtil.ZIPFILE_EXTENSION);
    }

}
