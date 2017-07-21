package qcha.arfind;

final class Constants {
    private Constants() {
        throw new AssertionError("Don't create instance of constant class");
    }

    static final class MainWindow {
        private MainWindow() {
            throw new AssertionError("Don't create instance of constant class");
        }
        static final String TITLE = "JavaFx App";
    }

    static final class ConfigFileConstants {
        private ConfigFileConstants() {
            throw new AssertionError("Don't create instance of constant class");
        }
        static final String CONFIG_FILENAME = "config.csv";
        static final String DEFAULT_CHARSET = "UTF-8";
    }
}
