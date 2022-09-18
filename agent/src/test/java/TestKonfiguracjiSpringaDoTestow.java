import local.wspolnyprojekt.nodeagent.AgentApplication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest(classes = AgentApplication.class)
class TestKonfiguracjiSpringaDoTestow {
    @Test
    @DisplayName("Sprawdzenie konfiguracji testów")
    void sprawdzenieKonfiguracjiTestowej() {
        assert (true);
    }
}
