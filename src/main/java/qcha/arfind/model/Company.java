package qcha.arfind.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import qcha.arfind.utils.TextFieldConfiguration;


public class Company {

    private final StringProperty companyName;
    private final SimpleObjectProperty<HBox> filePath;
    private final TextFieldConfiguration textFieldConfiguration;
    private final Button loadButton;

    public Company() {
        this(null);
    }

    public Company(String sourceColumn) {
        HBox hBox = new HBox();
        textFieldConfiguration = new TextFieldConfiguration();
        loadButton = new Button("...");
        hBox.getChildren().addAll(textFieldConfiguration, loadButton);
        HBox.setHgrow(textFieldConfiguration, Priority.ALWAYS);
        this.companyName = new SimpleStringProperty(sourceColumn);
        this.filePath = new SimpleObjectProperty<>(hBox);
    }

    public String getSourceColumn() {
        return companyName.get();
    }

    public void setSourceColumn(String sourceColumn) {
        this.companyName.set(sourceColumn);
    }

    public StringProperty sourceColumnProperty() {
        return companyName;
    }

    public HBox getFilePath() {
        return filePath.get();
    }

    public void setFilePath(HBox filePath) {
        this.filePath.set(filePath);
    }

    public ObjectProperty<HBox> filePathProperty() {
        return filePath;
    }


}
