package local.wspolnyprojekt.nodeagent.configuration;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = Environment.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class NodeInternetInterfaceTest {

    @Autowired
    private Environment environment;

    private NodeInternetInterface nodeInternetInterface;

    @BeforeAll
    void prepare() {
        nodeInternetInterface = new NodeInternetInterface(environment);
    }

    @Test
    void shouldReturnIpAsString() {
        assertThat(nodeInternetInterface.getIp()).isNotBlank();
    }

    @Test
    void shouldReturnCorrectPort() {
        assertThat(nodeInternetInterface.getPort()).isEqualTo(8000);
    }

}
