package qcha.arfind.utils;

import javafx.scene.control.TextField;

public class TextFieldConfiguration extends TextField {

    public TextField textField;

    public TextFieldConfiguration() {
        this.textField = new TextField();
        textField.setPromptText("Введите имя...");
    }
}
