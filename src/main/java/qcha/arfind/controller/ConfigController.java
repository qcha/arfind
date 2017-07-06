package qcha.arfind.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ConfigController {
    private List<TextField> data = new ArrayList<>();
    private static final String lineSeparator = "\n";
    private int buttonCounts = 0;
    @FXML
    private GridPane gridPane;
    @FXML
    private Button saveButton;

    public void addButtonAction() throws Exception {
        try {
            if (buttonCounts < 11) {
                buttonCounts++;
                data.add(new TextField());
                gridPane.add(data.get(buttonCounts - 1), 0, buttonCounts - 1);
            }
        }
        catch (IllegalStateException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveButtonAction() throws Exception {
        try {
            Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.close();
            Parent root = FXMLLoader.load(getClass().getResource("/application-stringList-window.fxml"));
            stage.setTitle("JavaFX App");
            stage.setScene(new Scene(root, 800, 600));
            for (TextField textField : data) {
                writeToFile("log.csv");
            }
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeToFile(String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
                for (int i = 0 ; i < data.size(); i++) {
                    writer.write(data.get(i).getText() + lineSeparator);
                }
        } catch (IOException e) {
            throw new IllegalStateException("Can't write to this file");
        }
    }

}

