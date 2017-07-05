package qcha.arfind.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ConfigController {
    private int i = 1;
    @FXML
    private GridPane gridPane;
    @FXML
    private Button saveButton;

    public void addButtonAction() {
        gridPane.add(new TextField(), 0, i++);
    }

    public void saveButtonAction() {
        try {
            Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.close();
            Parent root = FXMLLoader.load(getClass().getResource("/rootLayout.fxml"));
            stage.setTitle("JavaFX App");
            stage.setScene(new Scene(root, 500, 300));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

