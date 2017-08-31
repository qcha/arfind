package qcha.arfind.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import qcha.arfind.SearchModelCache;
import qcha.arfind.model.SearchDetails;

@Getter
class SearchViewModel {
    private ObservableList<SearchResultModel> results;
    private ObservableList<SearchDetails> companies;
    private ObservableList<SearchDetails> sourcesForSearch;

    SearchViewModel() {
        //init companies
        companies = FXCollections.observableArrayList(SearchModelCache.getOrCreateCache().values());
        results = FXCollections.observableArrayList();
        sourcesForSearch = FXCollections.observableArrayList();
    }
}
