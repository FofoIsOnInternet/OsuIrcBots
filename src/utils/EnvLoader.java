package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author fofoisoninternet
 */
public class EnvLoader {
    private static final Properties properties = new Properties();

    static {
        try (FileInputStream fileInputStream = new FileInputStream(".env")) {
            properties.load(fileInputStream);
        } catch (IOException e) {
            System.err.println("Warning: .env file not found!");
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }
}
