package com.ragnaroft.utilities.spark_config.config;

import com.ragnaroft.utilities.spark_config.config.context.Application;
import com.ragnaroft.utilities.spark_config.route.health.HealthCheckRoute;

public class Main extends Application {

    public enum Profiles {PROD, STAGE, READONLY, DEVELOP}

    public static void main(final String[] args) throws Exception {
        new Main().init();
    }

    @Override
    public void addRoutes() {
        HealthCheckRoute.register();
    }

    @Override
    public void registerModules() {

    }

}
