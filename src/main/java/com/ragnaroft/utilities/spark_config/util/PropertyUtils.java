package com.ragnaroft.utilities.spark_config.util;

import com.ragnaroft.utilities.spark_config.util.exception.FileLocationNonexistentException;
import com.ragnaroft.utilities.spark_config.util.exception.LoadingFileException;
import com.ragnaroft.utilities.spark_config.util.exception.NoLocationsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

public class PropertyUtils {

    private static final Logger logger = LoggerFactory.getLogger(PropertyUtils.class);

    public static Properties getProperties(String fileLocation) {
        Properties properties = new Properties();
        load(properties, fileLocation);
        return properties;
    }

    public static Properties getProperties(List<String> locations) {
        if (Objects.nonNull(locations) && !locations.isEmpty()) {
            Properties properties = new Properties();
            locations.stream().forEach(
                    fileLocation -> {
                        load(properties, fileLocation);
                    }
            );
            return properties;
        } else {
            String msg = "The parameter 'locations' can't be null or empty.";
            logger.error(msg);
            throw new NoLocationsException();
        }
    }

    public static void load(Properties properties, String fileLocation) {
        try (InputStream in = PropertyUtils.class.getClassLoader().getResourceAsStream(fileLocation)) {
            logger.info("Loading properties from: " + fileLocation);
            if (in == null) {
                String msg = "Unable to find file: " + fileLocation;
                logger.error(msg);
                throw new FileLocationNonexistentException(fileLocation);
            }
            properties.load(in);
        } catch (IOException ex) {
            String msg = "Error loading file: " + fileLocation;
            logger.error(msg, ex);
            throw new LoadingFileException(fileLocation, ex);
        }
    }

}
