package com.example.restdockerplatform;

import com.example.restdockerplatform.task.Task;
//import com.example.restdockerplatform.task.TaskRepository;
//import com.example.restdockerplatform.task.TaskStatus;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ResTdockerPlatformApplication {

//    @Autowired
//    TaskRepository taskRepository;

    public static void main(String[] args) {
        SpringApplication.run(ResTdockerPlatformApplication.class, args);
    }

//    public void run(String ...args) {
//        List<Task> tasks = taskRepository.findAll();
//        tasks.forEach(System.out::println);
//
//        Task someTask = createTask();
//        taskRepository.save(someTask);
//
//        System.out.println("---------------------");
//        List<Task> tasks2 = taskRepository.findAll();
//        tasks2.forEach(System.out::println);
//    }


//    private Task createTask() {
//        return new Task("Joe Doe", "Moon Landing", TaskStatus.STARTED, "some result");
//    }

}
