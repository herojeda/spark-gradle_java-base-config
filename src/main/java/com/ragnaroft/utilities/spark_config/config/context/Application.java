package com.ragnaroft.utilities.spark_config.config.context;

import com.ragnaroft.utilities.spark_config.config.environment.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import spark.RouteGroup;
import spark.servlet.SparkApplication;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

import static spark.Spark.*;

public abstract class Application implements SparkApplication, RouteGroup, ContextLoader {

    /**
     * Overloadable Configurations
     */
    public static final String SERVER_PORT = "server.port";
    public static final String SERVER_ADDRESS = "server.address";
    public static final String SERVER_BASE_THREADS = "server.base.threads";
    public static final String SERVER_MAX_CORE_MULTIPLIER = "server.max.core.multiplier";
    public static final String SERVER_MIN_CORE_MULTIPLIER = "server.min.core.multiplier";
    public static final String SERVER_REQUEST_TIMEOUT = "server.request.timeout";
    public static final String APPLICATION_PATH = "application.path";

    /**
     * MDC key to identify a request
     */
    public static final String REQUEST_ID = "request_id";

    private static final Logger logger = LoggerFactory.getLogger(Application.class);


    /**
     * Injector name to add to Config.addInjector()
     */
    public static final String APP = "app";


    private int port;
    private String address;
    private int baseThreads;
    private int maxMultiplier;
    private int minMultiplier;
    private int timeout;
    private String basePath;

    /**
     * Create an application with default configuration.
     */
    public Application() {
        try {
            initializeWithDefaults();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Initiate the application with the configuration and dependency injection module loaded.
     */
    @Override
    public void init() {
        logger.info("Waiting for initialization...");
        initializeContext();
        configureServer();
        setupRoutes();
        awaitInitialization();
        logger.info("READY");
    }

    /**
     * Destroy the application
     */
    @Override
    public void destroy() {
        stop();
    }

    /**
     * Configure the embedded jetty server to start.
     */
    protected void configureServer() {
        final int processors = Runtime.getRuntime().availableProcessors();
        final int maxThreads = processors * maxMultiplier + baseThreads;
        final int minThreads = processors * minMultiplier + baseThreads;

        port(port);
        ipAddress(address);
        threadPool(maxThreads, minThreads, timeout);

        logger.info("Listening in{}:{} using thread pool: [min:{} | max:{} | timeout:{}]", address, port, minThreads, maxThreads, timeout);
    }

    /**
     * Load the route groups.
     */
    protected void setupRoutes() {
        addRoutes();
        before((request, response) -> putRequestId());
        after((request, response) -> MDC.clear());
    }

    /**
     * Set the request id in the MDC.
     */
    private static void putRequestId() {
        MDC.put(REQUEST_ID, UUID.randomUUID().toString());
    }

    /**
     * @return the request id previously set in the MDC.
     */
    public static String getRequestId() {
        return MDC.get(REQUEST_ID);
    }

    /**
     * Port application.
     *
     * @param port
     * @return
     */
    public Application withPort(int port) {
        this.port = port;
        return this;
    }

    /**
     * Address to use to bind the process.
     *
     * @param address
     * @return
     */
    public Application withAddress(String address) {
        this.address = address;
        return this;
    }

    /**
     * Base threads to start the application.
     *
     * @param baseThreads
     * @return
     */
    public Application withBaseThreads(int baseThreads) {
        this.baseThreads = baseThreads;
        return this;
    }

    /**
     * Multiplier to use with the cores machine to calculate the max thread.
     *
     * @param maxMultiplier
     * @return
     */
    public Application withMaxMultiplier(int maxMultiplier) {
        this.maxMultiplier = maxMultiplier;
        return this;
    }

    /**
     * Multiplier to use with the cores machine to calculate the min thread.
     *
     * @param minMultiplier
     * @return
     */
    public Application withMinMultiplier(int minMultiplier) {
        this.minMultiplier = minMultiplier;
        return this;
    }

    /**
     * Max time to respond the client.
     *
     * @param timeout
     * @return
     */
    public Application withTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }


    /**
     * The base path to load all the routes.
     *
     * @param basePath
     * @return
     */
    public Application withBasePath(String basePath) {
        this.basePath = basePath;
        return this;
    }

    /**
     * Initialize application configuration fields with defaults.
     *
     * @throws UnknownHostException
     */
    protected void initializeWithDefaults() throws UnknownHostException {
        final String ipAddress = InetAddress.getLocalHost().getHostAddress();
        address = Environment.getString(SERVER_ADDRESS, ipAddress);
        port = Environment.getInt(SERVER_PORT, 8080);
        baseThreads = Environment.getInt(SERVER_BASE_THREADS, 3);
        maxMultiplier = Environment.getInt(SERVER_MAX_CORE_MULTIPLIER, 2);
        minMultiplier = Environment.getInt(SERVER_MIN_CORE_MULTIPLIER, 1);
        timeout = Environment.getInt(SERVER_REQUEST_TIMEOUT, 25000);
        basePath = Environment.getString(APPLICATION_PATH, "/");
    }

    protected void initializeContext() {
        Context.registerProfiles();
        registerModules();
        Context.init();
    }
}
