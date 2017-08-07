package qcha.arfind.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.List;

public class SearchResult {
    private StringProperty name;
    private ObjectProperty<List<String>> result;

    public SearchResult(String name, List<String> result) {
        this.name = new SimpleStringProperty(name);
        this.result = new SimpleObjectProperty<>(result);
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

    public List<String> getResult() {
        return result.get();
    }

    public void setResult(List<String> result) {
        this.result.set(result);
    }

    public ObjectProperty<List<String>> resultProperty() {
        return result;
    }
}
