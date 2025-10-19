package com.api.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * GlobalConfiguration is a utility class responsible for:
 * - Loading application configuration from `config.properties`
 * - Providing access to property values using keys
 * - Ensuring properties are loaded only once (lazy loading)
 */
public class GlobalConfiguration {
    // Flag to indicate whether the properties have been loaded
    private static boolean isInitialized = false;
    // Name of the properties file in the classpath (in src/test/resources )
    private static final String envPropertyFile = "config.properties";
    // Java Properties object to hold key-value pairs from the file
    private static final Properties properties = new Properties();

    /**
     * Retrieves the value of the specified property key from the loaded configuration.
     * Automatically loads the properties file if not already loaded.
     *
     * @param key the name of the property to retrieve
     * @return the value of the property, or null if not found
     */
    public static String getProperty(String key){
        if(!isInitialized){
            loadProperties();
        }
        return properties.getProperty("BaseURL");
    }
    /**
     * Loads the configuration from the properties file in the classpath.
     * If the file is missing or fails to load, throws a runtime exception.
     */
    public static void loadProperties() {
        try {
            InputStream input = GlobalConfiguration.class.getClassLoader().getResourceAsStream(envPropertyFile) ;
            if (input == null) {
                    throw new RuntimeException("Unable to find config.properties");
            }
            properties.load(input);
            isInitialized = true;
        } catch (IOException ex) {
            throw new RuntimeException("Failed to load properties file", ex);
        }

    }
}
