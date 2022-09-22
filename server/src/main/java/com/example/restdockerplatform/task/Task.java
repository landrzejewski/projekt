package com.example.restdockerplatform.task;


import lombok.*;

//import javax.persistence.*;

//@Entity
//@Table(name = "tasks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Task {

//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    private int Id;

    private String userName;
    private String project;
    private TaskStatus status;
    private String result;

    public Task(String userName, String project, TaskStatus status, String result) {
        this.userName = userName;
        this.project = project;
        this.status = status;
        this.result = result;
    }
}
