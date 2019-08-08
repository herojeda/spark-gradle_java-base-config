package com.ragnaroft.utilities.spark_config.config.environment;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author hojeda
 */
public class Profile {

    private String[] scopes;
    private String name;

    private Profile(String name) {
        this.name = name;
    }

    /**
     * Register the profile and load the scopes from the main.properties file (key: main.app.scopes.{profile_name})
     *
     */
    public static Profile build(String name) {
        Profile profile = new Profile(name);
        profile.scopes = loadScopes(name);
        return profile;
    }

    public List<String> getScopes() {
        return Arrays.asList(scopes);
    }

    public String getName() {
        return name;
    }

    private static String[] loadScopes(String key) {
        String propertyKey = Environment.SCOPES_SUFFIX_KEY + key;
        String scopes = Environment.getMainProperties().getProperty(propertyKey, null);
        return Objects.nonNull(scopes) ? scopes.split(Environment.SEPARATOR) : new String[0];
    }

}
