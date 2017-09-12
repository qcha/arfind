package qcha.arfind.view;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Getter;
import qcha.arfind.Sources;
import qcha.arfind.model.Source;

import java.util.List;

@Getter
class SearchViewModel {
    private final ObservableList<SearchResultModelDto> results;
    private final ObservableList<Source> companies;
    private final ObservableList<Source> sourcesForSearch;
    private final Stage stage;

    SearchViewModel(Stage stage) {
        this.stage = stage;

        //init companies
        companies = FXCollections.observableArrayList(Sources.getOrCreate().values());
        results = FXCollections.observableArrayList();
        sourcesForSearch = FXCollections.observableArrayList();

        //add listener for updates
        Sources.getOrCreate().addListener((MapChangeListener<String, Source>) change -> {
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
        private ObjectProperty<VBox> result;

        SearchResultModelDto(String name, List<String> result) {
            this.name = new SimpleStringProperty(name);
            this.result = new SimpleObjectProperty<>(new VBox() {
                {
                    ListView<String> listView = new ListView<String>(FXCollections.observableArrayList(result)) {
                        {
                            setOrientation(Orientation.HORIZONTAL);
                            setPrefHeight(38);
                            setCellFactory(param -> new CustomListCell(this));
                        }
                    };
                    getChildren().add(listView);

                    setVgrow(listView, Priority.ALWAYS);
                }
            });
        }
    }

    //fixme - width and height size
    private static class CustomListCell extends ListCell<String> {

        CustomListCell(ListView<String> listView) {
            // If you want to use as a separate class you can use the getListView() instead of listView.
            prefWidthProperty().bind(listView.widthProperty()
                    .divide(listView.getItems().size()) // set the width equally for each cell
                    .subtract(1)); // subtracted 1 to prevent displaying of a scrollBar, but you can play with
            // this if you have many values in the listView
        }

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setText(null);
            } else {
                setText(item);
            }
        }

    }
}
