package qcha.arfind.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import qcha.arfind.view.Main;

import java.io.IOException;

public class RootLayoutController {

    @FXML
    private VBox configurationInterface;
    private static Stage configurationWindow;

    @FXML
    private MenuBar menuBar;


    public void loadConfigurations() {
        try {
            configurationWindow = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../view/application-configuration-window.fxml"));
            configurationInterface = loader.load();
            ConfigController configController = loader.getController();
            configController.setMain(new Main());
            Scene scene = new Scene(configurationInterface);
            configurationWindow.setTitle("Добавление строк");
            configurationWindow.setResizable(false);
            configurationWindow.setScene(scene);
            configurationWindow.initModality(Modality.WINDOW_MODAL);
            configurationWindow.initOwner(menuBar.getScene().getWindow());
            configurationWindow.show();
        } catch (IOException e) {
            throw new RuntimeException("Cannot find fxml for configuration window", e);
        }
    }

    public static Stage getConfigurationWindow() {
        return configurationWindow;
    }

}
