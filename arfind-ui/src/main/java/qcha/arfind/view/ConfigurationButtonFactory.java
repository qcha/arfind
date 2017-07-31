package qcha.arfind.view;

import javafx.scene.control.Button;
import javafx.scene.text.Font;

public final class ConfigurationButtonFactory {

    public static Button createConfigurationButton(String text) {
        Button configButton = new Button(text);
        configButton.setMinSize(100, 50);
        configButton.setFont(Font.font(14));
        return configButton;
    }
}
