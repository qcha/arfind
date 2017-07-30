package qcha.arfind.utils;

import javafx.geometry.Insets;
import javafx.stage.Screen;

public final class Constants {
    private Constants() {
        throw new AssertionError("Don't create instance of constant class");
    }

    public static final class ConfigFileConstants {
        public static final String CONFIG_FILENAME = "config.csv";
        static final String DEFAULT_CHARSET = "UTF-8";
        static final String DEFAULT_FIELD_DELIMITER = ";";

        private ConfigFileConstants() {
            throw new AssertionError("Don't create instance of constant class");
        }
    }

    public static final class HBoxConstants {
        public static final int DEFAULT_SPACING = 10;

        private HBoxConstants() {
            throw new AssertionError("Don't create instance of constant class");
        }
    }

    public static final class UserResolutionConstants {
        public static final double DEFAULT_USER_RESOLUTION_WIDTH = Screen.getPrimary().getVisualBounds().getWidth();
        public static final double DEFAULT_USER_RESOLUTION_HEIGHT = Screen.getPrimary().getVisualBounds().getHeight();
    }

    public static final class GridPaneConstants {
        public static final double DEFAULT_HGAP = 0.018 * Screen.getPrimary().getVisualBounds().getWidth();
        public static final double DEFAULT_VGAP = 0.042 * Screen.getPrimary().getVisualBounds().getHeight();

        private GridPaneConstants() {
            throw new AssertionError("Don't create instance of constant class");
        }
    }

    public static final class PaddingConstants {
        public static final Insets DEFAULT_PADDING = new Insets(25, 25, 25, 25);

        private PaddingConstants() {
            throw new AssertionError("Don't create instance of constant class");
        }
    }


}
