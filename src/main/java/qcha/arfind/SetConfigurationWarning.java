package qcha.arfind;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * This class is responsible for creating warning window and asks user to set configuration
 *
 */
class SetConfigurationWarning {
    private final int DEFAULT_WIDTH = 1280;
    private final int DEFAULT_HEIGHT = 1024;
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
                setHeader(),
                setConfigurationButton()
        );
        Scene mainScene = new Scene(layout, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        warningWindow.setScene(mainScene);
        //making window modal
        warningWindow.initModality(Modality.WINDOW_MODAL);
        warningWindow.initOwner(mainApplication.getPrimaryStage().getScene().getWindow());

        warningWindow.show();
    }

    /**
     * Create header for this window with text - "Set configuration"
     * @return Label
     */
    private Label setHeader() {
        Label header = new Label();

        header.setMinWidth(DEFAULT_WIDTH);
        header.setText("Задайте конфигурацию");
        header.setMinHeight(30);
        header.setAlignment(Pos.CENTER);
        header.setTextAlignment(TextAlignment.CENTER);
        header.setFont(Font.font(28));
        AnchorPane.setTopAnchor(header, 200.0);

        return header;
    }

    /**
     * Create button for this window with text - "Set configuration"
     * @return Button which opens configuration window
     */
    private Button setConfigurationButton() {
        Button warningButton = new Button();

        warningButton.setText("Задайте конфигурацию ПО");
        warningButton.setFont(Font.font(28));
        warningButton.setMinWidth(DEFAULT_WIDTH);
        warningButton.setMinHeight(300);
        warningButton.setAlignment(Pos.CENTER);
        warningButton.setTextAlignment(TextAlignment.CENTER);
        AnchorPane.setTopAnchor(warningButton, 250.0);

        warningButton.setOnAction(e -> new ConfigurationWindow(mainApplication, this));

        return warningButton;
    }

    Stage getWarningWindow() {
        return warningWindow;
    }
}
