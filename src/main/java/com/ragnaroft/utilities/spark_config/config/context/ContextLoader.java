package com.ragnaroft.utilities.spark_config.config.context;

@FunctionalInterface
public interface ContextLoader {

    void registerModules();

}
