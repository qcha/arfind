package qcha.arfind;

import javafx.geometry.Insets;

public final class Constants {
    private Constants() {
        throw new AssertionError("Don't create instance of constant class");
    }

    public static final class ConfigFileConstants {
        public static final String CONFIG_FILENAME = "config.csv";
        public static final String DEFAULT_CHARSET = "UTF-8";
        public static final String DEFAULT_FIELD_DELIMITER = ";";

        private ConfigFileConstants() {
            throw new AssertionError("Don't create instance of constant class");
        }
    }

    static final class HBoxConstants {
        static final int DEFAULT_SPACING = 10;

        private HBoxConstants() {
            throw new AssertionError("Don't create instance of constant class");
        }
    }

    static final class GridPaneConstants {
        static final int DEFAULT_HGAP = 35;
        static final int DEFAULT_VGAP = 50;

        private GridPaneConstants() {
            throw new AssertionError("Don't create instance of constant class");
        }
    }

    static final class PaddingConstants {
        static final Insets DEFAULT_PADDING = new Insets(25, 25, 25, 25);

        private PaddingConstants() {
            throw new AssertionError("Don't create instance of constant class");
        }
    }


}
