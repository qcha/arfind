package qcha.arfind;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import qcha.arfind.model.Company;
import qcha.arfind.view.ErrorLabelFactory;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * This class is responsible for creating dialog window
 */
class EditCompanyDialog {
    private final String TITLE = "Редактирование";

    private boolean isForEdit;

    private Stage dialogWindow;
    private ConfigurationWindow parentWindow;
    private TextField companyName;
    private TextField filePath;
    private Label nameErrorLabel;
    private Label fileErrorLabel;
    private Company company;

    /**
     * Class constructor.
     */
    EditCompanyDialog(ConfigurationWindow parentWindow, Company company) {
        this.parentWindow = parentWindow;
        this.company = company;
        initEditDialog();

        //is it company for edit?
        if (Objects.nonNull(company)) {
            companyName.setText(company.getName());
            filePath.setText(company.getPath());
            isForEdit = true;
        } else {
            //is for adding new company
            this.company = new Company();
            isForEdit = false;
        }
    }

    void show() {
        dialogWindow.show();
    }

    /**
     * Init dialog window for edit company.
     */
    private void initEditDialog() {
        dialogWindow = new Stage();

        VBox dialogRootLayout = new VBox();

        dialogRootLayout.getChildren().add(createDialogWindowInterface());
        dialogRootLayout.setAlignment(Pos.CENTER);

        dialogWindow.setTitle(TITLE);
        dialogWindow.initModality(Modality.WINDOW_MODAL);
        dialogWindow.initOwner(parentWindow.getCompanyTableView().getScene().getWindow());
        dialogWindow.setResizable(false);

        Scene scene = new Scene(dialogRootLayout);

        dialogWindow.setScene(scene);
    }

    /**
     * Create two buttons - "OK" and "Cancel"
     *
     * @return HBox
     */
    private HBox createButtonBarBox() {
        HBox buttonBarBox = new HBox(10);

        Button saveButton = new Button("Сохранить");
        saveButton.setDefaultButton(true);

        //do not allow user to press the button when there is no input in text fields
        saveButton.disableProperty().bind(companyName.textProperty().isEqualTo("").
                or(filePath.textProperty().isEqualTo("")));

        saveButton.setOnAction(e -> saveAndClose());
        saveButton.setMinWidth(300);
        saveButton.setMinHeight(75);
        saveButton.setFont(Font.font(16));

        Button cancelButton = new Button("Отмена");
        cancelButton.setOnAction(e -> dialogWindow.close());
        cancelButton.setMinWidth(300);
        cancelButton.setMinHeight(75);
        cancelButton.setFont(Font.font(16));

        GridPane.setColumnSpan(buttonBarBox, 2);
        buttonBarBox.setAlignment(Pos.BOTTOM_LEFT);
        buttonBarBox.getChildren().addAll(
                saveButton,
                cancelButton
        );

        return buttonBarBox;
    }

    /**
     * Create line to separate button bar from text fields
     *
     * @return Separator
     */
    private Separator createSeparatingLine() {
        Separator separatingLine = new Separator();

        separatingLine.setValignment(VPos.CENTER);
        GridPane.setConstraints(separatingLine, 0, 1);
        GridPane.setColumnSpan(separatingLine, 2);

        return separatingLine;
    }

    /**
     * Create file path text field with button to open file chooser.
     *
     * @return HBox
     */
    private HBox createFinderLine() {
        HBox filePathBox = new HBox();

        filePath = new TextField();
        filePath.setPrefSize(300, 35);
        Button loadFilePath = new Button("Обзор...");
        loadFilePath.setPrefSize(100, 35);
        loadFilePath.setOnAction(e -> openFileChooser());
        HBox.setHgrow(filePath, Priority.ALWAYS);

        filePathBox.getChildren().addAll(
                filePath,
                loadFilePath
        );

        return filePathBox;
    }

    /**
     * Create grid pane of dialog window layout
     *
     * @return GridPane with dialogWindow text fields and labels.
     */
    private GridPane createDialogWindowInterface() {
        GridPane dialogWindowLayout = new GridPane();

        dialogWindowLayout.setAlignment(Pos.CENTER);
        dialogWindowLayout.setHgap(35);
        dialogWindowLayout.setVgap(30);
        dialogWindowLayout.setPadding(new Insets(10, 10, 10, 10));

        Label companyNameInfo = new Label("Название фирмы:");
        companyNameInfo.setFont(Font.font(14));
        dialogWindowLayout.add(companyNameInfo, 0, 0);

        companyName = new TextField();
        companyName.setPrefWidth(400);
        companyName.setPrefHeight(35);
        dialogWindowLayout.add(companyName, 1, 0);

        Label filePathInfo = new Label("Путь к файлу:");
        filePathInfo.setFont(Font.font(14));
        dialogWindowLayout.add(filePathInfo, 0, 1);

        dialogWindowLayout.add(createFinderLine(), 1, 1);

        fileErrorLabel = ErrorLabelFactory.createErrorLabel("Неправильно указан путь к файлу");
        dialogWindowLayout.add(fileErrorLabel, 1, 2);

        nameErrorLabel = ErrorLabelFactory.createErrorLabel("Такая компания уже существует");
        dialogWindowLayout.add(nameErrorLabel, 1, 2);

        dialogWindowLayout.add(createSeparatingLine(), 0, 3);

        dialogWindowLayout.add(createButtonBarBox(), 0, 4);

        return dialogWindowLayout;
    }

    /**
     * Saves configuration and closes the dialog window if input is correct
     */
    private void saveAndClose() {
        if (Files.exists(Paths.get(filePath.getText())) && validateCompanyName()) {
            company.setName(companyName.getText());
            company.setPathToPrice(filePath.getText());

            //if we edit existing company - don't add it again
            if (!isForEdit) {
                parentWindow.getCompanies().add(company);
            }
            dialogWindow.close();
        }
        //if company with input name already exists
        if (!validateCompanyName()) {
            nameErrorLabel.setVisible(true);
        } else {
            fileErrorLabel.setVisible(true);
        }
    }

    /**
     * Opens file chooser to select path to company file
     */
    private void openFileChooser() {
        FileChooser fileChooser = new FileChooser();

        //only excel files
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter(
                "Excel files (*.xlsx, *.xls)", "*.xlsx", "*.xls");
        fileChooser.getExtensionFilters().add(extensionFilter);
        File file = fileChooser.showOpenDialog(dialogWindow);
        if (Objects.isNull(file)) {
            filePath.setText("");
        } else {
            filePath.setText(file.getAbsolutePath());
        }
    }

    /**
     * Create error label which appears after wrong input to file path text field
     *
     * @return false - if company name written in text field already exists in company table
     * true - if does not exist
     */

    private boolean validateCompanyName() {
        for (String item : parentWindow.getCompanyColumnData()) {
            if (item.equals(companyName.getText())) {
                return false;
            }
        }
        return true;
    }
}
