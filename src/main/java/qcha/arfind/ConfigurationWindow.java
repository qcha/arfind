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
import javafx.stage.Modality;
import javafx.stage.Stage;
import qcha.arfind.model.Company;
import qcha.arfind.utils.ConfigFileUtils;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static qcha.arfind.Constants.ConfigFileConstants.CONFIG_FILENAME;

class ConfigurationWindow {

    private final String TITLE = "Настройки конфигурации";
    private final int DEFAULT_WIDTH = 800;
    private final int DEFAULT_HEIGHT = 600;

    private MainApplication mainApplication;
    private ObservableList<Company> companies;
    private Stage configurationWindow;
    private TableView<Company> companyTableView;

    ConfigurationWindow(MainApplication mainApplication) {
        this.mainApplication = mainApplication;
        configurationWindow = new Stage();
        companies = FXCollections.observableArrayList(ConfigFileUtils.readCompanies());
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

        companyTableView.setMinHeight(525);
        AnchorPane.setBottomAnchor(companyTableView,50.0);

        TableColumn<Company, String> companyColumn = new TableColumn<>("Название фирмы");
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
        HBox buttonBar = new HBox(Constants.HBoxConstants.DEFAULT_SPACING);

        Button addButton = new Button("Добавить");
        Button editButton = new Button("Изменить");
        Button removeButton = new Button("Удалить");
        Button removeAllButton = new Button("Удалить всё");

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

        saveButton.setLayoutX(550);
        saveButton.setOnAction(e -> saveConfigurations());

        AnchorPane.setBottomAnchor(saveButton, 10.0);
        AnchorPane.setRightAnchor(saveButton, 10.0);

        return saveButton;
    }

    private void saveConfigurations() {
        if (!Files.exists(Paths.get(CONFIG_FILENAME))) mainApplication.getFirstLoadStage().close();

        ConfigFileUtils.saveCompanies(getCompanyData());
        mainApplication.getCompanyList().clear();
        ObservableList<String> newCompanyList = FXCollections.observableList(ConfigFileUtils.readCompanyNames());
        mainApplication.getCompanyListView().setItems(newCompanyList);

        configurationWindow.close();
    }

    ObservableList<Company> getCompanies() {
        return companies;
    }

    TableView<Company> getCompanyTableView() {
        return companyTableView;
    }

    private List<String> getCompanyData() {
        List<String> companyData = new ArrayList<>();

        for (Company company : companies) {
            companyData.add(String.format(
                    "%s;%s",
                    company.getName(),
                    company.getPathToPrice()
            ));
        }
        return companyData;
    }


}