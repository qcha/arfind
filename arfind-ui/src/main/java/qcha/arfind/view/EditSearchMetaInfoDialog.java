package qcha.arfind.view;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import qcha.arfind.model.SearchDetails;

//todo hide error labels
public class EditSearchMetaInfoDialog extends Dialog<SearchDetails> {
    private final TextField tfName = new TextField();
    private final TextField tfPath = new TextField();

    public EditSearchMetaInfoDialog(SearchDetails searchDetails) {
        Label nameErrorLabel = new Label("Такое имя уже существует");
        Label fileErrorLabel = new Label("Такая фирма уже существует");
        Label filePathLabel = new Label("Полный путь");
        Label nameLabel = new Label("Название источника");

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

        ButtonType btOk = new ButtonType("Сохранить", ButtonBar.ButtonData.OK_DONE);
        ButtonType btCancel = new ButtonType("Отменить", ButtonBar.ButtonData.CANCEL_CLOSE);
        dp.getButtonTypes().addAll(btOk, btCancel);
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
}
