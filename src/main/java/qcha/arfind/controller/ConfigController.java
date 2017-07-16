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
import qcha.arfind.utils.TextFieldConfiguration;
import qcha.arfind.view.Main;

import java.io.File;
import java.io.IOException;

public final class ConfigController {
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
        TextFieldConfiguration textFieldConfiguration = new TextFieldConfiguration();
        textFields.add(textFieldConfiguration.textField);
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
            Stage mainWindowStage = (Stage) saveButton.getScene().getWindow();
            mainWindowStage.close();
            Parent configurationWindowInterface = FXMLLoader.load(getClass().getResource("../view/application-stringList-window.fxml"));
            Stage configurationWindowStage = Main.getPrimaryStage();
            configurationWindowStage.setScene(new Scene(configurationWindowInterface, Constants.ConfigurationWindow.DEFAULT_WIDTH,
                    Constants.ConfigurationWindow.DEFAULT_HEIGHT));
            FileUtils.writeStringToFile(new File(Constants.FileWriterConstants.FILENAME), textField.getText() +
                    Constants.FileWriterConstants.LINE_SEPARATOR, Constants.FileWriterConstants.DEFAULT_CHARSET);
            for (TextField allTextFields : textFields) {
                FileUtils.writeStringToFile(new File(Constants.FileWriterConstants.FILENAME), allTextFields.getText() +
                        Constants.FileWriterConstants.LINE_SEPARATOR, Constants.FileWriterConstants.DEFAULT_CHARSET, true);
            }
            configurationWindowStage.show();
        } catch (IOException e) {
            throw new RuntimeException("Cannot find fxml for save configuration window", e);
        }
    }


}

