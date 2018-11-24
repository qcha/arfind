package qcha.arfind.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.ToString;

import static qcha.arfind.utils.Constants.ConfigFileConstants.DEFAULT_FIELD_DELIMITER;

@ToString
public class Source {
    private StringProperty name;
    private StringProperty pathToSource;
    private BooleanProperty isValid;

    public Source(String name, String pathToSource) {
        this(name, pathToSource, true);
    }

    public Source(String name, String pathToSource, boolean isValid) {
        this.name = new SimpleStringProperty(name);
        this.pathToSource = new SimpleStringProperty(pathToSource);
        this.isValid = new SimpleBooleanProperty(isValid);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getPath() {
        return pathToSource.get();
    }

    public void setPathToSource(String pathToSource) {
        this.pathToSource.set(pathToSource);
    }

    public StringProperty pathToSourceProperty() {
        return pathToSource;
    }

    public boolean isValid() {
        return isValid.getValue();
    }

    public BooleanProperty getIsValidProperty() {
        return isValid;
    }

    public void setValidTo(boolean value) {
        this.isValid.set(value);
    }

    public String toCsv() {
        return String.format("%s%s%s%s%s", name.get(), DEFAULT_FIELD_DELIMITER, pathToSource.get(), DEFAULT_FIELD_DELIMITER, isValid.get());
    }
}
