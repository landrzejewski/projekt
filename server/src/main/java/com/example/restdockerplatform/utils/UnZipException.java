package com.example.restdockerplatform.utils;

import java.io.IOException;


public class UnZipException extends IOException {

    public UnZipException(IOException e) {

        super("Error unziping file", e);
    }

}
