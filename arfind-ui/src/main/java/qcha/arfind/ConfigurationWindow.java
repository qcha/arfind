package qcha.arfind;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import qcha.arfind.model.Company;
import qcha.arfind.utils.ConfigFileUtils;
import qcha.arfind.view.ConfigurationButton;

import java.util.ArrayList;
import java.util.List;

import static qcha.arfind.utils.ConfigFileUtils.readCompanies;
import static qcha.arfind.utils.Constants.HBoxConstants.DEFAULT_SPACING;

/**
 * This class is responsible for working with companies(adding, editing, etc).
 */
class ConfigurationWindow {

    private final String TITLE = "Настройки конфигурации";
    private final int DEFAULT_WIDTH = 1024;
    private final int DEFAULT_HEIGHT = 768;

    private MainApplication mainApplication;
    private ObservableList<Company> companies;
    private Stage configurationWindow;
    private TableView<Company> companyTableView;
    private TableColumn<Company, String> companyColumn;

    /**
     * Class constructor.
     */
    ConfigurationWindow(MainApplication mainApplication) {
        this.mainApplication = mainApplication;
        configurationWindow = new Stage();
        companies = FXCollections.observableArrayList(readCompanies());
        companyTableView = createTable();
        createConfigurationWindow();
        configurationWindow.show();

    }

    /**
     * Create initial window of configurations.
     */
    private void createConfigurationWindow() {
        VBox configurationWindowLayout = new VBox();

        configurationWindowLayout.getChildren().addAll(
                createTable(),
                new AnchorPane(createEditorBar()),
                new AnchorPane(createSaveButton())
        );

        Scene configurationWindowInterface = new Scene(
                configurationWindowLayout,
                DEFAULT_WIDTH,
                DEFAULT_HEIGHT
        );

        configurationWindow.setTitle(TITLE);
        configurationWindow.setResizable(false);
        configurationWindow.setScene(configurationWindowInterface);
        configurationWindow.initModality(Modality.WINDOW_MODAL);
        configurationWindow.initOwner(mainApplication.getPrimaryStage().getScene().getWindow());

    }

    /**
     * Create table of companies.
     *
     * @return TableView<Company> with two columns - name and filepath.
     * @see Company
     */
    private TableView<Company> createTable() {
        companyTableView = new TableView<>();

        companyTableView.setFixedCellSize(45);
        companyTableView.setStyle("-fx-font-size: 16px;");
        companyTableView.setMinHeight(625);
        AnchorPane.setBottomAnchor(companyTableView, 50.0);

        companyColumn = new TableColumn<>("Название фирмы");
        TableColumn<Company, String> filePathColumn = new TableColumn<>("Путь к файлу");

        //noinspection unchecked
        companyTableView.getColumns().addAll(
                companyColumn,
                filePathColumn
        );
        companyTableView.setItems(companies);
        companyTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        companyTableView.setFocusTraversable(false);

        companyColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        filePathColumn.setCellValueFactory(cellData -> cellData.getValue().pathToPriceProperty());

        return companyTableView;
    }

    /**
     * Create editor bar.
     *
     * @return HBox with buttons for edit company.
     * @see Company
     */
    private HBox createEditorBar() {
        HBox buttonBar = new HBox(DEFAULT_SPACING);

        ConfigurationButton addButton = new ConfigurationButton("Добавить");
        ConfigurationButton editButton = new ConfigurationButton("Изменить");
        ConfigurationButton removeButton = new ConfigurationButton("Удалить");
        ConfigurationButton removeAllButton = new ConfigurationButton("Удалить всё");

        buttonBar.getChildren().addAll(
                addButton,
                editButton,
                removeButton,
                removeAllButton
        );

        addButton.setOnAction(e -> new EditCompanyDialog(this, null));

        editButton.disableProperty().bind(Bindings.isEmpty(getCompanyTableView().getSelectionModel().getSelectedItems()));

        editButton.setOnAction(e -> new EditCompanyDialog(this, getCompanyTableView()
                .getSelectionModel()
                .getSelectedItem()));

        removeButton.disableProperty().bind(Bindings.isEmpty(getCompanyTableView().getSelectionModel().getSelectedItems()));

        removeButton.setOnAction(e -> {
            int selectedIndex = getCompanyTableView().getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                getCompanyTableView().getItems().remove(selectedIndex);
            }
        });

        removeAllButton.setOnAction(e -> getCompanyTableView().getItems().clear());

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

        saveButton.setMinSize(150, 60);
        saveButton.setFont(Font.font(17));
        saveButton.setStyle("-fx-base: #b6e7c9;");
        saveButton.setLayoutX(550);
        saveButton.setOnAction(e -> saveConfigurations());

        AnchorPane.setBottomAnchor(saveButton, 10.0);
        AnchorPane.setRightAnchor(saveButton, 10.0);

        return saveButton;
    }

    /**
     * Saves user configuration and shows it in the main window
     */
    private void saveConfigurations() {

        ConfigFileUtils.saveCompanies(companies);
        mainApplication.updateCompaniesListView(companies);

        configurationWindow.close();
    }

    ObservableList<Company> getCompanies() {
        return companies;
    }

    TableView<Company> getCompanyTableView() {
        return companyTableView;
    }

    List<String> getCompanyColumnData() {
        List<String> companyColumnData = new ArrayList<>();
        for (Company item : getCompanyTableView().getItems()) {
            companyColumnData.add(companyColumn.getCellObservableValue(item).getValue());
        }
        return companyColumnData;
    }
}