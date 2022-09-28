package com.example.restdockerplatform.event;


import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class SaveUserTaskEvent {

    private String user;
    private String project;

}
