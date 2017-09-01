package qcha.arfind.view;

import javafx.collections.ObservableList;
import lombok.Getter;
import qcha.arfind.SearchModelCache;
import qcha.arfind.model.SearchDetails;

//todo
@Getter
class ApplicationConfigurationModelView {
    private ObservableList<SearchDetails> companies;

    void remove(String name) {

    }

    void removeAll() {
        SearchModelCache.getOrCreateCache().clear();
    }
}
