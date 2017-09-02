package qcha.arfind.view;

import javafx.beans.binding.Bindings;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import qcha.arfind.SearchModelCache;
import qcha.arfind.model.SearchDetails;

import java.util.Optional;
import java.util.stream.Collectors;

public class ApplicationConfigurationPaneView extends BorderPane {
    private TableView<SearchDetails> companyTableView;
    private HBox controls;
    private ApplicationConfigurationModelView modelView;

    public ApplicationConfigurationPaneView() {
        modelView = new ApplicationConfigurationModelView();

        initTable();
        companyTableView.setItems(modelView.getCompanies());

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

        companyColumn.setResizable(false);
        filePathColumn.setResizable(false);

        //noinspection unchecked
        companyTableView.getColumns().addAll(
                companyColumn,
                filePathColumn
        );

        companyTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        companyTableView.setFocusTraversable(false);

        companyColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        filePathColumn.setCellValueFactory(cellData -> cellData.getValue().pathToSourceProperty());
    }

    private void initControlPanel() {
        controls = new HBox(10);
        Button addButton = createConfigurationButton("Добавить");
        Button editButton = createConfigurationButton("Изменить");
        Button removeButton = createConfigurationButton("Удалить");
        Button removeAllButton = createConfigurationButton("Удалить всё");
        Button saveButton = new Button("Сохранить") {
            {
                setMinSize(150, 75);
                setFont(Font.font(17));
                setStyle("-fx-base: #b6e7c9;");
            }
        };

        removeButton.disableProperty().bind(Bindings.isEmpty(companyTableView.getSelectionModel().getSelectedItems()));
        removeButton.setOnAction(e -> {
            int selectedIndex = companyTableView.getSelectionModel().getSelectedIndex();
            modelView.remove(companyTableView.getItems().get(selectedIndex).getName());
        });

        removeAllButton.setOnAction(e -> modelView.removeAll());

        addButton.setOnAction(e -> {
            Optional<SearchDetails> searchDetails = new EditSearchMetaInfoDialog(null).showAndWait();
            searchDetails.ifPresent(details -> SearchModelCache.getOrCreateCache().put(details.getName(), details));
        });

        editButton.setOnAction(e -> {
            int selectedIndex = companyTableView.getSelectionModel().getSelectedIndex();
            SearchDetails forEdit = companyTableView.getItems().get(selectedIndex);
            Optional<SearchDetails> searchDetails = new EditSearchMetaInfoDialog(forEdit).showAndWait();
            searchDetails.ifPresent(details -> {
                SearchModelCache.getOrCreateCache().remove(forEdit.getName());
                SearchModelCache.getOrCreateCache().put(details.getName(), details);
            });
        });

        saveButton.setOnAction(e -> {
            SearchModelCache.getOrCreateCache().putAll(
                    modelView.getCompanies().stream().collect(Collectors.toMap(SearchDetails::getName, v -> v))
            );
        });

        controls.getChildren().addAll(
                addButton,
                editButton,
                removeButton,
                removeAllButton,
                saveButton
        );

        editButton.disableProperty().bind(Bindings.isEmpty(companyTableView.getSelectionModel().getSelectedItems()));

        controls.setFocusTraversable(false);
    }

    private Button createConfigurationButton(String text) {
        Button configButton = new Button(text);
        configButton.setMinSize(100, 50);
        configButton.setFont(Font.font(14));
        return configButton;
    }
}
