package qcha.arfind.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.ListView;

public class SearchResult {
    private StringProperty name;
    private ObjectProperty<ListView<String>> result;

    public SearchResult(String name, ListView<String> result) {
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

    public ListView<String> getResult() {
        return result.get();
    }

    public void setResult(ListView<String> result) {
        this.result.set(result);
    }

    public ObjectProperty<ListView<String>> resultProperty() {
        return result;
    }
}
