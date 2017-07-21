package qcha.arfind.controller;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import qcha.arfind.Constants;
import qcha.arfind.model.Company;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

class CompanyShowDialogs {
    private Stage dialogStage;
    private Company company;


    private TextField companyNameField;
    private TextField filePathField;
    private Label companyNameErrorText;
    private Label filePathErrorText;

    private void setCompany(Company company) {
        this.company = company;
        companyNameField.setText(company.getCompanyName());
        filePathField.setText(company.getFilePath());
    }

    void addCompany() {
        Company company = new Company();
        showEditDialog(company);
        handleOk();
        ConfigurationWindowInterface.getCompanyData().add(company);
    }
    void editCompany() {
        Company company = ConfigurationWindowInterface.getConfigurationCompanyTable().getSelectionModel().getSelectedItem();
        if (company != null) {
            showEditDialog(company);
            handleOk();
        }
    }

    void removeCompany() {
        int selectedIndex = ConfigurationWindowInterface.getConfigurationCompanyTable().getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            ConfigurationWindowInterface.getConfigurationCompanyTable().getItems().remove(selectedIndex);
        }
    }

    void removeAll() {
        ConfigurationWindowInterface.getConfigurationCompanyTable().getItems().clear();
    }

    void saveConfigurations() {
        try {
            ConfigurationWindowInterface.convertTableDataToString();
            FileUtils.writeLines(new File(Constants.ConfigFileConstants.CONFIG_FILENAME), Constants.ConfigFileConstants.DEFAULT_CHARSET, ConfigurationWindowInterface.getTableStringData());
            ConfigurationWindowInterface.getConfigurationWindow().close();
        } catch (IOException e) {
            throw new RuntimeException("Cannot find such file", e);
        }
    }

    private void showEditDialog(Company company) {
        dialogStage = new Stage();
        AnchorPane dialogRootLayout = new AnchorPane();
        dialogStage.setTitle("Edit Company");

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(15);
        gridPane.setPadding(new Insets(25, 25, 25, 25));

        Label companyNameInfo = new Label("Название фирмы:");
        gridPane.add(companyNameInfo, 0, 1);

        companyNameField = new TextField();
        companyNameField.setMinWidth(250);
        gridPane.add(companyNameField, 1, 1);

        companyNameErrorText = new Label();
        companyNameErrorText.setMinWidth(100);
        companyNameErrorText.setAlignment(Pos.CENTER);
        gridPane.add(companyNameErrorText, 1, 2);

        Label filePathInfo = new Label("Путь к файлу:");
        gridPane.add(filePathInfo, 0, 3);

        filePathField = new TextField();
        Button loadFilePath = new Button("...");

        loadFilePath.setOnAction(e -> openFileChooser());

        HBox filePathBox = new HBox();
        HBox.setHgrow(filePathField, Priority.ALWAYS);

        filePathBox.getChildren().addAll(filePathField, loadFilePath);
        gridPane.add(filePathBox, 1, 3);


        filePathErrorText = new Label();
        filePathErrorText.setMinWidth(100);
        filePathErrorText.setAlignment(Pos.CENTER);
        gridPane.add(filePathErrorText, 1, 4);


        Button okButton = new Button("OK");

        okButton.setOnAction(e -> handleOk());

        Button cancelButton = new Button("Cancel");

        cancelButton.setOnAction(e -> dialogStage.close());

        HBox buttonBarBox = new HBox(10);
        buttonBarBox.setAlignment(Pos.BOTTOM_RIGHT);
        buttonBarBox.getChildren().addAll(okButton, cancelButton);
        gridPane.add(buttonBarBox, 1, 5);

        AnchorPane.setRightAnchor(filePathBox, 0.0);
        AnchorPane.setBottomAnchor(filePathBox, 0.0);
        dialogRootLayout.getChildren().add(gridPane);


        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(ConfigurationWindowInterface.getConfigurationCompanyTable().getScene().getWindow());
        Scene scene = new Scene(dialogRootLayout, 400, 250);
        dialogStage.setScene(scene);
        setCompany(company);
        dialogStage.showAndWait();
    }

    private void handleOk() {
        if (validateInput(companyNameField, companyNameErrorText) || validateInput(filePathField, filePathErrorText)) {
        }
        if (Files.exists(Paths.get(filePathField.getText()))) {
            company.setCompanyName(companyNameField.getText());
            company.setFilePath(filePathField.getText());
            dialogStage.close();
        }
    }

    private void openFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File file = fileChooser.showOpenDialog(dialogStage);
        if (file.exists()) {
            setFilePath(file);
        }
    }

    private void setFilePath(File file) {
        filePathField.setText(file.getAbsolutePath());
    }

    private boolean validateInput(TextField field, Label label) {
        if (Objects.isNull(field.getText())) {
            label.setText("Введите данные");
            label.setTextFill(Color.rgb(210, 39, 30));
            return false;
        }
        return true;
    }

}
