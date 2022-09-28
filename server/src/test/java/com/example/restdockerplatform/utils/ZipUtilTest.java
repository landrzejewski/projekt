package com.example.restdockerplatform.utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class ZipUtilTest {

    final String USER_FILE_SPACE = "src\\test\\resources\\user_file_space";
    final String ZIP_FILE_NAME = "\\test_test.zip";
    final String ZIP_FILE_SPACE = "src\\test\\resources\\zip_file_space";
    final String UNZIP_FILE_SPACE = "src\\test\\resources\\unzip_file_space";


    @Before
    public void before() {

        cleanDirectory(ZIP_FILE_SPACE);
        cleanDirectory(UNZIP_FILE_SPACE);

        prepareDirectory(ZIP_FILE_SPACE);
        prepareDirectory(UNZIP_FILE_SPACE);

    }

    @After
    public void after() {

        cleanDirectory(ZIP_FILE_SPACE);
        cleanDirectory(UNZIP_FILE_SPACE);
    }


    private void cleanDirectory(String directory) {

        final File file = new File(directory);

        if (file.exists() && file.isDirectory()) {

            boolean result = FileSystemUtils.deleteRecursively(file);
        }
    }


    private void prepareDirectory(String directory) {

        final File file = new File(directory);

        if (!file.exists()) {
            file.mkdir();
        }
    }


    @Test
    public void directoryContentTest() {

        List<String> files = ZipUtil.getFileNamesFromDirectory(new File(USER_FILE_SPACE));

        System.out.println(files);
        assertThat(files.size()).isEqualTo(4);
    }


    @Test
    public void directoryContentWithWalkTest() {

        List<String> files = ZipUtil.getFileNamesFromDirectoryWithWalk(USER_FILE_SPACE, Integer.MAX_VALUE);

        System.out.println(files);
        assertThat(files.size()).isEqualTo(4);
    }

    @Test
    public void zippingAndUnzippingFileTest() {

        // given

        // when
        try {
            ZipUtil.zipDirectory(new File(USER_FILE_SPACE), ZIP_FILE_SPACE + ZIP_FILE_NAME);
            ZipUtil.unzip(ZIP_FILE_SPACE + ZIP_FILE_NAME, UNZIP_FILE_SPACE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // then
        List<String> files = ZipUtil.getFileNamesFromDirectoryWithWalk(UNZIP_FILE_SPACE, Integer.MAX_VALUE);
        assertThat(files.size()).isEqualTo(4);
    }

}