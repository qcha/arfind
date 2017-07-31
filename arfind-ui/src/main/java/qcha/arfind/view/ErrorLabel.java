package qcha.arfind.view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import static qcha.arfind.utils.Constants.UserResolutionConstants.DEFAULT_USER_RESOLUTION_HEIGHT;
import static qcha.arfind.utils.Constants.UserResolutionConstants.DEFAULT_USER_RESOLUTION_WIDTH;

public final class ErrorLabel {
    public static Label createErrorLabel(String text) {
        Label errorLabel = new Label(text);
        errorLabel.setVisible(false);
        errorLabel.setAlignment(Pos.CENTER_LEFT);
        errorLabel.setTextFill(Color.rgb(210, 39, 30));
        errorLabel.setMinSize(0.21 * DEFAULT_USER_RESOLUTION_WIDTH,
                0.03 * DEFAULT_USER_RESOLUTION_HEIGHT);
        errorLabel.setFont(Font.font(18));
        return errorLabel;
    }
}
