package qcha.arfind;


public final class Constants {
    private static final String DEFAULT_ERROR_MESSAGE = "Can't create instance of constant class";

    private Constants() {
        throw new AssertionError(DEFAULT_ERROR_MESSAGE);
    }

    public static final class MainWindow {
        private MainWindow() {
            throw new AssertionError(DEFAULT_ERROR_MESSAGE);
        }

        public static final String TITLE = "JavaFx App";
        public static final int DEFAULT_WIDTH = 640;
        public static final int DEFAULT_HEIGHT = 480;
    }

    public static final class ConfigurationWindow {
        private ConfigurationWindow() {
            throw new AssertionError(DEFAULT_ERROR_MESSAGE);
        }

        public static final int DEFAULT_WIDTH = 600;
        public static final int DEFAULT_HEIGHT = 400;
    }

    public static final class FileWriterConstants {
        private FileWriterConstants() {
            throw new AssertionError(DEFAULT_ERROR_MESSAGE);

        }

        public static final String DEFAULT_CHARSET = "UTF-8";
        public static final String LINE_SEPARATOR = System.lineSeparator();
        public static final String FILENAME = "config.csv";
    }
}
