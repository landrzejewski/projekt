package com.example.restdockerplatform.file;

import java.io.File;


public class UserFileService {

    /**
     * saves unzipped zip file in repository/branch (project/user) directory
     * @param project
     * @param branch
     * @param zipFile
     */
    public void saveFiles(String project, String branch, File zipFile) {
        // TODO unzip

    }


    /**
     * returns zip file of git repository branch (project/user) directory
     * @param project
     * @param branch
     * @return
     */
    public File getFiles(String project, String branch) {
        // TODO zip
        return null;
    }

}
