package qcha.arfind.utils;

import javafx.scene.control.TextField;

public class TextFieldConfiguration extends TextField {

    public TextFieldConfiguration() {
        TextField textField = new TextField();
        textField.setPromptText("Введите имя файла...");
        textField.setFocusTraversable(false);
    }
}
