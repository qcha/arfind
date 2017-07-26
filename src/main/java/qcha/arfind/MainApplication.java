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
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import qcha.arfind.utils.ConfigFileUtils;

import java.nio.file.Files;
import java.nio.file.Paths;

import static qcha.arfind.Constants.ConfigFileConstants.CONFIG_FILENAME;

public class MainApplication extends Application {
    private final String TITLE = "JavaFx App";
    private final int DEFAULT_WIDTH = 640;
    private final int DEFAULT_HEIGHT = 480;

    private Stage primaryStage;
    private Stage firstLoadStage;
    private ObservableList<String> companyList;
    private ListView<String> companyListView;
    private TableView<String> companyTableView;
    private TextField searchLine;

    ObservableList<String> getCompanyList() {
        return companyList;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        companyList = FXCollections.observableArrayList(ConfigFileUtils.readCompanyNames());
        initMainWindow(primaryStage);
        if (!Files.exists(Paths.get(CONFIG_FILENAME))) initFirstLoadWindow();
    }

    /**
     * Initialize window during the first start of application.
     */
    private void initFirstLoadWindow() {
        firstLoadStage = new Stage();

        AnchorPane firstWindow = new AnchorPane();

        firstWindow.getChildren().addAll(
                createHeader(),
                createConfigurationButton()
        );

        Scene firstLoadScene = new Scene(firstWindow, DEFAULT_WIDTH, DEFAULT_HEIGHT);

        firstLoadStage.initModality(Modality.WINDOW_MODAL);
        firstLoadStage.initOwner(getPrimaryStage().getScene().getWindow());
        firstLoadStage.setScene(firstLoadScene);
        firstLoadStage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
        firstLoadStage.show();
    }

    /**
     * Create header for the first load window.
     *
     * @return Label with header text.
     */
    private Label createHeader() {
        Label header = new Label();

        header.setMinWidth(640);
        header.setText("Задайте конфигурацию");
        header.setMinHeight(30);
        header.setAlignment(Pos.CENTER);
        header.setTextAlignment(TextAlignment.CENTER);
        header.setFont(Font.font(28));
        AnchorPane.setTopAnchor(header, 150.0);

        return header;
    }

    /**
     * Create button for the first load window.
     *
     * @return Button which opens configuration window.
     */
    private Button createConfigurationButton() {
        Button configButton = new Button();
        configButton.setText("Задайте конфигурацию ПО");
        configButton.setFont(Font.font(20));
        configButton.setMinWidth(640);
        configButton.setAlignment(Pos.CENTER);
        configButton.setTextAlignment(TextAlignment.CENTER);
        AnchorPane.setTopAnchor(configButton, 220.0);

        configButton.setOnAction(e -> new ConfigurationWindow(this));

        return configButton;
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
                createCompanyTableView()
        );

        rootLayout.setCenter(mainWindow);

        primaryStage.setScene(new Scene(
                rootLayout,
                DEFAULT_WIDTH,
                DEFAULT_HEIGHT)
        );
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

        //todo
//       searchButton.setOnAction(e -> filterData());

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
        companyListView = new ListView<>();

        companyListView.setPrefSize(200, 455);
        companyListView.setFocusTraversable(false);
        companyListView.setItems(companyList);

        companyListView.setCellFactory(CheckBoxListCell.forListView(item -> {
            BooleanProperty observable = new SimpleBooleanProperty();
            observable.addListener((obs, wasSelected, isNowSelected) -> {
                        //todo listener to search for needed data
                    }
            );
            return observable;
        }));

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
        AnchorPane.setBottomAnchor(companyTableView, 25.0);
        AnchorPane.setRightAnchor(companyTableView, 0.0);
        AnchorPane.setTopAnchor(companyTableView, 0.0);

        return companyTableView;
    }

    ListView<String> getCompanyListView() {
        return companyListView;
    }

    Stage getPrimaryStage() {
        return primaryStage;
    }

    Stage getFirstLoadStage() {
        return firstLoadStage;
    }
}