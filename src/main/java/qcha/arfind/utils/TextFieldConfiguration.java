package qcha.arfind.utils;

import javafx.scene.control.TextField;

public class TextFieldConfiguration extends TextField {

    public TextField textField = new TextField();

    public TextFieldConfiguration() {
        textField.setPromptText("Введите имя...");
    }
}
