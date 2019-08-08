package com.ragnaroft.utilities.spark_config.config.environment;

import com.ragnaroft.utilities.spark_config.util.PropertyUtils;
import com.ragnaroft.utilities.spark_config.util.SystemUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;
import java.util.Properties;

public class Environment {

    private static final Logger logger = LoggerFactory.getLogger(Environment.class);
    public static final String PROPERTY_EXT = ".properties";
    public static final String MAIN_PROPERTY = "main";
    public static final String SOURCES_KEY = "main.app.property.source.names";
    public static final String SCOPES_SUFFIX_KEY = "main.app.scopes.";
    public static final String SEPARATOR = ",";

    private static Properties properties;
    private static Properties mainProperties;

    public static String getString(String key) {
        return get().getProperty(key);
    }

    public static Long getLong(String key) {
        String val = get().getProperty(key);
        return Objects.nonNull(val) ? Long.valueOf(val) : null;
    }

    public static Integer getInt(String key) {
        String val = get().getProperty(key);
        return Objects.nonNull(val) ? Integer.valueOf(val) : null;
    }

    public static Boolean getBoolean(String key) {
        return Boolean.parseBoolean(get().getProperty(key));
    }

    public static String getString(String key, String defaultValue) {
        return get().getProperty(key, defaultValue);
    }

    public static Long getLong(String key, Long defaultValue) {
        return Long.valueOf(get().getProperty(key, Objects.nonNull(defaultValue) ? defaultValue.toString() : null));
    }

    public static Integer getInt(String key, Integer defaultValue) {
        return Integer.valueOf(get().getProperty(key, Objects.nonNull(defaultValue) ? defaultValue.toString() : null));
    }

    public static Boolean getBoolean(String key, Boolean defaultValue) {
        String val = get().getProperty(key);
        return Objects.isNull(val) ? defaultValue : Boolean.parseBoolean(val);
    }

    public static String getValueFromSystem(String key) {
        String val = get().getProperty(key);
        if (val != null) {
            return SystemUtils.getEnv(val);
        } else {
            String msg = "Can't find value for " + key + " key.";
            logger.error(msg);
            throw new RuntimeException(msg);
        }
    }

    public static String getValueFromSystem(String key, String defaultValue) {
        String val = get().getProperty(key);
        if (val != null) {
            return SystemUtils.getEnv(val);
        } else {
           return defaultValue;
        }
    }

    public static Properties getMainProperties() {
        return getMain();
    }

    private static Properties loadProperties() {
        properties = new Properties();
        Profile profile = RuntimeEnvironment.get().getProfile();
        Arrays.stream(PropertyUtils.getProperties(MAIN_PROPERTY + PROPERTY_EXT)
                              .getProperty(SOURCES_KEY)
                              .split(SEPARATOR))
                .filter(s -> StringUtils.isNotBlank(s))
                .forEach(fileName -> PropertyUtils.load(properties, profile.getName() + File.separator + fileName + PROPERTY_EXT));
        return properties;
    }

    private static Properties get() {
        return Objects.nonNull(properties) ? properties : loadProperties();
    }

    private static Properties loadMainProperties() {
        mainProperties = PropertyUtils.getProperties(Environment.MAIN_PROPERTY + Environment.PROPERTY_EXT);
        return mainProperties;
    }

    private static Properties getMain() {
        return Objects.nonNull(mainProperties) ? mainProperties : loadMainProperties();
    }

}
