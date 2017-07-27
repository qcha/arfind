package qcha.arfind.view;

import javafx.scene.control.Button;
import javafx.scene.text.Font;

public class ButtonConfiguration extends Button {
    public ButtonConfiguration(String text) {
        super(text);
        setMinSize(100, 50);
        setFont(Font.font(14));    }
}
