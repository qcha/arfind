package qcha.arfind.utils;

import javafx.stage.Screen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Constants {
    private final static Logger logger = LoggerFactory.getLogger(Constants.class);

    private Constants() {
        logger.error("Try to create instance of constant class.");
        throw new AssertionError("Don't create instance of constant class");
    }

    public static final class ConfigFileConstants {
        public static final String CONFIG_FILENAME = "config.csv";
        public static final String DEFAULT_CHARSET = "UTF-8";
        public static final String DEFAULT_FIELD_DELIMITER = ";";

        private ConfigFileConstants() {
            logger.error("Try to create instance of constant class.");
            throw new AssertionError("Don't create instance of constant class");
        }
    }

    public static final class UserResolutionConstants {
        public static final double DEFAULT_USER_RESOLUTION_WIDTH = Screen.getPrimary().getVisualBounds().getWidth();
        public static final double DEFAULT_USER_RESOLUTION_HEIGHT = Screen.getPrimary().getVisualBounds().getHeight();

    }

}
