package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class ConfigUtils {

    private ConfigUtils() {}

    public static Properties loadConfig(final String configFile) throws IOException {
        if (!Files.exists(Paths.get(configFile))) {
            throw new IOException(configFile + " not found.");
        }
        final Properties cfg = new Properties();
        try (InputStream inputStream = new FileInputStream(configFile)) {
            cfg.load(inputStream);
        }

        //hack in my username and password as variables

        final String originalValue = cfg.get("sasl.jaas.config").toString();
        final String configValue =
                originalValue
                .replace("API_KEY", System.getenv("CONFLUENT_KEY"))
                .replace("API_SECRET", System.getenv("CONFLUENT_SECRET"));
        cfg.put("sasl.jaas.config", configValue);

        return cfg;
    }
}

