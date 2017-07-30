package qcha.arfind;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

import static qcha.arfind.utils.Constants.UserResolutionConstants.DEFAULT_USER_RESOLUTION_HEIGHT;
import static qcha.arfind.utils.Constants.UserResolutionConstants.DEFAULT_USER_RESOLUTION_WIDTH;

/**
 * This class is responsible for creating warning window and asks user to set configuration
 */
class SetConfigurationWarning {
    private final double DEFAULT_WIDTH = 0.65 * DEFAULT_USER_RESOLUTION_WIDTH;
    private final double DEFAULT_HEIGHT = 0.9 * DEFAULT_USER_RESOLUTION_HEIGHT;
    private Stage warningWindow;
    private MainApplication mainApplication;


    /**
     * Class constructor.
     */
    SetConfigurationWarning(MainApplication mainApplication) {
        this.mainApplication = mainApplication;

        warningWindow = new Stage();

        AnchorPane layout = new AnchorPane();
        layout.getChildren().addAll(
                createConfigurationButton()
        );
        Scene mainScene = new Scene(layout, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        warningWindow.setScene(mainScene);
        warningWindow.setResizable(false);
        warningWindow.initModality(Modality.WINDOW_MODAL);
        warningWindow.initOwner(mainApplication.getPrimaryStage().getScene().getWindow());

        warningWindow.show();
    }

    /**
     * Create button for this window with text - "Set configuration"
     *
     * @return Button which opens configuration window
     */
    private Button createConfigurationButton() {
        Button warningButton = new Button();

        warningButton.setText("Задайте конфигурацию ПО");
        warningButton.setFont(Font.font(28));
        warningButton.setMinWidth(DEFAULT_WIDTH);
        warningButton.setMinHeight(0.55 * DEFAULT_HEIGHT);
        warningButton.setAlignment(Pos.CENTER);
        warningButton.setTextAlignment(TextAlignment.CENTER);
        AnchorPane.setTopAnchor(warningButton, 0.196 * DEFAULT_HEIGHT);

        warningButton.setOnAction(e -> {
            new ConfigurationWindow(mainApplication);
            warningWindow.close();
        });

        return warningButton;
    }

}
