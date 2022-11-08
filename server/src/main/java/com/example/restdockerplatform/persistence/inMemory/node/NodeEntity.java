package com.example.restdockerplatform.persistence.inMemory.node;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Builder
@Setter
@Getter
public class NodeEntity {

    String id;
    String host;
    int port;
    NodeStatus status;
    LocalDateTime lastUpdate;

}
