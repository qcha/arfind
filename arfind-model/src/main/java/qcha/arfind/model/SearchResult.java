package qcha.arfind.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SearchResult {
    private StringProperty name;
    private StringProperty result;

    public SearchResult(String name, String result) {
        this.name = new SimpleStringProperty(name);
        this.result = new SimpleStringProperty(result);
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

    public String getResult() {
        return result.get();
    }

    public void setResult(String result) {
        this.result.set(result);
    }

    public StringProperty resultProperty() {
        return result;
    }
}
