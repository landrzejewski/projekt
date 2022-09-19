package com.example.restdockerplatform.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;


@Slf4j
public final class ZipUtil {

    private static final int ZIP_FRAME_SIZE = 1024;

    private ZipUtil() {

    }

    /**
     * unzips file to specified directory
     *
     * @param zipFilePath
     * @param destinationDirectory
     */
    public static void unzip(String zipFilePath, String destinationDirectory) {

        log.info(" -> unzip, zipFilePath = {}, destinationDirectory = {}", zipFilePath, destinationDirectory);
        File dir = new File(destinationDirectory);

        // create output directory if it doesn't exist
        if (!dir.exists()) {
            dir.mkdirs();
        }


        // buffer for read and write data to file
        byte[] buffer = new byte[ZIP_FRAME_SIZE];

        try (
                FileInputStream fis = new FileInputStream(zipFilePath);
                ZipInputStream zis = new ZipInputStream(fis)
        ) {

            ZipEntry ze = zis.getNextEntry();

            while (ze != null) {

                final String fileName = ze.getName();
                final File newFile = new File(destinationDirectory + File.separator + fileName);

                log.info(" -- unzip, unzipping file to: {}", newFile.getAbsolutePath());

                // create directories for sub directories in zip
                new File(newFile.getParent()).mkdirs();

                final FileOutputStream fos = new FileOutputStream(newFile);

                boolean hasAnySize = false;

                int len;
                while ((len = zis.read(buffer)) > 0) {

                    hasAnySize = true;
                    fos.write(buffer, 0, len);
                }

                fos.close();

                // close this ZipEntry
                zis.closeEntry();


                // delete directories and empty files
                if (newFile.exists() && (newFile.isDirectory() || !hasAnySize)) {
                    log.info(" -- unzip, deleting empty file or directory: {}", newFile.getAbsolutePath());
                    newFile.delete();
                }

                ze = zis.getNextEntry();
            }

            // close last ZipEntry
            zis.closeEntry();

        } catch (IOException e) {
            e.printStackTrace();
            log.error(" -- unzip, ERROR unziping file: " + e.getMessage());
        }
        log.info(" <- unzip ");
    }


    /**
     * zips the directory to specified zip file
     *
     * @param directory
     * @param zipFileName
     */
    public static void zipDirectory(File directory, String zipFileName) throws IOException {

        log.info(" -> zipDirectory, directory = {}, zipFileName = {}", directory.getAbsolutePath(), zipFileName);

//            final List<String> filesInDirectory = getFileNamesFromDirectory(directory);
        final List<String> filesInDirectory = getFileNamesFromDirectoryWithWalk(directory.getPath(), Integer.MAX_VALUE);

        if (filesInDirectory.isEmpty()) {
            throw new IOException("no files to return");
        }

        try (
                // create ZipOutputStream to write to the zip file
                FileOutputStream fos = new FileOutputStream(zipFileName);
                ZipOutputStream zos = new ZipOutputStream(fos)
        ) {
            for (String filePath : filesInDirectory) {
                log.info(" -- zipDirectory, zipping file: " + filePath);

                // only relative file path required for ZipEntry -> substring on absolute path
                final String relativePath = filePath.substring(directory.getAbsolutePath().length() + 1);

                ZipEntry ze = new ZipEntry(relativePath);
                zos.putNextEntry(ze);

                // read file and write to ZipOutputStream
                FileInputStream fis = new FileInputStream(filePath);
                byte[] buffer = new byte[ZIP_FRAME_SIZE];
                int len;
                while ((len = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, len);
                }

                zos.closeEntry();
                fis.close();
            }

        } catch (IOException e) {
            log.error(" -- zipDirectory, ERROR ziping files: " + e.getMessage());
            throw e;
        }

        log.info(" <- zipDirectory ");
    }

    /**
     * gets all file names from specified directory
     *
     * @param directory
     */
    public static List<String> getFileNamesFromDirectory(File directory) {

        final List<String> fileNames = new ArrayList<>();

        populateFileNames(fileNames, directory);

        return fileNames;
    }


    private static void populateFileNames(List<String> fileNames, File directory) {

        final File[] files = directory.listFiles();

        for (File file : files) {
            if (file.isFile()) {
                fileNames.add(file.getAbsolutePath());
            }

            if (file.isDirectory()) {
                populateFileNames(fileNames, file);
            }
        }
    }


    /**
     * gets all file names from specified directory with specified depth
     *
     * @param directory
     * @param depth
     */
    public static List<String> getFileNamesFromDirectoryWithWalk(String directory, int depth) {

        try (Stream<Path> stream = Files.walk(Paths.get(directory), depth)) {
            return stream
                    .filter(file -> !Files.isDirectory(file))
//                    .map(Path::getFileName)
                    .map(Path::toAbsolutePath)
                    .map(Path::toString)
                    .collect(Collectors.toList());

        } catch (IOException e) {

            // TODO proper exception handling
            log.error(" -- getFileNamesFromDirectoryWithWalk, ERROR: {} \n{}", e.getMessage(), e.toString());
            return Collections.emptyList();
        }

    }


}
