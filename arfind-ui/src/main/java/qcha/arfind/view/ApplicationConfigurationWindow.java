package qcha.arfind.view;

import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * This class is responsible for working with companies(adding, editing, etc).
 */
class ApplicationConfigurationWindow {

    private final String TITLE = "Настройки конфигурации";

    private Stage configurationWindow;

    ApplicationConfigurationWindow(Stage owner) {
        configurationWindow = new Stage() {
            {
                setTitle(TITLE);
                initOwner(owner);
                setScene(new Scene(new ApplicationConfigurationView(new ApplicationConfigurationModelView(this))));
            }
        };
    }

    void showAndWait() {
        configurationWindow.initModality(Modality.APPLICATION_MODAL);
        configurationWindow.showAndWait();
    }
}