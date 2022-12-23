package com.example.restdockerplatform.file;

import com.example.restdockerplatform.utils.FileUtil;
import com.example.restdockerplatform.utils.UnZipException;
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
import java.util.Optional;


@Slf4j
@Component
public class FileService {


    private static String USER_ZIPFILE_SPACE;
    private static String USER_FILE_SPACE;


    public FileService(
            @Value("${user.zipfile.space}") String userZipfileSpace,
            @Value("${user.file.space}") String userFileSpace) {

        USER_ZIPFILE_SPACE = userZipfileSpace;
        USER_FILE_SPACE = userFileSpace;
    }


    /**
     * returns zip file of git repository branch (project/user) directory
     *
     * @param user    user
     * @param project project
     * @return
     */
    public Resource getFile(String user, String project) throws IOException {

        final String fileCodeName = zipFiles(user, project);

        return getFileAsResource(fileCodeName);
    }


    private Resource getFileAsResource(String fileCodeName) throws IOException {

        log.info(" -> getFileAsResource, fileCode: {}", fileCodeName);
        final Path dirPath = Paths.get(USER_ZIPFILE_SPACE);

        Path foundFile = Files.list(dirPath)
                .filter(file -> fileCodeName.equals(file.getFileName().toString()))
                .findFirst()
                .orElse(null);


        if (foundFile != null) {
            return new UrlResource(foundFile.toUri());
        }

        // todo zamiast null zwrócić Optional<Resource>
        return null;
    }


    /**
     * Saves file in user zipfile space
     *
     * @param user          user
     * @param project       project
     * @param multipartFile file
     * @return
     * @throws IOException
     */
    public String saveFile(String user, String project, MultipartFile multipartFile)
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


    private String zipFiles(String user, String project) throws IOException {

        final String fileCodeName = FileUtil.getFileCodeName(user, project);

        final File directoryToZip = new File(USER_FILE_SPACE + File.separator + user + File.separator + project);
        final String zipFileName = USER_ZIPFILE_SPACE + File.separator + fileCodeName;

        ZipUtil.zipDirectory(directoryToZip, zipFileName);

        return fileCodeName;
    }

    public void unzipFile(String user, String project) throws UnZipException {

        final String fileCodeName = FileUtil.getFileCodeName(user, project);

        final String zipFilePath = USER_ZIPFILE_SPACE + File.separator + fileCodeName;
        final String destinationDirectory = USER_FILE_SPACE + File.separator + user + File.separator + project;

        ZipUtil.unzip(zipFilePath, destinationDirectory);
    }

    public boolean verifyMultipartFileExtension(MultipartFile multipartFile) {

        return Optional.ofNullable(multipartFile)
                .map(MultipartFile::getOriginalFilename)
                .map(String::toLowerCase)
                .filter(s -> s.endsWith(FileUtil.ZIPFILE_EXTENSION))
                .isPresent();

//        return multipartFile != null
//                && multipartFile.getOriginalFilename() != null
//                && multipartFile.getOriginalFilename().toLowerCase().endsWith(FileUtil.ZIPFILE_EXTENSION);
    }

}
