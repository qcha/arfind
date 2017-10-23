package qcha.arfind.view;

import javafx.scene.Scene;
import javafx.stage.Stage;

class ConfigurationWarningWindow {
    private final String TITLE = "Задайте конфигурацию";

    private Stage warningWindow;

    ConfigurationWarningWindow(Stage owner) {
        warningWindow = new Stage() {
            {
                setTitle(TITLE);
                initOwner(owner);
                setScene(new Scene(new EmptyConfigurationWarningView(new EmptyConfigurationWarningViewModel(this))));
            }
        };
    }

    void showAndWait() {
        warningWindow.showAndWait();
    }

}
