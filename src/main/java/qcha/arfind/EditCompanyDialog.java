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
import org.apache.commons.io.FileUtils;
import qcha.arfind.model.Company;
import qcha.arfind.view.ErrorLabel;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

class EditCompanyDialog {
    private final String TITLE = "Editing company";
    private final int DEFAULT_WIDTH = 550;
    private final int DEFAULT_HEIGHT = 300;

    private Stage dialogStage;
    private Company company;
    private TextField companyNameField;
    private TextField filePathField;
    private ErrorLabel filePathErrorText;
    private ConfigurationWindow configurationWindow;

    EditCompanyDialog(ConfigurationWindow configurationWindow) {
        this.configurationWindow = configurationWindow;
    }

    private void setCompany(Company company) {
        if (Objects.isNull(company)) {
            return;
        }
        this.company = company;
        companyNameField.setText(company.getCompanyName());
        filePathField.setText(company.getFilePath());
    }

    //fixme clicking red cross adds empty data to table
    void addCompany() {
        Company addingCompany = new Company();
        createEditDialog(addingCompany);
        dialogStage.showAndWait();
        if (Objects.nonNull(company.getCompanyName()) || Objects.nonNull(company.getFilePath())) {
            configurationWindow.getCompanies().add(addingCompany);
        }
    }

    void editCompany() {
        Company selectedCompany = configurationWindow.getCompanyTableView().getSelectionModel().getSelectedItem();
        if (Objects.nonNull(selectedCompany)) {
            createEditDialog(selectedCompany);
            dialogStage.showAndWait();
        }
    }

    void removeCompany() {
        int selectedIndex = configurationWindow.getCompanyTableView().getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            configurationWindow.getCompanyTableView().getItems().remove(selectedIndex);
        }
    }

    void removeAll() {
        configurationWindow.getCompanyTableView().getItems().clear();
    }

    void saveConfigurations() {
        try {
            FileUtils.writeLines(new File(Constants.ConfigFileConstants.CONFIG_FILENAME),
                    Constants.ConfigFileConstants.DEFAULT_CHARSET,
                    configurationWindow.getCompanyData());
            //readConfigFileToListView();
            configurationWindow.getConfigurationWindow().close();
        } catch (IOException e) {
            throw new RuntimeException("Cannot find such file", e);
        }
    }

    /**
     * Create dialog window and set company
     */
    private void createEditDialog(Company company) {
        dialogStage = new Stage();
        AnchorPane dialogRootLayout = new AnchorPane();
        dialogRootLayout.getChildren().add(
                createGridPane());

        dialogStage.setTitle(TITLE);
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(configurationWindow.getCompanyTableView().getScene().getWindow());
        dialogStage.setResizable(false);
        Scene scene = new Scene(
                dialogRootLayout,
                DEFAULT_WIDTH,
                DEFAULT_HEIGHT);
        dialogStage.setScene(scene);
        setCompany(company);
    }

    /**
     * Create error label which appears after wrong input
     * @return Label
     */
    private Label createLabel() {
        filePathErrorText = new ErrorLabel("Неправильно указан путь к файлу");
        return filePathErrorText;
    }

    /**
     * Create two default buttons - "OK" and "Cancel"
     * @return HBox
     */
    private HBox createButtonBarBox() {
        HBox buttonBarBox = new HBox(Constants.HBoxConstants.DEFAULT_SPACING);

        Button okButton = new Button("OK");
        okButton.setDefaultButton(true);
        okButton.disableProperty().bind(companyNameField.textProperty().isEqualTo("").
                or(filePathField.textProperty().isEqualTo("")));

        okButton.setOnAction(e -> handleOk());

        Button cancelButton = new Button("Cancel");
        cancelButton.setDefaultButton(true);
        cancelButton.setOnAction(e -> dialogStage.close());
        buttonBarBox.setAlignment(Pos.BOTTOM_RIGHT);
        buttonBarBox.getChildren().addAll(
                okButton,
                cancelButton);

        return buttonBarBox;
    }

    /**
     * Create file path text field with button to open file chooser.
     * @return HBox
     */
    private HBox createFilePathBox() {
        HBox filePathBox = new HBox();

        filePathField = new TextField();
        Button loadFilePath = new Button("...");
        loadFilePath.setOnAction(e -> openFileChooser());
        HBox.setHgrow(filePathField, Priority.ALWAYS);
        filePathBox.getChildren().addAll(
                filePathField,
                loadFilePath);

        return filePathBox;
    }

    /**
     * Create grid pane
     * @return GridPane with dialog text fields and labels.
     */
    private GridPane createGridPane() {
        GridPane gridPane = new GridPane();

        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(Constants.GridPaneConstants.DEFAULT_HGAP);
        gridPane.setVgap(Constants.GridPaneConstants.DEFAULT_VGAP);
        gridPane.setPadding(Constants.PaddingConstants.DEFAULT_PADDING);

        Label companyNameInfo = new Label("Название фирмы:");
        gridPane.add(companyNameInfo, 0, 1);

        companyNameField = new TextField();
        companyNameField.setMinWidth(250);
        gridPane.add(companyNameField, 1, 1);

        Label filePathInfo = new Label("Путь к файлу:");
        gridPane.add(filePathInfo, 0, 2);

        gridPane.add(createFilePathBox(), 1, 2);

        gridPane.add(createLabel(), 1, 3);

        gridPane.add(createButtonBarBox(), 1, 4);

        return gridPane;
    }

    private void handleOk() {
        if (Files.exists(Paths.get(filePathField.getText()))) {
            company.setCompanyName(companyNameField.getText());
            company.setFilePath(filePathField.getText());
            dialogStage.close();
        } else {
            filePathErrorText.setVisible(true);
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

   /* private void readConfigFileToListView() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(Constants.ConfigFileConstants.CONFIG_FILENAME));
            String line;
            while ((Objects.nonNull(line = br.readLine()))) {
                String[] companyField = line.split(Constants.ConfigFileConstants.DEFAULT_FIELD_DELIMITER);
                String companyName = companyField[0];
                .getCompanyList().add(companyName);
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException("Cannot find file", e);
        } catch (IOException e) {
            throw new RuntimeException("Cannot read file with such name", e);
        }

    }
*/

}
