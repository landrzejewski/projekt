package local.wspolnyprojekt.nodeagent.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.Optional;
import java.util.Properties;

@Component
@RequiredArgsConstructor
@ApplicationScope
public class FileConfigurationPersistence implements ConfigurationPersistence {

    private final NodeConfigurationProperties nodeConfigurationProperties;
    private Properties configurationProperties;
    private File configurationFile;

    @Override
    public void save(String key, String value) {
        synchronized (configurationFile) {
            configurationProperties.put(key, value);
            try (FileOutputStream fileOutputStream = new FileOutputStream(configurationFile)) {
                configurationProperties.store(fileOutputStream, "Agent persistence");
            } catch (FileNotFoundException e) {
                // TODO
            } catch (IOException e) {
                // TODO
            }
        }
    }

    @Override
    public Optional<String> load(String key) {
        return Optional.ofNullable(configurationProperties.getProperty(key));
    }

    @PostConstruct
    void init() {
        configurationFile = new File(nodeConfigurationProperties.getConfigurationPersistenceFileName());
        configurationProperties = new Properties();
        try {
            new File(nodeConfigurationProperties.getWorkspaceDirectory()).mkdirs(); // Utworzenie folderu workspace jeśli nie istnieje
            if (!configurationFile.createNewFile()) {
                try (FileInputStream inputStream = new FileInputStream(configurationFile)) {
                    configurationProperties.load(inputStream);
                } catch (FileNotFoundException e) {
                    // TODO Obsługa błędu
                }
            }
        } catch (IOException e) {
            // TODO Obsługa błędu
        }
    }
}
