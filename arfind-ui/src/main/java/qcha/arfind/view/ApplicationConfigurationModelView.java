package qcha.arfind.view;

import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import lombok.Getter;
import qcha.arfind.SearchModelCache;
import qcha.arfind.model.SearchDetails;

import java.util.Optional;
import java.util.stream.Collectors;

class ApplicationConfigurationModelView {
    @Getter
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
        companies.remove(details);
    }

    void removeAll() {
        companies.clear();
    }

    void save() {
        SearchModelCache.getOrCreateCache().clear();
        SearchModelCache.getOrCreateCache().putAll(
                companies.stream().collect(Collectors.toMap(SearchDetails::getName, v -> v))
        );

        stage.close();
    }

    void showDialogForEditSearchDetails(SearchDetails forEdit) {
        Optional<SearchDetails> searchDetails = new EditSearchMetaInfoDialog(forEdit).showAndWait();
        searchDetails.ifPresent(details -> {
            //need to update
            companies.remove(forEdit);
            companies.add(details);
        });
    }

    void showDialogForAddingSource() {
        Optional<SearchDetails> searchDetails = new EditSearchMetaInfoDialog(null).showAndWait();
        searchDetails.ifPresent(companies::add);
    }
}
