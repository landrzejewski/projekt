package com.example.restdockerplatform.utils;


public final class FileUtil {

    public static String ZIPFILE_EXTENSION = ".zip";


    private FileUtil() {

    }


    public static String getFileCodeName(String part1, String part2) {
        return getFileCode(part1, part2) + ZIPFILE_EXTENSION;
    }

    private static String getFileCode(String part1, String part2) {
        return part1 + "__" + part2;
    }

}
