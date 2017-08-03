package qcha.arfind;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import qcha.arfind.model.SearchDetails;
import qcha.arfind.view.ConfigurationButtonFactory;

import java.util.stream.Collectors;

import static qcha.arfind.utils.Constants.UserResolutionConstants.DEFAULT_USER_RESOLUTION_HEIGHT;
import static qcha.arfind.utils.Constants.UserResolutionConstants.DEFAULT_USER_RESOLUTION_WIDTH;

/**
 * This class is responsible for working with companies(adding, editing, etc).
 */
class ConfigurationWindow {

    private final String TITLE = "Настройки конфигурации";

    //this window size occupies 65% of user's display width and 70% of display height
    private final double DEFAULT_WIDTH = 0.65 * DEFAULT_USER_RESOLUTION_WIDTH;
    private final double DEFAULT_HEIGHT = 0.7 * DEFAULT_USER_RESOLUTION_HEIGHT;

    private MainApplication mainApplication;
    private ObservableList<SearchDetails> companies;
    private Stage configurationWindow;
    private TableView<SearchDetails> companyTableView;
    private TableColumn<SearchDetails, String> companyColumn;
    private ObservableMap<String, SearchDetails> companiesCache;

    /**
     * Class constructor.
     */
    ConfigurationWindow(MainApplication mainApplication) {
        this.mainApplication = mainApplication;
        configurationWindow = new Stage();

        companiesCache = SearchModelCache.getOrCreateCache();

        companies = FXCollections.observableArrayList(companiesCache.values());

        companiesCache.addListener((MapChangeListener<String, SearchDetails>) change -> {
            if (change.wasRemoved()) {
                companies.remove(change.getValueRemoved());
            }

            if (change.wasAdded()) {
                companies.add(change.getValueAdded());
            }
        });

        companyTableView = createTable();
        createConfigurationWindow();
    }

    void show() {
        configurationWindow.show();
    }

    /**
     * Create initial window of configurations.
     */
    private void createConfigurationWindow() {
        VBox configurationWindowLayout = new VBox();

        configurationWindowLayout.setAlignment(Pos.CENTER);

        configurationWindowLayout.getChildren().addAll(
                createTable(),
                new AnchorPane(createEditorBar()),
                new AnchorPane(createSaveButton())
        );

        Scene configurationWindowInterface = new Scene(configurationWindowLayout, DEFAULT_WIDTH, DEFAULT_HEIGHT);

        configurationWindow.setTitle(TITLE);
        configurationWindow.setResizable(false);
        configurationWindow.setScene(configurationWindowInterface);
        configurationWindow.initModality(Modality.WINDOW_MODAL);
        configurationWindow.initOwner(mainApplication.getPrimaryStage().getScene().getWindow());

    }

    /**
     * Create table of companies.
     *
     * @return TableView<SearchDetails> with two columns - name and filepath.
     * @see SearchDetails
     */
    private TableView<SearchDetails> createTable() {
        companyTableView = new TableView<>();

        companyTableView.setFixedCellSize(40);
        companyTableView.setStyle("-fx-font-size: 16px;");
        //table view occupies 80% of window height
        companyTableView.setPrefHeight(0.8 * DEFAULT_HEIGHT);
        AnchorPane.setBottomAnchor(companyTableView, 50.0);

        companyColumn = new TableColumn<>("Название фирмы");
        TableColumn<SearchDetails, String> filePathColumn = new TableColumn<>("Путь к файлу");

        //noinspection unchecked
        companyTableView.getColumns().addAll(
                companyColumn,
                filePathColumn
        );
        companyTableView.setItems(companies);
        companyTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        companyTableView.setFocusTraversable(false);

        companyColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        filePathColumn.setCellValueFactory(cellData -> cellData.getValue().pathToSourceProperty());

        return companyTableView;
    }

    /**
     * Create editor bar.
     *
     * @return HBox with buttons for edit company.
     * @see SearchDetails
     */
    private HBox createEditorBar() {
        HBox buttonBar = new HBox(10);

        Button addButton = ConfigurationButtonFactory.createConfigurationButton("Добавить");
        Button editButton = ConfigurationButtonFactory.createConfigurationButton("Изменить");
        Button removeButton = ConfigurationButtonFactory.createConfigurationButton("Удалить");
        Button removeAllButton = ConfigurationButtonFactory.createConfigurationButton("Удалить всё");

        buttonBar.getChildren().addAll(
                addButton,
                editButton,
                removeButton,
                removeAllButton
        );

        addButton.setOnAction(e -> new EditSearchMetaInfoDialog(this, null).show());

        editButton.disableProperty().bind(Bindings.isEmpty(companyTableView.getSelectionModel().getSelectedItems()));

        editButton.setOnAction(e -> new EditSearchMetaInfoDialog(
                        this,
                        companyTableView
                                .getSelectionModel()
                                .getSelectedItem()
                ).show()
        );

        removeButton.disableProperty().bind(Bindings.isEmpty(companyTableView.getSelectionModel().getSelectedItems()));

        removeButton.setOnAction(e -> {
            int selectedIndex = companyTableView.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                companiesCache.remove(companyTableView.getItems().get(selectedIndex).getName());
            }
        });

        removeAllButton.setOnAction(e -> companiesCache.clear());

        buttonBar.setFocusTraversable(false);

        AnchorPane.setTopAnchor(buttonBar, 10.0);
        AnchorPane.setLeftAnchor(buttonBar, 10.0);

        return buttonBar;
    }

    /**
     * Create save button.
     *
     * @return Button saving configurations
     */
    private Button createSaveButton() {
        Button saveButton = new Button("Сохранить");

        saveButton.setMinSize(150, 75);
        saveButton.setFont(Font.font(17));
        saveButton.setStyle("-fx-base: #b6e7c9;");
        saveButton.setOnAction(e -> saveConfigurations());

        AnchorPane.setBottomAnchor(saveButton, 10.0);
        AnchorPane.setRightAnchor(saveButton, 10.0);

        return saveButton;
    }

    /**
     * Saves user configuration and shows it in the main window
     */
    private void saveConfigurations() {
        companiesCache.putAll(
                companies.stream().collect(Collectors.toMap(SearchDetails::getName, v -> v))
        );

        configurationWindow.close();
    }

    Stage getConfigurationWindow() {
        return configurationWindow;
    }
}