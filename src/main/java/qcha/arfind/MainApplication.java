package qcha.arfind;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import qcha.arfind.model.Company;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

public class MainApplication extends Application {
    private final String TITLE = "JavaFx App";
    private final int DEFAULT_WIDTH = 640;
    private final int DEFAULT_HEIGHT = 480;

    private Stage primaryStage;
    private ObservableList<String> companyList;


    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
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
                createCompanyListView());

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

        TextField searchLine = new TextField();

        searchLine.setPromptText("Поиск");
        searchLine.setAlignment(Pos.CENTER);
        searchLine.setFocusTraversable(false);
        HBox.setHgrow(searchLine, Priority.ALWAYS);

        searcher.getChildren().add(searchLine);

        AnchorPane.setLeftAnchor(searcher, 0.0);
        AnchorPane.setBottomAnchor(searcher, 0.0);
        AnchorPane.setRightAnchor(searcher, 0.0);

        //todo add button for search

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

        companyList = FXCollections.observableArrayList();

        companyListView.setPrefSize(200, 430);
        companyListView.setFocusTraversable(false);
        companyListView.setItems(companyList);

        return companyListView;
    }

    Stage getPrimaryStage() {
        return primaryStage;
    }
}