package qcha.arfind.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import qcha.arfind.view.Main;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigController {
    private static final String LINE_SEPARATOR = System.lineSeparator();
    private ObservableList<TextField> textFields = FXCollections.observableArrayList();

    @FXML
    private Button saveButton;
    @FXML
    private ListView<TextField> listView;
    @FXML
    private TextField textField;

    public void addTextFields() {
        try {
            listView.setItems(textFields);
            textFields.add(new TextField());
        }
        catch (Exception e) {
            throw new RuntimeException("Cannot add new fields", e);
        }
    }
    public void saveConfiguration() {
        try {
            Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.close();
            Parent root = FXMLLoader.load(getClass().getResource("/application-stringList-window.fxml"));
            Stage secondStage = Main.getPrimaryStage();
            secondStage.setScene(new Scene(root, 600, 400));
            writeToFile("log.csv");
            secondStage.setResizable(true);
            secondStage.show();
        } catch (IOException e) {
            throw new RuntimeException("Cannot save current configuration", e);
        }
    }
    private void writeToFile(String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(textField.getText() + LINE_SEPARATOR);
            for (int i = 0 ; i < textFields.size(); i++) {
                    writer.write(textFields.get(i).getText() + LINE_SEPARATOR);
                }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

