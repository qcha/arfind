package qcha.arfind.utils;

import javafx.stage.Screen;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Properties;

@Slf4j
public final class Constants {
    private static final String DEFAULT_PROPERTY_FILE = "config.properties";

    private Constants() {
        throw new AssertionError("Don't create instance of constant class");
    }

    public static final class ConfigFileConstants {
        public static final String CONFIG_FILENAME;
        public static final String DEFAULT_CHARSET;
        public static final String DEFAULT_FIELD_DELIMITER;

        static {
            //init out file
            Properties props = new Properties();
            try (FileInputStream input = new FileInputStream(DEFAULT_PROPERTY_FILE)) {
                props.load(input);

                CONFIG_FILENAME = props.getProperty("CONFIG_FILENAME");
                log.debug("Constant CONFIG_FILENAME was initialized as {}", CONFIG_FILENAME);

                DEFAULT_CHARSET = props.getProperty("DEFAULT_CHARSET");
                log.debug("Constant DEFAULT_CHARSET was initialized as {}", DEFAULT_CHARSET);

                DEFAULT_FIELD_DELIMITER = props.getProperty("DEFAULT_FIELD_DELIMITER");
                log.debug("Constant DEFAULT_FIELD_DELIMITER was initialized as {}", DEFAULT_FIELD_DELIMITER);

            } catch (IOException e) {
                log.error("Error while working with file {}, cause: {}.", DEFAULT_PROPERTY_FILE, e);
                throw new UncheckedIOException("Error while working with file - " + DEFAULT_PROPERTY_FILE, e);
            }
        }

        private ConfigFileConstants() {
            throw new AssertionError("Don't create instance of constant class");
        }
    }

    public static final class UserResolutionConstants {
        public static final double DEFAULT_USER_RESOLUTION_WIDTH = Screen.getPrimary().getVisualBounds().getWidth();
        public static final double DEFAULT_USER_RESOLUTION_HEIGHT = Screen.getPrimary().getVisualBounds().getHeight();

        private UserResolutionConstants() {
            throw new AssertionError("Don't create instance of constant class");
        }
    }

}
