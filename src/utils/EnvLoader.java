/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author faustin
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
