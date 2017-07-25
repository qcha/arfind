package qcha.arfind;

import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import qcha.arfind.model.Company;
import qcha.arfind.utils.ConfigFileUtils;

public class MainApplication extends Application {
    private final String TITLE = "JavaFx App";
    private final int DEFAULT_WIDTH = 640;
    private final int DEFAULT_HEIGHT = 480;

    private Stage primaryStage;
    private ObservableList<String> companyList;
    private ObservableList<Company> items;
    private TableView<Company> companyTableView;
    private TextField searchLine;

    ObservableList<String> getCompanyList() {
        return companyList;
    }

    ObservableList<Company> getItems() {
        return items;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        items = FXCollections.observableArrayList();
        companyList = FXCollections.observableArrayList();
        initMainWindow(primaryStage);
    }

    /**
     * Initialize main window.
     *
     * @param primaryStage for main window.
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
                createCompanyTableView());

        rootLayout.setCenter(mainWindow);

        primaryStage.setScene(new Scene(rootLayout, DEFAULT_WIDTH, DEFAULT_HEIGHT));
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
        searchLine.setAlignment(Pos.CENTER);
        searchLine.setFocusTraversable(false);
        HBox.setHgrow(searchLine, Priority.ALWAYS);

        AnchorPane.setLeftAnchor(searcher, 0.0);
        AnchorPane.setBottomAnchor(searcher, 0.0);
        AnchorPane.setRightAnchor(searcher, 0.0);

        Button searchButton = new Button("Поиск");
        searchButton.setFocusTraversable(false);
        searchButton.setDefaultButton(true);

        searchButton.setOnAction(e -> filterData());

        searcher.getChildren().addAll(
                searchLine,
                searchButton);


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
        Menu options = new Menu("Настройки");

        MenuItem configuration = new MenuItem("Конфигурации");
        configuration.setOnAction(event -> new ConfigurationWindow(this));
        options.getItems().add(configuration);

        menuBar.getMenus().addAll(
                new Menu("Файл"),
                options,
                new Menu("О программе"));

        return menuBar;
    }

    /**
     * Create list of companies to filter search
     *
     * @return ListView company names.
     */
    private ListView createCompanyListView() {
        ListView<String> companyListView = new ListView<>();

        companyListView.setCellFactory(CheckBoxListCell.forListView(item -> {
            BooleanProperty observable = new SimpleBooleanProperty();
            observable.addListener((obs, wasSelected, isSelected) -> {
                        //todo listener to search for needed data
                    }
            );
            return observable;
        }));

        companyListView.setPrefSize(200, 455);
        companyListView.setFocusTraversable(false);
        companyListView.setItems(companyList);

        ConfigFileUtils.readCompanies(companyList);

        AnchorPane.setRightAnchor(companyListView, 200.0);
        AnchorPane.setBottomAnchor(companyListView, 25.0);
        AnchorPane.setLeftAnchor(companyListView, 0.0);
        AnchorPane.setTopAnchor(companyListView, 0.0);

        return companyListView;
    }

    /**
     * Create list of full items info to filter search
     *
     * @return TableView with 3 columns - company, item and price.
     */
    private TableView<Company> createCompanyTableView() {
        companyTableView = new TableView<>();

        companyTableView.setPrefSize(440, 455);
        companyTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        companyTableView.setFocusTraversable(false);

        TableColumn<Company, String> companyColumn = new TableColumn<>("Название фирмы");
        TableColumn<Company, String> fullItemNameColumn = new TableColumn<>("Модель товара");
        TableColumn<Company, String> priceColumn = new TableColumn<>("Цена");

        //noinspection unchecked
        companyTableView.getColumns().addAll(companyColumn, fullItemNameColumn, priceColumn);
        companyTableView.setItems(items);
        ConfigFileUtils.readFullDataToTableView(items);

        companyColumn.setCellValueFactory(cellData -> cellData.getValue().companyNameProperty());
        fullItemNameColumn.setCellValueFactory(cellData -> cellData.getValue().fullItemNameProperty());
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty());

        AnchorPane.setLeftAnchor(companyTableView, 200.0);
        AnchorPane.setBottomAnchor(companyTableView, 25.0);
        AnchorPane.setRightAnchor(companyTableView, 0.0);
        AnchorPane.setTopAnchor(companyTableView, 0.0);

        return companyTableView;
    }

    private void filterData() {
        FilteredList<Company> filteredData = new FilteredList<>(items, p -> true);

        filteredData.setPredicate(company -> company.getFullItemName().toLowerCase().contains(searchLine.getText().toLowerCase()) ||
                company.getPrice().contains(searchLine.getText()));

        SortedList<Company> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(companyTableView.comparatorProperty());
        companyTableView.setItems(sortedData);
    }

    Stage getPrimaryStage() {
        return primaryStage;
    }
}