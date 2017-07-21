package qcha.arfind;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class MainApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        initMainWindow(primaryStage);
    }

    /**
     * Initialize main window.
     * @param primaryStage for main window.
     */
    private void initMainWindow(Stage primaryStage) {
        primaryStage.setTitle(Constants.MainWindow.TITLE);

        BorderPane rootLayout = new BorderPane();

        //init menu bar
        rootLayout.setTop(createMenuBar());

        AnchorPane mainWindow = new AnchorPane();
        mainWindow.getChildren().add(createSearcher());
        rootLayout.setCenter(mainWindow);

        primaryStage.setScene(new Scene(rootLayout, 640, 480));
        primaryStage.show();
    }

    /**
     * Create search line.
     * @return HBox which includes field for input text and button for search.
     */
    private HBox createSearcher() {
        HBox searcher = new HBox();

        TextField searchLine = new TextField();

        searchLine.setPromptText("Поиск");
        searchLine.setAlignment(Pos.CENTER);
        searchLine.setFocusTraversable(false);
        searchLine.setMinWidth(640);
        searchLine.setMinHeight(35);
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
     * @return MenuBar with menus: File, Options, About.
     */
    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();

        //init option menu
        Menu options = new Menu("Настройки");

        MenuItem configuration = new MenuItem("Конфигурации");
        //fixme fix after configuration window
//        configuration.setOnAction(event -> ConfigurationWindow.createAndShow());
        options.getItems().add(configuration);

        menuBar.getMenus().addAll(
                new Menu("Файл"),
                options,
                new Menu("О программе"));

        return menuBar;
    }
}