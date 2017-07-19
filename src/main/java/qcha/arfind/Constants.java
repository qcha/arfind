package qcha.arfind;


public final class Constants {
    private Constants() {
        throw new AssertionError("Don't create instance of constant class");
    }

    public static final class MainWindow {

        private MainWindow() {
            throw new AssertionError("Don't create instance of constant class");
        }
        public static final String TITLE = "JavaFx App";

    }
    public static final class ConfigFileUtils {
        private ConfigFileUtils() {
            throw new AssertionError("Don't create instance of constant class");
        }
        public static final String CONFIG_FILENAME = "config.csv";
        public static final String DEFAULT_CHARSET = "UTF-8";
    }
}
