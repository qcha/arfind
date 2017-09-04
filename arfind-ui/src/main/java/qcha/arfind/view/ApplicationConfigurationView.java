package qcha.arfind.view;

import javafx.beans.binding.Bindings;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import qcha.arfind.SearchModelCache;
import qcha.arfind.model.SearchDetails;

import java.util.Optional;
import java.util.stream.Collectors;

class ApplicationConfigurationView extends BorderPane {
    private TableView<SearchDetails> companyTableView;
    private HBox controls;
    private ApplicationConfigurationModelView viewModel;

    ApplicationConfigurationView(ApplicationConfigurationModelView viewModel) {
        this.viewModel = viewModel;

        initTable();
        companyTableView.setItems(this.viewModel.getCompanies());

        initControlPanel();

        setCenter(companyTableView);
        setBottom(controls);
    }

    private void initTable() {
        companyTableView = new TableView<>();

        companyTableView.setFixedCellSize(40);
        companyTableView.setStyle("-fx-font-size: 16px;");

        TableColumn<SearchDetails, String> companyColumn = new TableColumn<>("Название фирмы");
        TableColumn<SearchDetails, String> filePathColumn = new TableColumn<>("Путь к файлу");

        //noinspection unchecked
        companyTableView.getColumns().addAll(
                companyColumn,
                filePathColumn
        );

        companyTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        companyColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        filePathColumn.setCellValueFactory(cellData -> cellData.getValue().pathToSourceProperty());
    }

    private void initControlPanel() {
        controls = new HBox();

        Button addButton = createButtonWithPreferenceSizeAndFont("Добавить");
        Button editButton = createButtonWithPreferenceSizeAndFont("Изменить");
        Button removeButton = createButtonWithPreferenceSizeAndFont("Удалить");
        Button removeAllButton = createButtonWithPreferenceSizeAndFont("Удалить всё");

        //save button with different color
        Button saveButton = createButtonWithPreferenceSizeAndFont("Сохранить");
        saveButton.setStyle("-fx-base: #b6e7c9;");

        removeButton.disableProperty().bind(Bindings.isEmpty(companyTableView.getSelectionModel().getSelectedItems()));
        removeButton.setOnAction(e -> {
            int selectedIndex = companyTableView.getSelectionModel().getSelectedIndex();
            viewModel.remove(companyTableView.getItems().get(selectedIndex));
        });

        removeAllButton.setOnAction(e -> viewModel.removeAll());

        addButton.setOnAction(e -> {
            Optional<SearchDetails> searchDetails = new EditSearchMetaInfoDialog(null).showAndWait();
            searchDetails.ifPresent(details -> viewModel.getCompanies().add(details));
        });

        editButton.setOnAction(e -> {
            int selectedIndex = companyTableView.getSelectionModel().getSelectedIndex();
            SearchDetails forEdit = companyTableView.getItems().get(selectedIndex);
            Optional<SearchDetails> searchDetails = new EditSearchMetaInfoDialog(forEdit).showAndWait();
            searchDetails.ifPresent(details -> {
                //need to update
                viewModel.getCompanies().remove(forEdit);
                viewModel.getCompanies().remove(details);
            });
        });

        saveButton.setOnAction(e -> {
            SearchModelCache.getOrCreateCache().clear();
            SearchModelCache.getOrCreateCache().putAll(
                    viewModel.getCompanies().stream().collect(Collectors.toMap(SearchDetails::getName, v -> v))
            );

            viewModel.getStage().close();
        });

        controls.getChildren().addAll(
                addButton,
                editButton,
                removeButton,
                removeAllButton,
                saveButton
        );

        editButton.disableProperty().bind(Bindings.isEmpty(companyTableView.getSelectionModel().getSelectedItems()));

        HBox.setHgrow(saveButton, Priority.ALWAYS);
        HBox.setHgrow(addButton, Priority.ALWAYS);
        HBox.setHgrow(editButton, Priority.ALWAYS);
        HBox.setHgrow(removeButton, Priority.ALWAYS);
        HBox.setHgrow(removeAllButton, Priority.ALWAYS);

        saveButton.setMaxWidth(Double.MAX_VALUE);
        addButton.setMaxWidth(Double.MAX_VALUE);
        editButton.setMaxWidth(Double.MAX_VALUE);
        removeButton.setMaxWidth(Double.MAX_VALUE);
        removeAllButton.setMaxWidth(Double.MAX_VALUE);
    }

    private Button createButtonWithPreferenceSizeAndFont(String text) {
        return new Button(text) {
            {
                setMinSize(150, 75);
                setFont(Font.font(17));
            }
        };
    }
}
