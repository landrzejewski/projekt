package com.example.restdockerplatform.persistence.database;


import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Task {

    @Id
    @Column(columnDefinition = "varchar(255)")
    private String Id;

    @Column(columnDefinition = "varchar(255)")
    private String nodeUUId;

    @Column(columnDefinition = "varchar(255)")
    private String userName;

    @Column(columnDefinition = "varchar(255)")
    private String project;

    @Column(columnDefinition = "varchar(50)")
    private TaskStatus status;

    @Column(columnDefinition = "BLOB" )
    private byte[] bytesResult;

    @Column(columnDefinition = "text")
    private String textResult;

    @Column
    private LocalDateTime startDateTime;

    @Column
    private LocalDateTime endDateTime;

    //TODO: zmiana statusu na start -> set startTime, Zakonczony -> set endTime

}
