package qcha.arfind.view;

import javafx.collections.FXCollections;
import javafx.geometry.Orientation;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

import java.util.List;
import java.util.Objects;

public class ListViewConfiguration {

    public ListView<String> listViewConfiguration(List<String> data) {
        ListView<String> listView = new ListView<>();
        listView.setOrientation(Orientation.HORIZONTAL);
        listView.setCellFactory(param -> new ListCell<String>() {
            {
                prefWidthProperty().bind(listView.widthProperty().divide(data.size()));
            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (Objects.nonNull(item) && !empty) {
                    setText(item);
                } else {
                    setText(null);
                }
            }
        });
        listView.setItems(FXCollections.observableArrayList(data));
        return listView;
    }
}
