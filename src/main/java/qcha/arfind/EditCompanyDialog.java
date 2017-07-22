package qcha.arfind;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import qcha.arfind.model.Company;
import qcha.arfind.utils.LabelConfiguration;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

class EditCompanyDialog {
    private static final int DEFAULT_WIDTH = 400;
    private static final int DEFAULT_HEIGHT = 250;

    private Stage dialogStage;
    private Company company;
    private TextField companyNameField;
    private TextField filePathField;
    private Label companyNameErrorText;
    private Label filePathErrorText;
    private ConfigurationWindow configurationWindow;

    EditCompanyDialog(ConfigurationWindow configurationWindow) {
        this.configurationWindow = configurationWindow;
    }

    private void setCompany(Company company) {
        this.company = company;
        companyNameField.setText(company.getCompanyName());
        filePathField.setText(company.getFilePath());
    }

    //todo
    void addCompany() {
        Company company = new Company();
        showEditDialog(company);
        handleOk();
        new ConfigurationWindow().getCompanies().add(company);
    }

    //todo
    void editCompany() {
        Company company = configurationWindow.getCompanyTableView().getSelectionModel().getSelectedItem();
        showEditDialog(company);
        handleOk();
    }

    //todo
    void removeCompany() {
        int selectedIndex = new ConfigurationWindow().getCompanyTableView().getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            new ConfigurationWindow().getCompanyTableView().getItems().remove(selectedIndex);
        }
    }

    //todo
    void removeAll() {
        new ConfigurationWindow().getCompanyTableView().getItems().clear();
    }

    /*//todo
    void saveConfigurations() {
        try {
            ConfigurationWindow.convertTableDataToString();
            FileUtils.writeLines(new File(Constants.ConfigFileConstants.CONFIG_FILENAME), Constants.ConfigFileConstants.DEFAULT_CHARSET, ConfigurationWindow.getTableStringData());
            ConfigurationWindow.getConfigurationWindow().close();
        } catch (IOException e) {
            throw new RuntimeException("Cannot find such file", e);
        }
    }*/

    private void showEditDialog(Company company) {
        dialogStage = new Stage();
        AnchorPane dialogRootLayout = new AnchorPane();
        dialogRootLayout.getChildren().add(createGridPane());
        dialogStage.setTitle(Constants.DialogWindowConstants.TITLE);
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(new ConfigurationWindow().getCompanyTableView().getScene().getWindow());
        Scene scene = new Scene(dialogRootLayout, Constants.DialogWindowConstants.DEFAULT_WIDTH, Constants.DialogWindowConstants.DEFAULT_HEIGHT);
        dialogStage.setScene(scene);
        setCompany(company);
        dialogStage.showAndWait();
    }

    private Label createLabel() {
        return new LabelConfiguration().label;
    }

    private HBox createButtonBarBox() {
        Button okButton = new Button("OK");
        okButton.setOnAction(e -> handleOk());
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> dialogStage.close());
        HBox buttonBarBox = new HBox(Constants.HBoxConstants.DEFAULT_SPACING);
        buttonBarBox.setAlignment(Pos.BOTTOM_RIGHT);
        buttonBarBox.getChildren().addAll(okButton, cancelButton);
        return buttonBarBox;
    }

    private HBox createFilePathBox() {
        filePathField = new TextField();
        Button loadFilePath = new Button("...");
        loadFilePath.setOnAction(e -> openFileChooser());
        HBox filePathBox = new HBox();
        HBox.setHgrow(filePathField, Priority.ALWAYS);
        filePathBox.getChildren().addAll(filePathField, loadFilePath);
        return filePathBox;
    }

    private GridPane createGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(Constants.GridPaneConstants.DEFAULT_HGAP);
        gridPane.setVgap(Constants.GridPaneConstants.DEFAULT_VGAP);
        gridPane.setPadding(Constants.InsetsConstants.DEFAULT_PADDING);

        Label companyNameInfo = new Label("Название фирмы:");
        gridPane.add(companyNameInfo, 0, 1);

        companyNameField = new TextField();
        companyNameField.setMinWidth(250);
        gridPane.add(companyNameField, 1, 1);

        gridPane.add(createLabel(), 1, 2);

        Label filePathInfo = new Label("Путь к файлу:");
        gridPane.add(filePathInfo, 0, 3);

        gridPane.add(createFilePathBox(), 1, 3);

        gridPane.add(createLabel(), 1, 4);

        gridPane.add(createButtonBarBox(), 1, 5);
        return gridPane;
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
            //fixme
            label.setTextFill(Constants.LabelConstants.DEFAULT_TEXTFILL);
            return false;
        }
        return true;
    }

}
