package qcha.arfind.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Source {
    private StringProperty name;
    private StringProperty pathToSource;

    public Source(String name, String pathToSource) {
        this.name = new SimpleStringProperty(name);
        this.pathToSource = new SimpleStringProperty(pathToSource);
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
}
