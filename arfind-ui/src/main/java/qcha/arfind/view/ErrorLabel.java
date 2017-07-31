package qcha.arfind.view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import static qcha.arfind.utils.Constants.UserResolutionConstants.DEFAULT_USER_RESOLUTION_HEIGHT;
import static qcha.arfind.utils.Constants.UserResolutionConstants.DEFAULT_USER_RESOLUTION_WIDTH;

public class ErrorLabel extends Label {
    public ErrorLabel(String text) {
        super(text);
        setVisible(false);
        setAlignment(Pos.CENTER_RIGHT);
        setTextFill(Color.rgb(210, 39, 30));
        setMinSize(0.21 * DEFAULT_USER_RESOLUTION_WIDTH,
                0.03 * DEFAULT_USER_RESOLUTION_HEIGHT);
        setFont(Font.font(18));
    }
}
