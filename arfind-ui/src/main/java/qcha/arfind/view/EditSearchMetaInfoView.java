package qcha.arfind.view;

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
import qcha.arfind.SearchModelCache;
import qcha.arfind.model.SearchDetails;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * This class is responsible for creating dialog window
 */
class EditSearchMetaInfoView {
    private final String TITLE = "Редактирование";

    private boolean isForEdit;

    private Stage dialogWindow;
    private ApplicationConfigurationView parentWindow;
    private TextField companyNameTextField;
    private TextField filePathTextField;
    private Label nameErrorLabel;
    private Label fileErrorLabel;
    private SearchDetails searchDetails;

    /**
     * Class constructor.
     */
    EditSearchMetaInfoView(ApplicationConfigurationView parentWindow, SearchDetails searchDetails) {
        this.parentWindow = parentWindow;
        this.searchDetails = searchDetails;
        initEditDialog();

        //is it searchDetails for edit?
        if (Objects.nonNull(searchDetails)) {
            companyNameTextField.setText(searchDetails.getName());
            filePathTextField.setText(searchDetails.getPath());
            isForEdit = true;
        } else {
            //is for adding new searchDetails
            isForEdit = false;
        }
    }

    void show() {
        dialogWindow.show();
    }

    /**
     * Init dialog window for edit searchDetails.
     */
    private void initEditDialog() {
        dialogWindow = new Stage();

        VBox dialogRootLayout = new VBox();

        dialogRootLayout.getChildren().add(createDialogWindowInterface());
        dialogRootLayout.setAlignment(Pos.CENTER);

        dialogWindow.setTitle(TITLE);
        dialogWindow.initModality(Modality.WINDOW_MODAL);
        dialogWindow.initOwner(parentWindow.getConfigurationWindow().getScene().getWindow());
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
        return new HBox(10) {
            {
                Button saveButton = new Button("Сохранить") {
                    {
                        setDefaultButton(true);
                        //do not allow user to press the button when there is no input in text fields
                        disableProperty().bind(
                                companyNameTextField.textProperty()
                                        .isEqualTo("")
                                        .or(filePathTextField.textProperty()
                                                .isEqualTo("")));

                        setOnAction(e -> saveAndClose());
                        setMinWidth(300);
                        setMinHeight(75);
                        setFont(Font.font(16));
                    }
                };

                Button cancelButton = new Button("Отмена") {
                    {
                        setOnAction(e -> dialogWindow.close());
                        setMinWidth(300);
                        setMinHeight(75);
                        setFont(Font.font(16));
                    }
                };

                GridPane.setColumnSpan(this, 2);
                setAlignment(Pos.BOTTOM_LEFT);
                getChildren().addAll(
                        saveButton,
                        cancelButton
                );
            }
        };
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

        filePathTextField = new TextField();
        filePathTextField.setPrefSize(300, 35);
        Button loadFilePath = new Button("Обзор...");
        loadFilePath.setPrefSize(100, 35);
        loadFilePath.setOnAction(e -> openFileChooser());
        HBox.setHgrow(filePathTextField, Priority.ALWAYS);

        filePathBox.getChildren().addAll(
                filePathTextField,
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

        companyNameTextField = new TextField();
        companyNameTextField.setPrefWidth(400);
        companyNameTextField.setPrefHeight(35);
        dialogWindowLayout.add(companyNameTextField, 1, 0);

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
        if (Files.exists(Paths.get(filePathTextField.getText())) && validateCompanyName()) {
            //if we edit - remove from cache and put it again
            if (isForEdit) {
                SearchModelCache.getOrCreateCache().remove(searchDetails.getName());
            }

            SearchModelCache.getOrCreateCache().put(
                    companyNameTextField.getText(),
                    new SearchDetails(
                            companyNameTextField.getText(),
                            filePathTextField.getText()
                    )
            );

            dialogWindow.close();
        }
        //if searchDetails with input name already exists
        if (!validateCompanyName()) {
            //if it is not for edit
            if (!isForEdit) {
                fileErrorLabel.setVisible(false);
                nameErrorLabel.setVisible(true);
            } else {
                SearchModelCache.getOrCreateCache().put(
                        companyNameTextField.getText(),
                        new SearchDetails(
                                companyNameTextField.getText(),
                                filePathTextField.getText()
                        )
                );
                dialogWindow.close();
            }
        } else {
            nameErrorLabel.setVisible(false);
            fileErrorLabel.setVisible(true);
        }
    }

    /**
     * Opens file chooser to select path to searchDetails file
     */
    private void openFileChooser() {
        FileChooser fileChooser = new FileChooser();

        //only excel files
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter(
                "Excel files (*.xlsx, *.xls)", "*.xlsx", "*.xls");
        fileChooser.getExtensionFilters().add(extensionFilter);
        File file = fileChooser.showOpenDialog(dialogWindow);
        if (Objects.isNull(file)) {
            filePathTextField.setText("");
        } else {
            filePathTextField.setText(file.getAbsolutePath());
        }
    }

    /**
     * Create error label which appears after wrong input to file path text field
     *
     * @return false - if searchDetails name written in text field already exists in searchDetails table
     * true - if does not exist
     */
    private boolean validateCompanyName() {
        return SearchModelCache.getOrCreateCache()
                .keySet()
                .stream()
                .noneMatch(item -> item.equals(companyNameTextField.getText()));
    }
}
