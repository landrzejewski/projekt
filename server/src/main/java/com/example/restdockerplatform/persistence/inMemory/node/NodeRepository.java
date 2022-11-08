package com.example.restdockerplatform.persistence.inMemory.node;

import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


@Repository
public class NodeRepository {

    private static final Map<String, NodeEntity> NODE_STATUS_MAP = new HashMap<>();

    public String insertNode(NodeEntity nodeEntity) {

        if (NODE_STATUS_MAP.containsKey(nodeEntity.getId())) {
            return null;
        }

        final NodeEntity inserted = NODE_STATUS_MAP.put(nodeEntity.getId(), nodeEntity);

        return inserted.getId();
    }


    public String updateNode(NodeEntity nodeEntity) {

        if (!NODE_STATUS_MAP.containsKey(nodeEntity.getId())) {
            return null;
        }

        final NodeEntity updated = NODE_STATUS_MAP.put(nodeEntity.getId(), nodeEntity);

        return updated.getId();
    }


    public Collection<NodeEntity> getAllNodes() {

        return NODE_STATUS_MAP.values();
    }

}
