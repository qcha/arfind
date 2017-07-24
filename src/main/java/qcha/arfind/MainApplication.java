package qcha.arfind;

import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
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
import javafx.stage.Stage;
import javafx.util.Callback;
import qcha.arfind.Constants.ConfigFileConstants;
import qcha.arfind.utils.ConfigFileUtils;

import java.nio.file.Files;
import java.nio.file.Paths;

public class MainApplication extends Application {
    private final String TITLE = "JavaFx App";
    private final int DEFAULT_WIDTH = 640;
    private final int DEFAULT_HEIGHT = 480;

    private Stage primaryStage;
    private ObservableList<String> companyList;

    ObservableList<String> getCompanyList() {
        return companyList;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
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

        AnchorPane.setLeftAnchor(searcher, 0.0);
        AnchorPane.setBottomAnchor(searcher, 0.0);
        AnchorPane.setRightAnchor(searcher, 0.0);

        Button searchButton = new Button("Поиск");
        searchButton.setFocusTraversable(false);

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
            observable.addListener((obs, wasSelected, isNowSelected) -> {
                //todo listener to search for needed data
                    }
            );
            return observable ;
        }));

        companyListView.setPrefSize(200, 455);
        companyListView.setFocusTraversable(false);
        companyListView.setItems(companyList);

        ConfigFileUtils.readConfigFileToCompanyListView(getCompanyList());

        AnchorPane.setBottomAnchor(companyListView, 25.0);
        AnchorPane.setLeftAnchor(companyListView, 0.0);
        AnchorPane.setTopAnchor(companyListView, 0.0);

        return companyListView;
    }


    Stage getPrimaryStage() {
        return primaryStage;
    }
}