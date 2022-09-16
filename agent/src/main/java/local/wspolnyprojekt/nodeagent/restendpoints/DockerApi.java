package local.wspolnyprojekt.nodeagent.restendpoints;

import local.wspolnyprojekt.nodeagent.common.RestEndpoints;
import local.wspolnyprojekt.nodeagent.docker.DockerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DockerApi {

    private final DockerService dockerService;

    @GetMapping(RestEndpoints.DOCKER_UP)
    void buildAndRun(@PathVariable String taskid) {
        dockerService.buildAndRun(taskid);
    }

    @GetMapping(RestEndpoints.DOCKER_DOWN)
    void down(@PathVariable String taskid) {
        dockerService.down(taskid);
    }

    @GetMapping(value = RestEndpoints.DOCKER_LOG, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    InputStreamResource getLog(@PathVariable String taskid) throws FileNotFoundException {
        return dockerService.getLog(taskid);
    }

    @GetMapping(RestEndpoints.DOCKER_CLEANUP)
    void cleanup(@PathVariable String taskid) {
        dockerService.cleanup(taskid);
    }

// TODO Docelowo wynieść do kontrolera odpowiedzialnego za usuwanie całych tasków
//    //@PostMapping("/{taskid}/delete")
//    @GetMapping("/{taskid}/delete")
//    void delete(@PathVariable String taskid) {
//        dockerController.delete(taskid);
//    }

}
