package qcha.arfind.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public final class RootController {

    @FXML
    private MenuBar menuBar;

    public void loadConfigurations() {
        try {
            Stage mainWindowStage = new Stage();
            Parent mainWindowInterface = FXMLLoader.load(getClass().getResource("../view/application-configuration-window.fxml"));
            mainWindowStage.setTitle("Добавление строк");
            mainWindowStage.setResizable(false);
            mainWindowStage.setScene(new Scene(mainWindowInterface));
            mainWindowStage.initModality(Modality.WINDOW_MODAL);
            mainWindowStage.initOwner(menuBar.getScene().getWindow());
            mainWindowStage.show();
        } catch (IOException e) {
            throw new RuntimeException("Cannot find fxml for configuration window", e);
        }
    }
}