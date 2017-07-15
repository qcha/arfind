package qcha.arfind.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import qcha.arfind.Constants;
import qcha.arfind.view.Main;

import java.io.IOException;

public class ConfigController {
    private ObservableList<TextField> textFields;

    @FXML
    private Button saveButton;
    @FXML
    private ListView<TextField> listView;
    @FXML
    private TextField textField;

    @FXML
    public void initialize() {
        textFields = FXCollections.observableArrayList();
        listView.setItems(textFields);
    }

    @FXML
    private void addTextField() {
        TextField newTextField = new TextField();
        newTextField.setPromptText("Введите имя...");
        textFields.add(newTextField);
    }

    @FXML
    private void removeTextField() {
        if (textFields.size() > 0) {
            textFields.remove(textFields.size() - 1);
        }
    }

    @FXML
    private void saveConfiguration() {
        try {
            Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.close();
            Parent root = FXMLLoader.load(getClass().getResource("../view/application-stringList-window.fxml"));
            Stage secondStage = Main.getPrimaryStage();
            secondStage.setScene(new Scene(root, 600, 400));
            FileUtils.writeStringToFile(Constants.FileWriterConstants.FILENAME, textField.getText() +
                    Constants.FileWriterConstants.LINE_SEPARATOR, "UTF-8");
            for (TextField allTextFields : textFields) {
                FileUtils.writeStringToFile(Constants.FileWriterConstants.FILENAME, allTextFields.getText() +
                        Constants.FileWriterConstants.LINE_SEPARATOR, "UTF-8", true);
            }
            secondStage.show();
        } catch (IOException e) {
            throw new RuntimeException("Cannot find fxml for save configuration window", e);
        }
    }


}

