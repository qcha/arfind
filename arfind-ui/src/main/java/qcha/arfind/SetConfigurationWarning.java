package qcha.arfind;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import static qcha.arfind.utils.Constants.UserResolutionConstants.DEFAULT_USER_RESOLUTION_HEIGHT;
import static qcha.arfind.utils.Constants.UserResolutionConstants.DEFAULT_USER_RESOLUTION_WIDTH;

/**
 * This class is responsible for creating warning window and asks user to set configuration
 */
class SetConfigurationWarning {
    //this window size occupies 70% of user's display width and 90% of display height
    private final double DEFAULT_WIDTH = 0.7 * DEFAULT_USER_RESOLUTION_WIDTH;
    private final double DEFAULT_HEIGHT = 0.9 * DEFAULT_USER_RESOLUTION_HEIGHT;
    private Stage warningWindow;
    private MainApplication mainApplication;

    /**
     * Class constructor.
     */
    SetConfigurationWarning(MainApplication mainApplication) {
        this.mainApplication = mainApplication;

        warningWindow = new Stage();

        VBox layout = new VBox();
        layout.getChildren().addAll(
                createConfigurationButton()
        );

        Scene mainScene = new Scene(layout);
        warningWindow.setScene(mainScene);
        warningWindow.setResizable(false);
        warningWindow.setOnCloseRequest(e -> mainApplication.initMainWindow(mainApplication.getPrimaryStage()));

        warningWindow.show();
    }

    /**
     * Create button for this window with text - "Set configuration"
     *
     * @return Button which opens configuration window
     */
    private Button createConfigurationButton() {
        Button warningButton = new Button();

        warningButton.setText("Задайте список компаний и источников для поиска");
        warningButton.setFont(Font.font(28));
        warningButton.setMinSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        warningButton.setAlignment(Pos.CENTER);
        warningButton.setTextAlignment(TextAlignment.CENTER);
        AnchorPane.setTopAnchor(warningButton, 200.0);

        warningButton.setOnAction(e -> {
            mainApplication.initMainWindow(mainApplication.getPrimaryStage());
            new ConfigurationWindow(mainApplication).show();
            warningWindow.close();
        });

        return warningButton;
    }

}
