package qcha.arfind;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
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
import qcha.arfind.model.SearchDetails;
import qcha.arfind.model.SearchResult;
import qchar.arfind.excel.ExcelTextFinder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static qcha.arfind.utils.Constants.ConfigFileConstants.CONFIG_FILENAME;
import static qcha.arfind.utils.Constants.UserResolutionConstants.DEFAULT_USER_RESOLUTION_HEIGHT;
import static qcha.arfind.utils.Constants.UserResolutionConstants.DEFAULT_USER_RESOLUTION_WIDTH;

public class MainApplication extends Application {
    private final String TITLE = "Поисковик артикулов";
    //this window size occupies 70% of user's display width and 90% of display height
    private final double DEFAULT_WIDTH = 0.7 * DEFAULT_USER_RESOLUTION_WIDTH;
    private final double DEFAULT_HEIGHT = 0.9 * DEFAULT_USER_RESOLUTION_HEIGHT;

    private Stage primaryStage;
    private ObservableList<String> companyNameList;
    private ListView<String> companyListView;
    private ObservableList<SearchResult> searchResults;
    private TableView<SearchResult> companyTableView;
    private TextField searchLine;
    private List<String> sourcesForSearch;
    private ObservableMap<String, SearchDetails> companiesCache;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.companiesCache = SearchModelCache.getOrCreateCache();
        this.sourcesForSearch = new ArrayList<>();

        searchResults = FXCollections.observableArrayList();

        companyNameList = FXCollections.observableArrayList(companiesCache.keySet());

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (!companiesCache.isEmpty()) {
                SearchModelCache.saveCacheToFile(companiesCache);
            } else {
                try {
                    Files.deleteIfExists(Paths.get(CONFIG_FILENAME));
                } catch (IOException e) {
                    throw new RuntimeException("Cannot find file - config.csv", e);
                }
            }
        }));

        companiesCache.addListener((MapChangeListener<String, SearchDetails>) change -> {
            if (change.wasRemoved()) {
                companyNameList.remove(change.getKey());
            }

            if (change.wasAdded()) {
                companyNameList.add(change.getKey());
            }
        });

        if (!Files.exists(Paths.get(CONFIG_FILENAME))) {
            new SetConfigurationWarning(this);
        } else {
            initMainWindow(primaryStage);
        }
    }

    /**
     * Initialize main window.
     */
    void initMainWindow(Stage primaryStage) {
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

        searchLine.setPromptText("Введите текст для поиска");
        searchLine.setFont(Font.font(18));
        searchLine.setAlignment(Pos.CENTER);
        searchLine.setFocusTraversable(false);
        searchLine.setPrefHeight(75);
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
        searchButton.setMinWidth(200);
        searchButton.setStyle("-fx-font: 18 arial; -fx-base: #b6e7c9;");

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
    private ListView<String> createCompanyListView() {
        companyListView = new ListView<>();

        companyListView.setStyle("-fx-font-size: 16px;");
        companyListView.setFocusTraversable(false);
        companyListView.setItems(companyNameList);

        companyListView.setFixedCellSize(55);
        companyListView.setPrefSize(440, 455);

        companyListView.setCellFactory(CheckBoxListCell.forListView(item -> {
            BooleanProperty observable = new SimpleBooleanProperty();
            observable.addListener((obs, wasSelected, isNowSelected) -> {
                        if (isNowSelected) {
                            sourcesForSearch.add(item);
                        }

                        if (wasSelected) {
                            sourcesForSearch.remove(item);
                        }
                    }
            );
            return observable;
        }));

        AnchorPane.setRightAnchor(companyListView, 175.0);
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
    private TableView<SearchResult> createCompanyTableView() {
        companyTableView = new TableView<>();

        companyTableView.setStyle("-fx-font-size: 14px;");
        companyTableView.setFixedCellSize(45);
        companyTableView.setPrefSize(600, 455);

        companyTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        companyTableView.setFocusTraversable(false);

        TableColumn<SearchResult, String> companyColumn = new TableColumn<>("Название фирмы");
        TableColumn<SearchResult, String> filterResultColumn = new TableColumn<>("Результат поиска");

        companyColumn.prefWidthProperty().bind(companyTableView.widthProperty().multiply(0.2));
        filterResultColumn.prefWidthProperty().bind(companyTableView.widthProperty().multiply(0.8));
        companyColumn.setResizable(false);
        filterResultColumn.setResizable(false);

        companyColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        filterResultColumn.setCellValueFactory(cellData -> cellData.getValue().resultProperty());
        //noinspection unchecked
        companyTableView.getColumns().addAll(companyColumn, filterResultColumn);
        companyTableView.setItems(searchResults);

        AnchorPane.setLeftAnchor(companyTableView, 175.0);
        AnchorPane.setBottomAnchor(companyTableView, 75.0);
        AnchorPane.setRightAnchor(companyTableView, 0.0);
        AnchorPane.setTopAnchor(companyTableView, 0.0);

        return companyTableView;
    }

    /**
     * Creates button to apply a new search
     *
     * @return Button which allows user to apply a new search
     */
    private Button createNewSearchButton() {
        Button searchButton = new Button("Новый поиск");

        searchButton.setOnAction(e -> {
            searchResults.clear();
            sourcesForSearch.clear();
            initMainWindow(primaryStage);
        });

        searchButton.setFocusTraversable(false);
        searchButton.setDefaultButton(true);
        searchButton.setMinHeight(75);
        searchButton.setMinWidth(DEFAULT_WIDTH);
        searchButton.setStyle("-fx-font: 18 arial; -fx-base: #b6e7c9;");

        AnchorPane.setLeftAnchor(searchButton, 0.0);
        AnchorPane.setBottomAnchor(searchButton, 0.0);
        AnchorPane.setRightAnchor(searchButton, 0.0);

        return searchButton;
    }

    /**
     * New scene which shows filtered data after pressing the "search" button
     *
     * @see #createSearcher()
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

        loadFilteredData();

        primaryStage.setScene(filteredScene);
        primaryStage.show();
    }

    /**
     * Loads data to show it in a new scene {@link #showFilteredData}
     */
    private void loadFilteredData() {
        sourcesForSearch.forEach(source -> {
            SearchDetails searchDetails = SearchModelCache.getOrCreateCache().get(source);
            ExcelTextFinder finder = new ExcelTextFinder(searchDetails.getPath());

            finder.findMatches(searchLine.getText())
                    .forEach(matchString ->
                            searchResults.add(new SearchResult(source, matchString))
                    );
        });
    }

    /**
     * Creates informative window when clicking on "About" menu item
     *
     * @see #createMenuBar()
     */
    private void applicationInfo() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Информация о программе");
        alert.setContentText("Данная программа производит поиск строки в заданных excel-файлах.");
        alert.showAndWait();
    }

    Stage getPrimaryStage() {
        return primaryStage;
    }
}