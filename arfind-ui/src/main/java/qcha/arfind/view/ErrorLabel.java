package qcha.arfind.view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.awt.*;

public class ErrorLabel extends Label {
    public ErrorLabel(String text) {
        super(text);
        setVisible(false);
        setAlignment(Pos.CENTER);
        setTextFill(Color.rgb(210, 39, 30));
        setMinSize(0.21 * GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getWidth(),
                0.03 * GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getWidth());
        setFont(Font.font(18));
    }
}
