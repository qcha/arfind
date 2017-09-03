package qcha.arfind.view;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import qcha.arfind.SearchModelCache;
import qcha.arfind.model.SearchDetails;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

import static qcha.arfind.utils.Constants.UserResolutionConstants.DEFAULT_USER_RESOLUTION_HEIGHT;
import static qcha.arfind.utils.Constants.UserResolutionConstants.DEFAULT_USER_RESOLUTION_WIDTH;

class EditSearchMetaInfoDialog extends Dialog<SearchDetails> {
    private final TextField tfName = new TextField();
    private final TextField tfPath = new TextField();
    private final Button btnChoose;

    EditSearchMetaInfoDialog(SearchDetails searchDetails) {
        Label nameErrorLabel = createErrorLabel("Такое имя уже существует");
        Label fileErrorLabel = createErrorLabel("По указанному пути файла не существует");
        Label filePathLabel = new Label("Полный путь");
        Label nameLabel = new Label("Название источника");

        btnChoose = new Button("Выбрать");
        btnChoose.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();

            //only excel files
            FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter(
                    "Excel files (*.xlsx, *.xls)", "*.xlsx", "*.xls");
            fileChooser.getExtensionFilters().add(extensionFilter);
            File file = fileChooser.showOpenDialog(this.getOwner());
            if (Objects.nonNull(file)) {
                tfPath.setText(file.getAbsolutePath());
            }
        });

        VBox vbox = new VBox(
                nameLabel,
                tfName,
                nameErrorLabel,
                filePathLabel,
                tfPath,
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
            //duplicate name
            if (!validateCompanyName(tfName.getText())) {
                nameErrorLabel.setVisible(true);
            } else {
                nameErrorLabel.setVisible(false);
            }

            //invalid path
            if (!validatePath(tfPath.getText())) {
                fileErrorLabel.setVisible(true);
            } else {
                fileErrorLabel.setVisible(false);
            }

            //if it's no visible warnings - consume
            if (nameErrorLabel.isVisible() || fileErrorLabel.isVisible()) {
                event.consume();
            }
        });


        dp.setContent(vbox);

        init(searchDetails);
    }

    private void init(SearchDetails details) {
        if (details != null) {
            tfName.setText(details.getName());
            tfPath.setText(details.getPath());
        }
    }

    private SearchDetails formResult(ButtonType bt) {
        if (bt.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
            return new SearchDetails(
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
        return SearchModelCache.getOrCreateCache()
                .keySet()
                .stream()
                .noneMatch(item -> item.equals(name));
    }

    private boolean validatePath(String path) {
        return Files.exists(Paths.get(path));
    }
}
