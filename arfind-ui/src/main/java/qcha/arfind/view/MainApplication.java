package qcha.arfind.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import qcha.arfind.SearchModelCache;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static qcha.arfind.utils.Constants.ConfigFileConstants.CONFIG_FILENAME;
import static qcha.arfind.utils.Constants.UserResolutionConstants.DEFAULT_USER_RESOLUTION_HEIGHT;
import static qcha.arfind.utils.Constants.UserResolutionConstants.DEFAULT_USER_RESOLUTION_WIDTH;

public class MainApplication extends Application {
    private final String TITLE = "Поисковик артикулов";

    //this window size occupies 70% of user's display width and 90% of display height
    private final double DEFAULT_WIDTH = 0.7 * DEFAULT_USER_RESOLUTION_WIDTH;
    private final double DEFAULT_HEIGHT = 0.9 * DEFAULT_USER_RESOLUTION_HEIGHT;
    private Scene mainScene;
    private SearchView searchView;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (!SearchModelCache.getOrCreateCache().isEmpty()) {
                SearchModelCache.saveCacheToFile();
            } else {
                try {
                    Files.deleteIfExists(Paths.get(CONFIG_FILENAME));
                } catch (IOException e) {
                    throw new RuntimeException("Cannot find file - config.csv", e);
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