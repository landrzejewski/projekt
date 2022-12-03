package local.wspolnyprojekt.nodeagent.configuration;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.FileSystemUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = NodeConfigurationProperties.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FileConfigurationPersistenceTest {

    @Autowired
    private NodeConfigurationProperties nodeConfigurationProperties;

    private FileConfigurationPersistence fileConfigurationPersistence;

    @BeforeAll
    void prepare() {
        try {
            Files.createDirectory(Path.of("tmp"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterAll
    void cleanup() {
        try {
            FileSystemUtils.deleteRecursively(Path.of("tmp"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    void init() {
        fileConfigurationPersistence = new FileConfigurationPersistence(nodeConfigurationProperties);
        fileConfigurationPersistence.init();
    }

    @Test
    void shouldCorrectlyRetrieveStoredProperty() {
        fileConfigurationPersistence.save("key", "value");
        assertThat(fileConfigurationPersistence.load("key").isPresent()).isTrue();
        assertThat(fileConfigurationPersistence.load("key").get()).isEqualTo("value");
    }

    @Test
    void shouldNotThrowExceptionWhenKeyIsNotStored() {
        fileConfigurationPersistence.save("key", "value");
        assertThat(fileConfigurationPersistence.load("unknownkey").isPresent()).isFalse();
    }

    @Test
    void shouldReturnUpdatedValue() {
        fileConfigurationPersistence.save("key", "firstvalue");
        assertThat(fileConfigurationPersistence.load("key").get()).isEqualTo("firstvalue");
        fileConfigurationPersistence.save("key", "updatedvalue");
        assertThat(fileConfigurationPersistence.load("key").get()).isEqualTo("updatedvalue");
    }
}
