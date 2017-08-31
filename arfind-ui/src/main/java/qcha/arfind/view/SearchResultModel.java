package qcha.arfind.view;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.ListView;
import lombok.Getter;

import java.util.List;

@Getter
public class SearchResultModel {
    private StringProperty name;
    private ObjectProperty<ListView<String>> result;

    public SearchResultModel(String name, List<String> result) {
        this.name = new SimpleStringProperty(name);
        this.result = new SimpleObjectProperty<>(new ListView<>(FXCollections.observableArrayList(result)));
    }
}
