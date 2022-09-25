package com.example.restdockerplatform.node;


public interface NodeService {

    void registerNode(Node node);

    Node getNextNode(int iteration);
}
