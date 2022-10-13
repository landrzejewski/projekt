package com.example.restdockerplatform.rest;


import java.io.IOException;

public class IncorrectFileTypeException extends IOException {

    public IncorrectFileTypeException(String message) {
        super(message);
    }

}
