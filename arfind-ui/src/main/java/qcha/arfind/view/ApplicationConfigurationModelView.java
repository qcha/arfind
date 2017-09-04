package qcha.arfind.view;

import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import lombok.Getter;
import qcha.arfind.SearchModelCache;
import qcha.arfind.model.SearchDetails;

//todo
@Getter
class ApplicationConfigurationModelView {
    private final ObservableList<SearchDetails> companies;
    private final Stage stage;

    ApplicationConfigurationModelView(Stage stage) {
        this.stage = stage;
        companies = FXCollections.observableArrayList(SearchModelCache.getOrCreateCache().values());
        SearchModelCache.getOrCreateCache().addListener((MapChangeListener<String, SearchDetails>) change -> {
            if (change.wasRemoved()) {
                companies.remove(change.getValueRemoved());
            }

            if (change.wasAdded()) {
                companies.add(change.getValueAdded());
            }
        });
    }

    void remove(SearchDetails details) {
        SearchModelCache.getOrCreateCache().remove(details.getName());
    }

    void removeAll() {
        SearchModelCache.getOrCreateCache().clear();
    }
}
