package com.ragnaroft.utilities.spark_config.route.health;

import com.google.common.net.HttpHeaders;
import com.google.common.net.MediaType;
import spark.*;

import javax.inject.Singleton;

/**
 * The basic configuration that implements a simple health check endpoint.
 * <p>
 * Clients can verify the endpoint is alive by sending a GET request to ./ping.
 */
public class HealthCheckRoute implements RouteGroup {

    private HealthCheckRoute() {
    }

    private String handle(Request request, Response response) {
        response.header(HttpHeaders.CONTENT_TYPE, MediaType.PLAIN_TEXT_UTF_8.toString());
        return "pong";
    }

    @Override
    public void addRoutes() {
        Spark.get("/ping", this::handle);
    }

    public static void register() {
        new HealthCheckRoute().addRoutes();
    }
}
