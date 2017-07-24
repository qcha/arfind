package qcha.arfind;

import javafx.geometry.Insets;

final class Constants {
    private Constants() {
        throw new AssertionError("Don't create instance of constant class");
    }

    static final class ConfigFileConstants {
        static final String CONFIG_FILENAME = "config.csv";
        static final String DEFAULT_CHARSET = "UTF-8";
        static final String DEFAULT_FIELD_DELIMITER = ",";

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
        static final int DEFAULT_HGAP = 10;
        static final int DEFAULT_VGAP = 25;

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