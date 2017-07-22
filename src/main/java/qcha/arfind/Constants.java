package qcha.arfind;

import javafx.geometry.Insets;
import javafx.scene.paint.Color;

final class Constants {
    private Constants() {
        throw new AssertionError("Don't create instance of constant class");
    }

    static final class MainWindow {
        static final String TITLE = "JavaFx App";
        static final int DEFAULT_WIDTH = 640;
        static final int DEFAULT_HEIGHT = 480;

        private MainWindow() {
            throw new AssertionError("Don't create instance of constant class");
        }
    }

    static final class ConfigFileConstants {
        static final String CONFIG_FILENAME = "config.csv";
        static final String DEFAULT_CHARSET = "UTF-8";

        private ConfigFileConstants() {
            throw new AssertionError("Don't create instance of constant class");
        }
    }

    static final class ConfigurationWindow {
        static final String TITLE = "Настройки конфигурации";
        static final int DEFAULT_WIDTH = 600;
        static final int DEFAULT_HEIGHT = 400;

        private ConfigurationWindow() {
            throw new AssertionError("Don't create instance of constant class");
        }
    }

    static final class DialogWindowConstants {
        static final String TITLE = "Editing company";
        static final int DEFAULT_WIDTH = 400;
        static final int DEFAULT_HEIGHT = 250;

        private DialogWindowConstants() {
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
        static final int DEFAULT_VGAP = 15;

        private GridPaneConstants() {
            throw new AssertionError("Don't create instance of constant class");
        }
    }

    static final class LabelConstants {
        static final Color DEFAULT_TEXTFILL = Color.rgb(210, 39, 30);
        static final int DEFAULT_WIDTH = 640;
        static final int DEFAULT_HEIGHT = 35;

        private LabelConstants() {
            throw new AssertionError("Don't create instance of constant class");
        }
    }

    static final class InsetsConstants {
        static final Insets DEFAULT_PADDING = new Insets(25, 25, 25, 25);
        private InsetsConstants() {
            throw new AssertionError("Don't create instance of constant class");
        }
    }


}
