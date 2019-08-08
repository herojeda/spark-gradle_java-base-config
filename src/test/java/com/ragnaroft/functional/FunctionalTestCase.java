package com.ragnaroft.functional;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.ragnaroft.utilities.spark_config.config.Main;
import com.ragnaroft.utilities.spark_config.config.context.Context;
import com.ragnaroft.utilities.spark_config.config.module.MainModule;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

public class FunctionalTestCase {

    private static boolean isInitialized = false;
    private Main main;

    @BeforeClass
    public static void initDB() {
    }

    @Before
    public void init() throws Exception {
        if (!isInitialized) {
            mockInjector();
            main = new Main();
            main.init();
            isInitialized = true;
        }
    }

    @After
    public void cleanUp() throws InterruptedException {
        main.destroy();
        Thread.sleep(500);
        isInitialized = false;
    }

    @AfterClass
    public static void clearUpDB(){
    }

    protected void mockInjector() {
        Injector injector = Guice.createInjector(MainModule.getInstance());
        Context.setInjector(injector);
    }
}
