package qcha.arfind.view;

import javafx.scene.control.Button;
import javafx.scene.text.Font;

public class ConfigurationButton extends Button {
    public ConfigurationButton(String text) {
        super(text);
        setMinSize(100, 50);
        setFont(Font.font(14));
    }
}
