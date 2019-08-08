package com.ragnaroft.utilities.spark_config.config.context;

import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;
import com.ragnaroft.utilities.spark_config.config.environment.Environment;
import com.ragnaroft.utilities.spark_config.config.environment.Profile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 *
 * This class contains methods to manage the application context depending on which profile is loaded.
 *
 * @author hojeda
 *
 */
public class Context {

    private static final Logger logger = LoggerFactory.getLogger(Context.class);
    private static final String PROFILES_KEY = "main.app.profiles";
    private static final Profile DEFAULT_PROFILE = Profile.build("develop");

    public static Injector injector;
    private static List<AbstractModule> modules;
    private static List<Profile> profiles;

    public static void init() {

        if (Objects.isNull(injector)) {
            injector = Guice.createInjector(modules);
        }
    }

    public static void setInjector(Injector newInjector) {
        injector = newInjector;
    }

    public static Injector getInjector() { return injector; }
    /**
     * Use this function to register modules in your application.
     *
     * @param module -> This is the module to register in the context.
     */
    public static void registerModule(AbstractModule module) {
        modules = Objects.nonNull(modules) ? modules : Lists.newArrayList();
        modules.add(module);
    }

    /**
     * @param clazz -> The type to look for in the context.
     * @param <T> -> Represent the type to return in generic mode.
     * @return -> An object associated with the {@clazz} type from the context.
     */
    public static <T> T getInstance(Class<T> clazz) {
        return injector.getInstance(clazz);
    }

    /**
     * @param clazz -> The type to look for in the context.
     * @param name -> The name associated to the implementation.
     * @param <T> -> Represent the type to return in generic mode.
     * @return -> An object associated with the {@clazz} type and named with {@named} from the context.
     */
    public static <T> T getInstance(Class<T> clazz, String name) {
        return injector.getInstance(Key.get(clazz, Names.named(name)));
    }

    /**
     * @param scope -> This is the name sought to select the profile.
     *              The @scope must be configured in the corresponding profile of the main.properties file
     * @return The selected {@link Profile}
     */
    public static final Profile selectProfile(final String scope) {
        registerProfiles();
        if (Objects.nonNull(scope)) {
            Optional<Profile> profileOpt = profiles.stream()
                    .filter(profile -> profile.getScopes().contains(scope))
                    .findFirst();
            return profileOpt.orElse(DEFAULT_PROFILE);
        } else {
            return DEFAULT_PROFILE;
        }
    }

    public static void registerProfiles() {
        if (Objects.isNull(profiles)) {
            profiles = Lists.newArrayList();
            String configProfiles = Environment.getMainProperties().getProperty(PROFILES_KEY);
            if (Objects.nonNull(configProfiles)) {
                Arrays.stream(configProfiles.split(Environment.SEPARATOR))
                        .forEach( profileName -> profiles.add(Profile.build(profileName)));
            }
        }
    }
}
