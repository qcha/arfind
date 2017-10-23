package qcha.arfind.view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

class EmptyConfigurationWarningView extends VBox {
    EmptyConfigurationWarningView(EmptyConfigurationWarningViewModel emptyConfigurationWarningViewModel) {
        Button warningButton = new Button("Файл конфигурации пуст или отсутствует - задайте конфигурацию");
        warningButton.setFont(Font.font(28));
        warningButton.setAlignment(Pos.CENTER);
        warningButton.setTextAlignment(TextAlignment.CENTER);

        warningButton.setOnAction(e -> {
            emptyConfigurationWarningViewModel.closeWindow();
            new ApplicationConfigurationWindow(emptyConfigurationWarningViewModel.getStage()).showAndWait();
        });

        getChildren().add(warningButton);
    }
}
