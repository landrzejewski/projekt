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
    @Column
    private String Id;

    @Column
    private String nodeUUId;

    @Column
    private String username;

    @Column
    private String project;

    @Column
    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @Column
    private byte[] bytesResult;

    @Column
    private String textResult;

    @Column
    private LocalDateTime startDateTime;

    @Column
    private LocalDateTime endDateTime;

    //TODO: zmiana statusu na start -> set startTime, Zakonczony -> set endTime

}
