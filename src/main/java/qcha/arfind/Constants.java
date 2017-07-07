package qcha.arfind;

import java.io.File;

public class Constants {
    public static final class MainWindow {
        private MainWindow() {
            throw new AssertionError();
        }

        public static final String TITLE = "JavaFx App";
        public static final int WIDTH = 500;
        public static final int HEIGHT = 300;
    }

    public static final class FileWriterConstants {
        private FileWriterConstants() {
            throw new AssertionError();

        }

        public static final String LINE_SEPARATOR = System.lineSeparator();
        public static final File FILENAME = new File("config.csv");
    }
}
