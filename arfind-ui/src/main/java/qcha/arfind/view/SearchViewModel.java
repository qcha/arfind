package qcha.arfind.view;

import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import qcha.arfind.Sources;
import qcha.arfind.model.Source;

@Getter
class SearchViewModel {
    private static final Logger logger = LoggerFactory.getLogger(SearchViewModel.class);

    private final ObservableList<Source> companies;
    private final ObservableList<Source> sourcesForSearch;
    private final Stage stage;

    SearchViewModel(Stage stage) {
        this.stage = stage;

        //init companies
        companies = FXCollections.observableArrayList(Sources.getOrCreate().values());
        sourcesForSearch = FXCollections.observableArrayList();

        //add listener for updates
        Sources.getOrCreate().addListener((MapChangeListener<String, Source>) change -> {
            if (change.wasRemoved()) {
                companies.remove(change.getValueRemoved());
                logger.info("Company: {} was removed", change.getValueRemoved().getName());
            }

            if (change.wasAdded()) {
                companies.add(change.getValueAdded());
                logger.info("Company: {}, was added", change.getValueAdded().getName());
            }
        });
    }
}
