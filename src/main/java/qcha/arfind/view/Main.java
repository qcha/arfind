package qcha.arfind.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import qcha.arfind.Constants;

import java.io.IOException;

public class Main extends Application {
    private Stage primaryStage;
    private BorderPane rootLayout;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle(Constants.MainWindow.TITLE);
        loadRootLayout();
        loadMainWindow();
    }

    private void loadRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("../view/root-layout.fxml"));
            rootLayout = loader.load();
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            throw new RuntimeException("Cannot find fxml for loading root", e);
        }
    }

    private void loadMainWindow() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("../view/main-window.fxml"));
            AnchorPane mainWindow = loader.load();
            rootLayout.setCenter(mainWindow);
        } catch (IOException e) {
            throw new RuntimeException("Cannot find fxml for loading main window", e);
        }
    }
}
