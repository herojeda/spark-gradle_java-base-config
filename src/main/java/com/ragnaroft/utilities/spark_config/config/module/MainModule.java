package com.ragnaroft.utilities.spark_config.config.module;

import com.google.inject.AbstractModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class MainModule extends AbstractModule {

    private static final Logger log = LoggerFactory.getLogger(MainModule.class);

    private static MainModule module;

    public static MainModule getInstance() {
        module = Objects.nonNull(module) ? module : new MainModule();
        return module;
    }

    @Override
    protected void configure() {

    }
}
