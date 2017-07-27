package qcha.arfind;

import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import qcha.arfind.model.Company;
import qcha.arfind.view.ErrorLabel;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * This class is responsible for creating dialog window
 *
 */
class EditCompanyDialog {
    private final String TITLE = "Editing company";
    private final int DEFAULT_WIDTH = 800;
    private final int DEFAULT_HEIGHT = 600;

    private final boolean isForEdit;

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
            filePath.setText(company.getPathToPrice());
            isForEdit = true;
        } else {
            //is for adding new company
            this.company = new Company();
            isForEdit = false;
        }

        dialogWindow.show();
    }

    /**
     * Init dialog window for edit company.
     */
    private void initEditDialog() {
        dialogWindow = new Stage();
        AnchorPane dialogRootLayout = new AnchorPane();
        dialogRootLayout.getChildren().add(createDialogWindowInterface());

        dialogWindow.setTitle(TITLE);
        dialogWindow.initModality(Modality.WINDOW_MODAL);
        dialogWindow.initOwner(parentWindow.getCompanyTableView().getScene().getWindow());
        dialogWindow.setResizable(false);

        Scene scene = new Scene(
                dialogRootLayout,
                DEFAULT_WIDTH,
                DEFAULT_HEIGHT
        );

        dialogWindow.setScene(scene);
    }

    /**
     * Create two buttons - "OK" and "Cancel"
     *
     * @return HBox
     */
    private HBox createButtonBarBox() {
        HBox buttonBarBox = new HBox(Constants.HBoxConstants.DEFAULT_SPACING);

        Button saveButton = new Button("Save");
        saveButton.setDefaultButton(true);

        saveButton.disableProperty().bind(companyName.textProperty().isEqualTo("").
                or(filePath.textProperty().isEqualTo("")));

        saveButton.setOnAction(e -> saveAndClose());
        saveButton.setMinWidth(120);
        saveButton.setMinHeight(45);
        saveButton.setFont(Font.font(16));

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> dialogWindow.close());
        cancelButton.setMinWidth(120);
        cancelButton.setMinHeight(45);
        cancelButton.setFont(Font.font(16));


        AnchorPane.setRightAnchor(cancelButton, 10.0);

        buttonBarBox.setAlignment(Pos.BOTTOM_RIGHT);
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
        filePath.setMinWidth(500);
        filePath.setMinHeight(40);
        Button loadFilePath = new Button("Browse...");
        loadFilePath.setMinHeight(40);
        loadFilePath.setMinWidth(60);
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
        dialogWindowLayout.setHgap(Constants.GridPaneConstants.DEFAULT_HGAP);
        dialogWindowLayout.setVgap(Constants.GridPaneConstants.DEFAULT_VGAP);
        dialogWindowLayout.setPadding(Constants.PaddingConstants.DEFAULT_PADDING);

        Label companyNameInfo = new Label("Название фирмы:");
        companyNameInfo.setFont(Font.font(14));
        dialogWindowLayout.add(companyNameInfo, 0, 1);

        companyName = new TextField();
        companyName.setMinWidth(500);
        companyName.setMinHeight(40);
        dialogWindowLayout.add(companyName, 1, 1);

        Label filePathInfo = new Label("Путь к файлу:");
        filePathInfo.setFont(Font.font(14));
        dialogWindowLayout.add(filePathInfo, 0, 2);

        dialogWindowLayout.add(createFinderLine(), 1, 2);

        fileErrorLabel = createFileErrorLabel();
        dialogWindowLayout.add(fileErrorLabel, 1, 4);

        nameErrorLabel = createNameErrorLabel();
        dialogWindowLayout.add(nameErrorLabel, 1, 4);


        dialogWindowLayout.add(createSeparatingLine(), 0, 5);

        dialogWindowLayout.add(createButtonBarBox(), 1, 6);

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
        if(!validateCompanyName()) {
            nameErrorLabel.setVisible(true);
        }
        else {
            fileErrorLabel.setVisible(true);
        }
    }

    private void openFileChooser() {
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter(
                "Excel files (*.xlsx, *.xls)", "*.xlsx", "*.xls");
        fileChooser.getExtensionFilters().add(extensionFilter);
        File file = fileChooser.showOpenDialog(dialogWindow);
        if (Objects.isNull(file)) {
            filePath.setText("");
        }
        else {
            filePath.setText(file.getAbsolutePath());
        }
    }
    /**
     * Create error label which appears after wrong input to file path text field
     *
     * @return false - if company name written in text field already exists in company table
     *         true - if does not exist
     */

    private boolean validateCompanyName() {
        for(String item : parentWindow.getCompanyColumnData()) {
            if(item.equals(companyName.getText())) {
                return false;
            }
        }
        return true;
    }
    /**
     * Create error label which appears after wrong input to file path text field
     *
     * @return ErrorLabel with error message
     * @see ErrorLabel
     */
    private Label createFileErrorLabel() {
        return new ErrorLabel("Неправильно указан путь к файлу");
    }

    /**
     * Create error label which appears after wrong input to company name text field
     *
     * @return ErrorLabel with error message
     * @see ErrorLabel
     */
    private Label createNameErrorLabel() {
        return new ErrorLabel("Такая компания уже существует");
    }

}
