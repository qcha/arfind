package qcha.arfind.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import qcha.arfind.model.Company;

import java.io.File;
import java.util.Objects;

public class CompanyEditDialogController {

    private Stage dialogStage;
    private Company company;
    private boolean okClicked;

    @FXML
    private TextField companyNameField;
    @FXML
    private TextField filePathField;
    @FXML
    private Label companyNameInfo;
    @FXML
    private Label filePathInfo;


    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setCompany(Company company) {
        this.company = company;
        companyNameField.setText(company.getCompanyName());
        filePathField.setText(company.getFilePath());
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void handleOk() {
        if (Objects.isNull(companyNameField.getText()) && Objects.isNull(filePathField.getText())) {
            companyNameInfo.setText("Введите название фирмы");
            companyNameInfo.setTextFill(Color.rgb(210, 39, 30));
            filePathInfo.setText("Выберите путь к файлу");
            filePathInfo.setTextFill(Color.rgb(210, 39, 30));
        }
        if (Objects.nonNull(companyNameInfo.getText()) && Objects.nonNull(filePathField.getText()) && new File(filePathField.getText()).exists()) {
            company.setCompanyName(companyNameField.getText());
            company.setFilePath(filePathField.getText());
            dialogStage.close();
        }
        okClicked = true;
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    @FXML
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
}