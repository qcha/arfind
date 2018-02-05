package qcha.arfind.view;

import javafx.beans.binding.Bindings;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import qcha.arfind.model.Source;

import java.util.Objects;

final class ApplicationConfigurationView extends BorderPane {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationConfigurationView.class);

    private HBox controls;
    private TableView<Source> companyTableView;
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

        TableColumn<Source, String> companyColumn = new TableColumn<>("Название фирмы");
        TableColumn<Source, String> filePathColumn = new TableColumn<>("Путь к файлу");

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
            String company = companyTableView.getSelectionModel().getSelectedItem().getName();
            viewModel.remove(companyTableView.getSelectionModel().getSelectedItem());
            logger.debug("Company: {} was successfully removed.", company);
        });

        removeAllButton.setOnAction(e -> {
            viewModel.removeAll();
            logger.debug("All companies were removed.");
        });

        addButton.setOnAction(e -> {
            logger.debug("Opening dialog for adding new company.");
            viewModel.showDialogForAddingSource();
        });

        editButton.setOnAction(e -> {
            String company = companyTableView.getSelectionModel().getSelectedItem().getName();
            viewModel.showDialogForEditSearchDetails(companyTableView.getSelectionModel().getSelectedItem());
            logger.debug("Company: {} was successfully edited.", company);

        });

        saveButton.setOnAction(e -> {
            viewModel.save();
            logger.info("Configuration was successfully updated.");
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
