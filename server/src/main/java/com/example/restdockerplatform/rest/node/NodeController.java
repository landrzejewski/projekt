package com.example.restdockerplatform.rest.node;


import local.wspolnyprojekt.nodeagentlib.dto.NodeRegistrationEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/api/node")
@AllArgsConstructor
public class NodeController {

    private final NodeService nodeService;


    /**
     * Updates node information
     *
     * @param id id
     * @return node id
     */
    @PutMapping("/{id}")
    String updateNode(
            @PathVariable final String id,
            @RequestBody final NodeRegistrationEntity nodeFromRequest
    ) {

        log.info(" -> updateNode, id = {}", id);

        return nodeService.updateNode(nodeFromRequest);
    }


    /**
     * Register node in server
     *
     * @param id id
     * @return node id
     */
    @PostMapping("/{id}")
    String registerNode(
            @PathVariable final String id,
            @RequestBody final NodeRegistrationEntity nodeFromRequest
    ) {

        log.info(" -> registerNode, id = {}", id);

        return nodeService.registerNode(nodeFromRequest);
    }

}
