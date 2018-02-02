package qcha.arfind.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import lombok.Getter;
import qcha.arfind.Sources;
import qcha.arfind.model.Source;

import java.util.Optional;
import java.util.stream.Collectors;

class ApplicationConfigurationModelView {
    @Getter
    private final ObservableList<Source> companies;
    private final Stage stage;

    ApplicationConfigurationModelView(Stage stage) {
        this.stage = stage;
        companies = FXCollections.observableArrayList(Sources.getOrCreate().values());
    }

    void remove(Source details) { companies.remove(details);
    }

    void removeAll() {
        companies.clear();
    }

    void save() {
        Sources.getOrCreate().clear();
        Sources.getOrCreate().putAll(
                companies.stream().collect(Collectors.toMap(Source::getName, v -> v))
        );

        stage.close();
    }

    void showDialogForEditSearchDetails(Source forEdit) {
        Optional<Source> searchDetails = new EditSearchMetaInfoDialog(companies, forEdit).showAndWait();
        searchDetails.ifPresent(details -> {
            //need to update
            companies.remove(forEdit);
            companies.add(details);
        });
    }

    void showDialogForAddingSource() {
        Optional<Source> searchDetails = new EditSearchMetaInfoDialog(companies, null).showAndWait();
        searchDetails.ifPresent(companies::add);
    }
}
