package qcha.arfind.utils;

import javafx.stage.Screen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public final class Constants {
    private final static Logger logger = LoggerFactory.getLogger(Constants.class);

    private Constants() {
        throw new AssertionError("Don't create instance of constant class");
    }

    public static final class ConfigFileConstants {
        public static final String PATH_TO_PROPERTIES = "../../../config.properties";

        public static final String CONFIG_FILENAME;
        public static final String DEFAULT_CHARSET;
        public static final String DEFAULT_FIELD_DELIMITER;

        static {
            //init out file
            Properties props = new Properties();
            try(FileInputStream input = new FileInputStream(PATH_TO_PROPERTIES)) {
                props.load(input);
                CONFIG_FILENAME = props.getProperty("CONFIG_FILENAME");
                logger.debug("Constant CONFIG_FILENAME was initialized as {}", CONFIG_FILENAME);
                DEFAULT_CHARSET = props.getProperty("DEFAULT_CHARSET");
                logger.debug("Constant DEFAULT_CHARSET was initialized as {}", DEFAULT_CHARSET);
                DEFAULT_FIELD_DELIMITER = props.getProperty("DEFAULT_FIELD_DELIMITER");
                logger.debug("Constant DEFAULT_FIELD_DELIMITER was initialized as {}", DEFAULT_FIELD_DELIMITER);
            } catch (IOException e) {
                logger.error("Error while working with file {}, cause: {}.", PATH_TO_PROPERTIES, e);
                throw new RuntimeException("Error while working with file - " + PATH_TO_PROPERTIES, e);
            }
        }

        private ConfigFileConstants() {
            throw new AssertionError("Don't create instance of constant class");
        }
    }

    public static final class UserResolutionConstants {
        public static final double DEFAULT_USER_RESOLUTION_WIDTH = Screen.getPrimary().getVisualBounds().getWidth();
        public static final double DEFAULT_USER_RESOLUTION_HEIGHT = Screen.getPrimary().getVisualBounds().getHeight();

    }

}
