package qcha.arfind.view;

import javafx.stage.Stage;
import lombok.Getter;

@Getter
class EmptyConfigurationWarningViewModel {
    private final Stage stage;

    EmptyConfigurationWarningViewModel(Stage stage) {
        this.stage = stage;
    }

    void closeWindow() {
        stage.close();
    }
}
