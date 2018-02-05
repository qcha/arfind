package qcha.arfind.view;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import qcha.arfind.model.Source;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

import static qcha.arfind.utils.Constants.UserResolutionConstants.DEFAULT_USER_RESOLUTION_HEIGHT;
import static qcha.arfind.utils.Constants.UserResolutionConstants.DEFAULT_USER_RESOLUTION_WIDTH;

final class EditSearchMetaInfoDialog extends Dialog<Source> {
    private final static Logger logger = LoggerFactory.getLogger(EditSearchMetaInfoDialog.class);

    private final TextField tfName = new TextField();
    private final TextField tfPath = new TextField();
    private boolean isForEdit;
    private ObservableList<Source> existingCompanies;

    EditSearchMetaInfoDialog(ObservableList<Source> existingCompanies, Source source) {
        this.existingCompanies = existingCompanies;
        Label nameErrorLabel = createErrorLabel("Такое имя уже существует");
        Label fileErrorLabel = createErrorLabel("По указанному пути файла не существует");
        Label filePathLabel = new Label("Полный путь");
        Label nameLabel = new Label("Название источника");

        Button btnChoose = new Button("Выбрать");
        btnChoose.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();

            //only excel files
            FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter(
                    "Excel files (*.xlsx, *.xls)", "*.xlsx", "*.xls");
            fileChooser.getExtensionFilters().add(extensionFilter);
            File file = fileChooser.showOpenDialog(this.getOwner());
            if (Objects.nonNull(file)) {
                tfPath.setText(file.getAbsolutePath());
                logger.info("Working with file: {}.", file.getAbsolutePath());
            }
        });

        VBox vbox = new VBox(
                nameLabel,
                tfName,
                nameErrorLabel,
                filePathLabel,
                new HBox(tfPath, btnChoose),
                fileErrorLabel
        );

        vbox.setSpacing(10.0d);
        vbox.setPadding(new Insets(40.0d));

        DialogPane dp = getDialogPane();

        setTitle("Редактирование");
        setResultConverter(this::formResult);
        ButtonType saveBtnType = new ButtonType("Сохранить", ButtonBar.ButtonData.OK_DONE);
        dp.getButtonTypes().addAll(
                saveBtnType,
                new ButtonType("Отменить", ButtonBar.ButtonData.CANCEL_CLOSE)
        );

        Button btnOk = (Button) getDialogPane().lookupButton(saveBtnType);
        btnOk.disableProperty().bind(tfName.textProperty()
                .isEqualTo("")
                .or(tfPath.textProperty()
                        .isEqualTo("")));

        btnOk.addEventFilter(ActionEvent.ACTION, event -> {
            if (!isForEdit) {
                //check for duplicate name
                if (!validateCompanyName(tfName.getText())) {
                    nameErrorLabel.setVisible(true);
                    logger.warn("Name: {} was duplicated.", tfName.getText());
                } else {
                    nameErrorLabel.setVisible(false);
                }
            }

            //invalid path
            if (!validatePath(tfPath.getText())) {
                fileErrorLabel.setVisible(true);
                logger.warn("Path: {} is invalid", tfPath.getText());
            } else {
                fileErrorLabel.setVisible(false);
            }

            //if it's no visible warnings - consume
            if (nameErrorLabel.isVisible() || fileErrorLabel.isVisible()) {
                event.consume();
            }
        });


        dp.setContent(vbox);

        init(source);
    }

    private void init(Source details) {
        if (details != null) {
            tfName.setText(details.getName());
            tfPath.setText(details.getPath());
            isForEdit = true;
        }
    }

    private Source formResult(ButtonType bt) {
        if (bt.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
            return new Source(
                    tfName.getText(), tfPath.getText());
        }

        return null;
    }

    private Label createErrorLabel(String text) {
        Label errorLabel = new Label(text);
        errorLabel.setVisible(false);
        errorLabel.setAlignment(Pos.CENTER_LEFT);
        errorLabel.setTextFill(Color.rgb(210, 39, 30));
        errorLabel.setMinSize(0.21 * DEFAULT_USER_RESOLUTION_WIDTH,
                0.03 * DEFAULT_USER_RESOLUTION_HEIGHT);
        errorLabel.setFont(Font.font(18));
        return errorLabel;
    }

    private boolean validateCompanyName(String name) {
        return existingCompanies
                .stream()
                .noneMatch(item -> item.getName().equals(name));
    }

    private boolean validatePath(String path) {
        return Files.exists(Paths.get(path));
    }
}
