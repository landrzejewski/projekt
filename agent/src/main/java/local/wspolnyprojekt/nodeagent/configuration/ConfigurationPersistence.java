package local.wspolnyprojekt.nodeagent.configuration;

import java.util.Optional;

public interface ConfigurationPersistence {
    void save(String key, String value);
    Optional<String> load(String key);
}
