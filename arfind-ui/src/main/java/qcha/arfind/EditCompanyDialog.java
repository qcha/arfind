package qcha.arfind;

import javafx.geometry.Insets;
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

import static qcha.arfind.utils.Constants.UserResolutionConstants.DEFAULT_USER_RESOLUTION_HEIGHT;
import static qcha.arfind.utils.Constants.UserResolutionConstants.DEFAULT_USER_RESOLUTION_WIDTH;

/**
 * This class is responsible for creating dialog window
 */
class EditCompanyDialog {
    private final String TITLE = "Редактирование";
    private final double DEFAULT_WIDTH = 0.5 * DEFAULT_USER_RESOLUTION_WIDTH;
    private final double DEFAULT_HEIGHT = 0.45 * DEFAULT_USER_RESOLUTION_HEIGHT;

    private final boolean isForEdit;

    private Stage dialogWindow;
    private ConfigurationWindow parentWindow;
    private TextField companyName;
    private TextField filePath;
    private Button loadFilePath;
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
        AnchorPane dialogRootLayout = new AnchorPane();
        dialogRootLayout.getChildren().add(createDialogWindowInterface());

        dialogWindow.setTitle(TITLE);
        //making this window modal
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
        HBox buttonBarBox = new HBox(10);

        Button saveButton = new Button("Сохранить");
        saveButton.setDefaultButton(true);

        //do not allow user to press the button when there is no input in text fields
        saveButton.disableProperty().bind(companyName.textProperty().isEqualTo("").
                or(filePath.textProperty().isEqualTo("")));

        saveButton.setOnAction(e -> saveAndClose());
        saveButton.setMinWidth(0.15 * DEFAULT_WIDTH);
        saveButton.setMinHeight(0.075 * DEFAULT_HEIGHT);
        saveButton.setFont(Font.font(16));

        Button cancelButton = new Button("Отмена");
        cancelButton.setOnAction(e -> dialogWindow.close());
        cancelButton.setMinWidth(0.15 * DEFAULT_WIDTH);
        cancelButton.setMinHeight(0.075 * DEFAULT_HEIGHT);
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
        filePath.setMaxWidth(0);
        filePath.setMinHeight(0.067 * DEFAULT_HEIGHT);
        filePath.setVisible(false);
        filePath.setEditable(false);
        loadFilePath = new Button("Просмотр");
        loadFilePath.setMinHeight(0.067 * DEFAULT_HEIGHT);
        loadFilePath.setPrefWidth(0.625 * DEFAULT_WIDTH);

        loadFilePath.setOnAction(e -> {
            openFileChooser();
            filePath.setMinWidth(550);
            loadFilePath.setMaxWidth(100);
            filePath.setVisible(true);
        });
        HBox.setHgrow(loadFilePath, Priority.ALWAYS);

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
        dialogWindowLayout.setVgap(50);
        dialogWindowLayout.setPadding(new Insets(25, 25, 25, 25));

        Label companyNameInfo = new Label("Название фирмы:");
        companyNameInfo.setFont(Font.font(14));
        dialogWindowLayout.add(companyNameInfo, 0, 1);

        companyName = new TextField();
        companyName.setMinWidth(550);
        companyName.setMinHeight(0.07 * DEFAULT_HEIGHT);
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
