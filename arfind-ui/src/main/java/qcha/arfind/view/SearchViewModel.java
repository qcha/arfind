package qcha.arfind.view;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import qcha.arfind.Sources;
import qcha.arfind.model.Source;

import java.util.stream.Collectors;

@Slf4j
@Getter
final class SearchViewModel {
    private final Stage stage;
    private final ObservableList<SearchSource> sourcesForSearch;

    SearchViewModel(Stage stage) {
        this.stage = stage;

        //init companies
        sourcesForSearch = FXCollections.observableArrayList(
                Sources.getOrCreate()
                        .values()
                        .stream()
                        .map(SearchSource::new)
                        .collect(Collectors.toList())
        );

        //add listener for updates
        Sources.getOrCreate().addListener((MapChangeListener<String, Source>) change -> {
            if (change.wasRemoved()) {
                if (sourcesForSearch.remove(new SearchSource(change.getValueRemoved()))) {
                    log.debug("Source: {} was removed", change.getValueRemoved().getName());
                } else {
                    log.error(
                            "Source: {} was removed from cache, but can't remove it from search view.",
                            change.getValueRemoved().getName()
                    );
                }
            }

            if (change.wasAdded()) {
                sourcesForSearch.add(new SearchSource(change.getValueAdded()));
                log.debug("Source: {} was added", change.getValueAdded().getName());
            }
        });
    }

    @ToString(callSuper = true)
    @EqualsAndHashCode(exclude = {"selected"}, callSuper = false)
    static final class SearchSource extends Source {
        private BooleanProperty selected = new SimpleBooleanProperty();

        SearchSource(Source source, boolean forSearch) {
            super(source.getName(), source.getPath());
            selected.set(forSearch);
        }

        SearchSource(Source source) {
            this(source, false);
        }

        void setSelected(boolean selected) {
            this.selected.set(selected);
        }

        boolean isSelected() {
            return selected.get();
        }

        BooleanProperty selectedProperty() {
            return selected;
        }
    }
}
