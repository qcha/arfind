package qcha.arfind.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import qcha.arfind.Sources;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static qcha.arfind.utils.Constants.ConfigFileConstants.CONFIG_FILENAME;
import static qcha.arfind.utils.Constants.UserResolutionConstants.DEFAULT_USER_RESOLUTION_HEIGHT;
import static qcha.arfind.utils.Constants.UserResolutionConstants.DEFAULT_USER_RESOLUTION_WIDTH;

public class MainApplication extends Application {
    private final static Logger logger = LoggerFactory.getLogger(MainApplication.class);
    private final String TITLE = "Поисковик артикулов";

    //this window size occupies 70% of user's display width and 90% of display height
    private final double DEFAULT_WIDTH = 0.7 * DEFAULT_USER_RESOLUTION_WIDTH;
    private final double DEFAULT_HEIGHT = 0.9 * DEFAULT_USER_RESOLUTION_HEIGHT;
    private Scene mainScene;
    private SearchView searchView;


    @Override
    public void start(Stage primaryStage) throws Exception {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (!Sources.getOrCreate().isEmpty()) {
                logger.debug("Saving cache to file {}.", CONFIG_FILENAME);
                Sources.saveCacheToFile();
                logger.info("Cache was successfully saved to file {}.", CONFIG_FILENAME);
            } else {
                try {
                    logger.debug("Trying to delete file {}.", CONFIG_FILENAME);
                    Files.deleteIfExists(Paths.get(CONFIG_FILENAME));
                    logger.info("File {} was successfully deleted.", CONFIG_FILENAME);
                } catch (IOException e) {
                    //todo replace error message
                    logger.error("Cannot find file {}, cause: {}.", CONFIG_FILENAME, e);
                    throw new RuntimeException("Cannot find file - config.csv", e);
                }
            }
        }));

        ConfigurationWarningWindow configurationWarningWindow = new ConfigurationWarningWindow(primaryStage);
        if (!Files.exists(Paths.get(CONFIG_FILENAME))) {
            logger.info("Working with config {}.", CONFIG_FILENAME);
            configurationWarningWindow.showAndWait();
        }

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