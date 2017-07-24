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
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import qcha.arfind.model.Company;
import qcha.arfind.view.ErrorLabel;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

class EditCompanyDialog {
    private final String TITLE = "Editing company";
    private final int DEFAULT_WIDTH = 550;
    private final int DEFAULT_HEIGHT = 300;

    private final boolean isForEdit;

    private Stage dialogWindow;
    private ConfigurationWindow parentWindow;
    private TextField companyName;
    private TextField filePath;
    private Label errorLabel;
    private Company company;

    EditCompanyDialog(ConfigurationWindow parentWindow, Company company) {
        this.parentWindow = parentWindow;
        this.company = company;
        initEditDialog();

        //is it company for edit?
        if (Objects.nonNull(company)) {
            companyName.setText(company.getCompanyName());
            filePath.setText(company.getFilePath());
            isForEdit = true;
        } else {
            //is for adding new company
            this.company = new Company();
            isForEdit = false;
        }

        dialogWindow.showAndWait();
    }

    /**
     * Init dialog window for edit company.
     */
    private void initEditDialog() {
        dialogWindow = new Stage();
        AnchorPane dialogRootLayout = new AnchorPane();
        dialogRootLayout.getChildren().add(createDialogInterface());

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

        Button okButton = new Button("Save");
        okButton.setDefaultButton(true);
        okButton.disableProperty().bind(companyName.textProperty().isEqualTo("").
                or(filePath.textProperty().isEqualTo("")));

        okButton.setOnAction(e -> saveAndClose());

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> dialogWindow.close());

        buttonBarBox.setAlignment(Pos.BOTTOM_RIGHT);
        buttonBarBox.getChildren().addAll(
                okButton,
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
        filePath.setMaxWidth(330);
        Button loadFilePath = new Button("...");
        loadFilePath.setOnAction(e -> openFileChooser());
        HBox.setHgrow(filePath, Priority.ALWAYS);

        filePathBox.getChildren().addAll(
                filePath,
                loadFilePath
        );

        return filePathBox;
    }

    /**
     * Create grid pane
     *
     * @return GridPane with dialogWindow text fields and labels.
     */
    private GridPane createDialogInterface() {
        GridPane dialogLayout = new GridPane();

        dialogLayout.setAlignment(Pos.CENTER);
        dialogLayout.setHgap(Constants.GridPaneConstants.DEFAULT_HGAP);
        dialogLayout.setVgap(Constants.GridPaneConstants.DEFAULT_VGAP);
        dialogLayout.setPadding(Constants.PaddingConstants.DEFAULT_PADDING);

        Label companyNameInfo = new Label("Название фирмы:");
        dialogLayout.add(companyNameInfo, 0, 1);

        companyName = new TextField();
        companyName.setMaxWidth(350);
        dialogLayout.add(companyName, 1, 1);

        Label filePathInfo = new Label("Путь к файлу:");
        dialogLayout.add(filePathInfo, 0, 2);

        dialogLayout.add(createFinderLine(), 1, 2);

        errorLabel = createErrorLabel();
        dialogLayout.add(errorLabel, 1, 3);

        dialogLayout.add(createSeparatingLine(), 0, 4);

        dialogLayout.add(createButtonBarBox(), 1, 5);

        return dialogLayout;
    }

    private void saveAndClose() {
        if (Files.exists(Paths.get(filePath.getText()))) {
            company.setCompanyName(companyName.getText());
            company.setFilePath(filePath.getText());

            //if we edit existing company - don't add it again
            if (!isForEdit) {
                parentWindow.getCompanies().add(company);
            }

            dialogWindow.close();
        } else {
            errorLabel.setVisible(true);
        }
    }

    private void openFileChooser() {
        File file = new FileChooser().showOpenDialog(dialogWindow);
        if (file.exists()) {
            filePath.setText(file.getAbsolutePath());
        } else {
            errorLabel.isVisible();
        }
    }

    /**
     * Create error label which appears after wrong input
     *
     * @return ErrorLabel with error message
     * @see ErrorLabel
     */
    private Label createErrorLabel() {
        return new ErrorLabel("Неправильно указан путь к файлу");
    }

}
