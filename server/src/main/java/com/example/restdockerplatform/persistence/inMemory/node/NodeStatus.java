package com.example.restdockerplatform.persistence.inMemory.node;

public enum NodeStatus {

    ALIVE,       // after registering and ping
    DISCONNECTED // after unsuccessfull ping

}
