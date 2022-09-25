package com.example.restdockerplatform.node;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Node {

    private String id;
    private String host;
    private String port;
}
