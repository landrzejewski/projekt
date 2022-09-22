package com.example.restdockerplatform;

import com.example.restdockerplatform.persistence.database.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ResTdockerPlatformApplication {

    @Autowired
    TaskRepository taskRepository;

    public static void main(String[] args) {
        SpringApplication.run(ResTdockerPlatformApplication.class, args);
    }

}
