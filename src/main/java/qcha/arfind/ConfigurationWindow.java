package qcha.arfind;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class ConfigurationWindow {

    private final String TITLE = "Настройки конфигурации";
    private final int DEFAULT_WIDTH = 600;
    private final int DEFAULT_HEIGHT = 400;

    private MainApplication mainApplication;
    private ObservableList<Company> companies;
    private Stage configurationWindow;
    private TableView<Company> companyTableView;

    ConfigurationWindow(MainApplication mainApplication) {
        this.mainApplication = mainApplication;
        configurationWindow = new Stage();
        companies = FXCollections.observableArrayList();
        ConfigFileUtils.readConfigFileToCompanyTableView(getCompanies());
        companyTableView = createTable();
        createConfigurationWindow();
        configurationWindow.show();

    }

    ObservableList<Company> getCompanies() {
        return companies;
    }

    TableView<Company> getCompanyTableView() {
        return companyTableView;
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

        TableColumn<Company, String> companyColumn = new TableColumn<>("Название фирмы");
        TableColumn<Company, String> filePathColumn = new TableColumn<>("Путь к файлу");

        //noinspection unchecked
        companyTableView.getColumns().addAll(companyColumn, filePathColumn);
        companyTableView.setItems(companies);
        companyTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        companyTableView.setFocusTraversable(false);

        companyColumn.setCellValueFactory(cellData -> cellData.getValue().companyNameProperty());
        filePathColumn.setCellValueFactory(cellData -> cellData.getValue().filePathProperty());

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

        editButton.setOnAction(e -> {
            if (Objects.nonNull(getCompanyTableView().getSelectionModel().getSelectedItem()))
                new EditCompanyDialog(this, getCompanyTableView()
                        .getSelectionModel()
                        .getSelectedItem());
        });

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
        ConfigFileUtils.saveDataToConfigFile(getCompanyData());
        mainApplication.getCompanyList().clear();
        ConfigFileUtils.readConfigFileToCompanyListView(mainApplication.getCompanyList());
        configurationWindow.close();
    }

    private List<String> getCompanyData() {
        List<String> companyData = new ArrayList<>();

        for (Company company : companies) {
            companyData.add(String.format("%s;%s",
                    company.getCompanyName(),
                    company.getFilePath()));
        }

        return companyData;
    }
}