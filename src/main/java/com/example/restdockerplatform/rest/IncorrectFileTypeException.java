package com.example.restdockerplatform.rest;


import java.io.IOException;

public class IncorrectFileTypeException extends IOException {

//    public IncorrectFileTypeException() {
//        super();
//    }

    public IncorrectFileTypeException(String message) {
        super(message);
    }

}