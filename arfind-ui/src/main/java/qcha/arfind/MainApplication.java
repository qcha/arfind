package qcha.arfind;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import qcha.arfind.model.Company;
import qcha.arfind.utils.ConfigFileUtils;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static qcha.arfind.utils.Constants.ConfigFileConstants.CONFIG_FILENAME;
import static qcha.arfind.utils.Constants.UserResolutionConstants.DEFAULT_USER_RESOLUTION_HEIGHT;
import static qcha.arfind.utils.Constants.UserResolutionConstants.DEFAULT_USER_RESOLUTION_WIDTH;

public class MainApplication extends Application {
    private final String TITLE = "JavaFx App";
    private final double DEFAULT_WIDTH = 0.65 * DEFAULT_USER_RESOLUTION_WIDTH;
    private final double DEFAULT_HEIGHT = 0.9 * DEFAULT_USER_RESOLUTION_HEIGHT;

    private Stage primaryStage;
    private ObservableList<String> companyNameList;
    private ListView<String> companyListView;
    private TableView<String> companyTableView;
    private TextField searchLine;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        companyNameList = FXCollections.observableArrayList(
                ConfigFileUtils.extractCompanyNames(ConfigFileUtils.readCompanies())
        );

        initMainWindow(primaryStage);

        if (!Files.exists(Paths.get(CONFIG_FILENAME))) {
            new SetConfigurationWarning(this);
        }
    }

    /**
     * Initialize main window.
     */
    private void initMainWindow(Stage primaryStage) {
        primaryStage.setTitle(TITLE);

        BorderPane rootLayout = new BorderPane();

        //init menu bar
        rootLayout.setTop(createMenuBar());

        AnchorPane mainWindow = new AnchorPane();

        mainWindow.getChildren().addAll(
                createSearcher(),
                createCompanyListView(),
                createCompanyTableView()
        );

        Scene mainScene = new Scene(
                rootLayout,
                DEFAULT_WIDTH,
                DEFAULT_HEIGHT
        );

        rootLayout.setCenter(mainWindow);

        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    /**
     * Create search line.
     *
     * @return HBox which includes field for input text and button for search.
     */
    private HBox createSearcher() {
        HBox searcher = new HBox();

        searchLine = new TextField();

        searchLine.setPromptText("Поиск");
        searchLine.setFont(Font.font(18));
        searchLine.setAlignment(Pos.CENTER);
        searchLine.setFocusTraversable(false);
        searchLine.setMinHeight(0.07 * DEFAULT_HEIGHT);
        HBox.setHgrow(searchLine, Priority.ALWAYS);

        searcher.setMinHeight(0.07 * DEFAULT_HEIGHT);

        AnchorPane.setLeftAnchor(searcher, 0.0);
        AnchorPane.setBottomAnchor(searcher, 0.0);
        AnchorPane.setRightAnchor(searcher, 0.0);


        Button searchButton = new Button("Поиск");

        searchButton.disableProperty().bind(searchLine.textProperty().isEqualTo(""));

        searchButton.setFocusTraversable(false);
        searchButton.setDefaultButton(true);
        searchButton.setMinHeight(0.07 * DEFAULT_HEIGHT);
        searchButton.setMinWidth(0.115 * DEFAULT_WIDTH);
        searchButton.setStyle("-fx-font: 18 arial; -fx-base: #b6e7c9;");

        //todo
        searchButton.setOnAction(e -> showFilteredData());

        searcher.getChildren().addAll(
                searchLine,
                searchButton
        );


        return searcher;
    }

    /**
     * Create menu bar.
     *
     * @return MenuBar with menus: File, Options, About.
     */
    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();

        //init option menu
        Menu file = new Menu("Файл");

        MenuItem exit = new MenuItem("Выход");
        exit.setOnAction(event -> {
            Platform.exit();
            System.exit(0);
        });

        file.getItems().add(exit);

        Menu options = new Menu("Настройки");
        MenuItem configuration = new MenuItem("Конфигурация");
        configuration.setOnAction(event -> new ConfigurationWindow(this).show());
        options.getItems().add(configuration);

        Menu help = new Menu("Помощь");
        MenuItem about = new MenuItem("О программе");
        about.setOnAction(event -> applicationInfo());
        help.getItems().add(about);

        menuBar.getMenus().addAll(
                file,
                options,
                help
        );

        return menuBar;
    }

    /**
     * Create list of companies to filter search
     *
     * @return ListView company names.
     */
    private ListView createCompanyListView() {
        companyListView = new ListView<>();

        companyListView.setStyle("-fx-font-size: 16px;");
        companyListView.setPrefSize(0.3125 * DEFAULT_WIDTH, 0.42 * DEFAULT_HEIGHT);
        companyListView.setFocusTraversable(false);
        companyListView.setItems(companyNameList);

        companyListView.setFixedCellSize(0.042 * DEFAULT_HEIGHT);

        companyListView.setCellFactory(CheckBoxListCell.forListView(item -> {
            BooleanProperty observable = new SimpleBooleanProperty();
            observable.addListener((obs, wasSelected, isNowSelected) -> {
                        //todo listener to search for needed data
                    }
            );
            return observable;
        }));

        AnchorPane.setRightAnchor(companyListView, 0.25 * DEFAULT_WIDTH);
        AnchorPane.setBottomAnchor(companyListView, 0.07 * DEFAULT_HEIGHT);
        AnchorPane.setLeftAnchor(companyListView, 0.0);
        AnchorPane.setTopAnchor(companyListView, 0.0);

        return companyListView;
    }

    /**
     * Create list of full items info to filter search
     *
     * @return TableView with 3 columns - company, item and price.
     */
    private TableView<String> createCompanyTableView() {
        companyTableView = new TableView<>();

        companyTableView.setPrefSize(0.23 * DEFAULT_WIDTH, 0.42 * DEFAULT_HEIGHT);
        companyTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        companyTableView.setFocusTraversable(false);

        TableColumn<String, String> companyColumn = new TableColumn<>("Название фирмы");
        TableColumn<String, String> filterResultColumn = new TableColumn<>("Результат поиска");

        //noinspection unchecked
        companyTableView.getColumns().addAll(companyColumn, filterResultColumn);

        AnchorPane.setLeftAnchor(companyTableView, 0.25 * DEFAULT_WIDTH);
        AnchorPane.setBottomAnchor(companyTableView, 0.07 * DEFAULT_HEIGHT);
        AnchorPane.setRightAnchor(companyTableView, 0.0);
        AnchorPane.setTopAnchor(companyTableView, 0.0);

        return companyTableView;
    }

    /**
     * Creates informative window when clicking on "About" menu item
     */
    private void applicationInfo() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Информация о программе");
        alert.setHeaderText(null);
        alert.setContentText("Данная программа производит поиск товаров по артикулам из excel-файла.");
        alert.showAndWait();
    }

    /**
     * Creates button to apply a new search
     *
     * @return Button which allows user to apply a new search
     */
    private Button createNewSearchButton() {
        Button searchButton = new Button("Новый поиск");

        searchButton.setOnAction(e -> initMainWindow(primaryStage));

        searchButton.setFocusTraversable(false);
        searchButton.setDefaultButton(true);
        searchButton.setMinHeight(0.07 * DEFAULT_HEIGHT);
        searchButton.setMinWidth(DEFAULT_WIDTH);
        searchButton.setStyle("-fx-font: 18 arial; -fx-base: #b6e7c9;");

        searchButton.setMinHeight(0.07 * DEFAULT_HEIGHT);
        AnchorPane.setLeftAnchor(searchButton, 0.0);
        AnchorPane.setBottomAnchor(searchButton, 0.0);
        AnchorPane.setRightAnchor(searchButton, 0.0);

        return searchButton;
    }

    /**
     * New scene which shows filtered data after pressing the "search" button
     */

    private void showFilteredData() {
        BorderPane rootLayout = new BorderPane();
        //init menu bar
        rootLayout.setTop(createMenuBar());

        AnchorPane mainWindow = new AnchorPane();

        companyTableView.setMinWidth(DEFAULT_WIDTH);
        AnchorPane.setLeftAnchor(companyTableView, 0.0);

        mainWindow.getChildren().addAll(
                createNewSearchButton(),
                companyTableView
        );

        rootLayout.setCenter(mainWindow);

        Scene filteredScene = new Scene(
                rootLayout,
                DEFAULT_WIDTH,
                DEFAULT_HEIGHT
        );

        getPrimaryStage().setScene(filteredScene);
        getPrimaryStage().show();
    }

    void updateCompaniesListView(List<Company> companies) {
        companyListView.getItems().clear();
        companyNameList.removeAll();
        companyNameList.addAll(ConfigFileUtils.extractCompanyNames(companies));
    }

    Stage getPrimaryStage() {
        return primaryStage;
    }
}