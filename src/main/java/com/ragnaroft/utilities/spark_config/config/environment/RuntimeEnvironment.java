package com.ragnaroft.utilities.spark_config.config.environment;

import com.ragnaroft.utilities.spark_config.config.context.Context;
import com.ragnaroft.utilities.spark_config.util.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class RuntimeEnvironment {

    private static final Logger logger = LoggerFactory.getLogger(RuntimeEnvironment.class);
    private static final String APPLICATION = "APPLICATION";
    private static final String SCOPE = "SCOPE";
    private static final String DEV_SCOPE = "develop";

    private static RuntimeEnvironment runtimeEnvironment;
    private Profile profile;
    private String scope;
    private String application;

    public static RuntimeEnvironment get() {
        return Objects.nonNull(runtimeEnvironment) ? runtimeEnvironment : load();
    }

    private static RuntimeEnvironment load() {
        runtimeEnvironment = new RuntimeEnvironment();
        runtimeEnvironment.scope = SystemUtils.getEnv(SCOPE);
        runtimeEnvironment.application = SystemUtils.getEnv(APPLICATION);
        runtimeEnvironment.profile = Context.selectProfile(
                Objects.nonNull(runtimeEnvironment.getScope()) ? runtimeEnvironment.getScope().toLowerCase() : null
        );
        logger.info("Loading runtime enviroment: \n" +
                " Scope: " + runtimeEnvironment.scope +
                "\n Application: " + runtimeEnvironment.application +
                "\n Profile: " + runtimeEnvironment.profile.getName());
        return runtimeEnvironment;
    }

    public Profile getProfile() {
        return profile;
    }

    public String getScope() {
        return scope;
    }

    public String getApplication() {
        return application;
    }

    public Boolean is(String profileName) {
        return this.profile.getName().equals(profileName);
    }
}
