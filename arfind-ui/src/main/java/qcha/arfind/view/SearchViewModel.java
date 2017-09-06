package qcha.arfind.view;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import lombok.Getter;
import qcha.arfind.SearchModelCache;
import qcha.arfind.model.SearchDetails;

import java.util.List;

@Getter
class SearchViewModel {
    private final ObservableList<SearchResultModelDto> results;
    private final ObservableList<SearchDetails> companies;
    private final ObservableList<SearchDetails> sourcesForSearch;
    private final Stage stage;

    SearchViewModel(Stage stage) {
        this.stage = stage;

        //init companies
        companies = FXCollections.observableArrayList(SearchModelCache.getOrCreateCache().values());
        results = FXCollections.observableArrayList();
        sourcesForSearch = FXCollections.observableArrayList();

        //add listener for updates
        SearchModelCache.getOrCreateCache().addListener((MapChangeListener<String, SearchDetails>) change -> {
            if (change.wasRemoved()) {
                companies.remove(change.getValueRemoved());
            }

            if (change.wasAdded()) {
                companies.add(change.getValueAdded());
            }
        });
    }

    @Getter
    static class SearchResultModelDto {
        private StringProperty name;
        private ObjectProperty<ListView<String>> result;

        SearchResultModelDto(String name, List<String> result) {
            this.name = new SimpleStringProperty(name);
            this.result = new SimpleObjectProperty<>(new ListView<String>(FXCollections.observableArrayList(result)) {
                {
                    setOrientation(Orientation.HORIZONTAL);
                    setPrefHeight(35);
                }
            });
        }
    }
}
