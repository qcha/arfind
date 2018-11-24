package qcha.arfind.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import qcha.arfind.Sources;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static qcha.arfind.utils.Constants.ConfigFileConstants.CONFIG_FILENAME;
import static qcha.arfind.utils.Constants.UserResolutionConstants.DEFAULT_USER_RESOLUTION_HEIGHT;
import static qcha.arfind.utils.Constants.UserResolutionConstants.DEFAULT_USER_RESOLUTION_WIDTH;

@Slf4j
public final class MainApplication extends Application {
    private final String TITLE = "Поиск артикулов";

    //this window size occupies 70% of user's display width and 90% of display height
    private final double DEFAULT_WIDTH = 0.4 * DEFAULT_USER_RESOLUTION_WIDTH;
    private final double DEFAULT_HEIGHT = 0.5 * DEFAULT_USER_RESOLUTION_HEIGHT;

    private Scene mainScene;
    private SearchView searchView;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (!Sources.getOrCreate().isEmpty()) {
                log.debug("Saving cache to file {}.", CONFIG_FILENAME);
                Sources.saveCacheToFile();
                log.info("Cache was successfully saved to file {}.", CONFIG_FILENAME);
            } else {
                try {
                    log.debug("Trying to delete file {}.", CONFIG_FILENAME);
                    Files.deleteIfExists(Paths.get(CONFIG_FILENAME));
                    log.info("File {} was successfully deleted.", CONFIG_FILENAME);
                } catch (IOException e) {
                    log.error("Cannot find file {}, cause: {}.", CONFIG_FILENAME, e);
                    throw new UncheckedIOException("Cannot find file - " + CONFIG_FILENAME, e);
                }
            }
        }));

        //init search view
        searchView = new SearchView(new SearchViewModel(primaryStage));

        mainScene = new Scene(
                searchView,
                DEFAULT_WIDTH,
                DEFAULT_HEIGHT
        );

        primaryStage.setScene(mainScene);
        primaryStage.setTitle(TITLE);
        primaryStage.show();
    }
}