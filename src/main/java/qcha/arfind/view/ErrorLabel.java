package qcha.arfind.view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class ErrorLabel extends Label {
    public ErrorLabel(String text) {
        super(text);
        setVisible(false);
        setAlignment(Pos.CENTER);
        setTextFill(Color.rgb(210, 39, 30));
        setMinSize(400, 35);
    }
}
