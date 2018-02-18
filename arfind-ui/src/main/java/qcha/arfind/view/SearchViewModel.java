package qcha.arfind.view;

import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import qcha.arfind.Sources;
import qcha.arfind.model.Source;

import java.util.stream.Collectors;

@Getter
class SearchViewModel {
    private static final Logger logger = LoggerFactory.getLogger(SearchViewModel.class);

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
                    logger.debug("Source: {} was removed", change.getValueRemoved().getName());
                } else {
                    logger.error(
                            "Source: {} was removed from cache, but can't remove it from search view.",
                            change.getValueRemoved().getName()
                    );
                }
            }

            if (change.wasAdded()) {
                if (sourcesForSearch.add(new SearchSource(change.getValueAdded()))) {
                    logger.debug("Source: {} was added", change.getValueAdded().getName());
                } else {
                    logger.error(
                            "Source: {} was added from cache, but can't add it from search view.",
                            change.getValueAdded().getName()
                    );
                }
            }
        });
    }

    @Getter
    @Setter
    @ToString(callSuper = true)
    @EqualsAndHashCode(exclude = {"isForSearch"}, callSuper = false)
    static final class SearchSource extends Source {
        private boolean isForSearch;

        SearchSource(Source source, boolean forSearch) {
            super(source.getName(), source.getPath());
            this.isForSearch = forSearch;
        }

        SearchSource(Source source) {
            this(source, false);
        }
    }
}
