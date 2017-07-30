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

public class MainApplication extends Application {
    private final String TITLE = "JavaFx App";
    private final int DEFAULT_WIDTH = 1280;
    private final int DEFAULT_HEIGHT = 1024;

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
        searchLine.setMinHeight(75);
        HBox.setHgrow(searchLine, Priority.ALWAYS);

        searcher.setMinHeight(75);

        AnchorPane.setLeftAnchor(searcher, 0.0);
        AnchorPane.setBottomAnchor(searcher, 0.0);
        AnchorPane.setRightAnchor(searcher, 0.0);


        Button searchButton = new Button("Поиск");

        searchButton.disableProperty().bind(searchLine.textProperty().isEqualTo(""));

        searchButton.setFocusTraversable(false);
        searchButton.setDefaultButton(true);
        searchButton.setMinHeight(75);
        searchButton.setMinWidth(220);
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
        configuration.setOnAction(event -> new ConfigurationWindow(this));
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
        companyListView.setMinSize(600, 455);
        companyListView.setFocusTraversable(false);
        companyListView.setItems(companyNameList);

        companyListView.setFixedCellSize(45);

        companyListView.setCellFactory(CheckBoxListCell.forListView(item -> {
            BooleanProperty observable = new SimpleBooleanProperty();
            observable.addListener((obs, wasSelected, isNowSelected) -> {
                        //todo listener to search for needed data
                    }
            );
            return observable;
        }));

        AnchorPane.setRightAnchor(companyListView, 200.0);
        AnchorPane.setBottomAnchor(companyListView, 75.0);
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

        companyTableView.setPrefSize(440, 455);
        companyTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        companyTableView.setFocusTraversable(false);

        TableColumn<String, String> companyColumn = new TableColumn<>("Название фирмы");
        TableColumn<String, String> filterResultColumn = new TableColumn<>("Результат поиска");

        //noinspection unchecked
        companyTableView.getColumns().addAll(companyColumn, filterResultColumn);

        AnchorPane.setLeftAnchor(companyTableView, 200.0);
        AnchorPane.setBottomAnchor(companyTableView, 75.0);
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
        searchButton.setMinHeight(75);
        searchButton.setMinWidth(DEFAULT_WIDTH);
        searchButton.setStyle("-fx-font: 18 arial; -fx-base: #b6e7c9;");

        searchButton.setMinHeight(75);
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