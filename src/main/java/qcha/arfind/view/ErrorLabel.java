package qcha.arfind.view;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class ErrorLabel extends Label {
    public ErrorLabel() {
        this("");
    }

    public ErrorLabel(String text) {
        super(text);
        setTextFill(Color.rgb(210, 39, 30));
        setMinSize(640, 35);
    }
}
