package qcha.arfind;

import java.io.File;

public final class Constants {
    private Constants() {
        throw new AssertionError("Can't create instance of constant class");
    }

    public static final class MainWindow {
        private MainWindow() {
            throw new AssertionError("Can't create instance of constant class");
        }

        public static final String TITLE = "JavaFx App";
        public static final int DEFAULT_WIDTH = 640;
        public static final int DEFAULT_HEIGHT = 480;
    }

    public static final class FileWriterConstants {
        private FileWriterConstants() {
            throw new AssertionError("Can't create instance of constant class");

        }

        public static final String LINE_SEPARATOR = System.lineSeparator();
        //todo for timuranosov - fix it!
        public static final File FILENAME = new File("config.csv");
    }
}
