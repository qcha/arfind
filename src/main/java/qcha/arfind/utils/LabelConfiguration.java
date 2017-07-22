package qcha.arfind.utils;

import javafx.geometry.Pos;
import javafx.scene.control.Label;

public class LabelConfiguration extends Label {
    public Label label;

    public LabelConfiguration() {
        this.label = new Label();
        label.setMinWidth(100);
        label.setAlignment(Pos.CENTER);
    }
}
