package qcha.arfind.view;

import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * This class is responsible for working with companies(adding, editing, etc).
 */
class ApplicationConfigurationView {

    private final String TITLE = "Настройки конфигурации";

    private Stage configurationWindow;

    ApplicationConfigurationView() {
        configurationWindow = new Stage();
        configurationWindow.setScene(new Scene(new ApplicationConfigurationPaneView()));
    }

    void showAndWait() {
        configurationWindow.showAndWait();
    }

    Stage getConfigurationWindow() {
        return configurationWindow;
    }

}