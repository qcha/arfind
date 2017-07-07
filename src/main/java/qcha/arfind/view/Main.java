package qcha.arfind.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import qcha.arfind.Constants;

public class Main extends Application {
    private static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        setPrimaryStage(primaryStage);
        Parent root = FXMLLoader.load(getClass().getResource("/main-window.fxml"));
        primaryStage.setTitle(Constants.MainWindow.TITLE);
        primaryStage.setScene(new Scene(root, Constants.MainWindow.WIDTH, Constants.MainWindow.HEIGHT));
        primaryStage.show();
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    private void setPrimaryStage(Stage primaryStage) {
        Main.primaryStage = primaryStage;
    }
}
