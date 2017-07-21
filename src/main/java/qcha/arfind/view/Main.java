package qcha.arfind.view;
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
import qcha.arfind.Constants;
import qcha.arfind.controller.ConfigurationWindowInterface;

public class Main extends Application {
    private static MenuBar menuBar;
    private Stage primaryStage;
    private BorderPane rootLayout;
    private AnchorPane mainWindow;
    private HBox searchBox;
    private TextField searchField;

    public static MenuBar getMenuBar() {
        return menuBar;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle(Constants.MainWindow.TITLE);
        loadRootLayout();
        loadMainWindow();

    }

    private void loadRootLayout() {
        rootLayout = new BorderPane();
        menuBar = new MenuBar();

        final Menu fileMenu = new Menu("Файл");
        final Menu optionsMenu = new Menu("Настройки");
        final Menu aboutMenu = new Menu("О программе");

        MenuItem configurations = new MenuItem("Конфигурации");
        optionsMenu.getItems().add(configurations);

        configurations.setOnAction(event -> new ConfigurationWindowInterface().loadConfigurations());

        menuBar.getMenus().addAll(fileMenu, optionsMenu, aboutMenu);
        rootLayout.setTop(menuBar);
        Scene scene = new Scene(rootLayout, 640, 480);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void loadMainWindow() {
        mainWindow = new AnchorPane();
        searchBox = new HBox();

        searchField = new TextField();
        searchField.setPromptText("Поиск");
        searchField.setAlignment(Pos.CENTER);
        searchField.setFocusTraversable(false);
        searchField.setMinWidth(640);
        searchField.setMinHeight(35);

        HBox.setHgrow(searchField, Priority.ALWAYS);
        searchBox.getChildren().add(searchField);

        AnchorPane.setLeftAnchor(searchBox, 0.0);
        AnchorPane.setBottomAnchor(searchBox, 0.0);
        AnchorPane.setRightAnchor(searchBox, 0.0);

        mainWindow.getChildren().add(searchBox);
        rootLayout.setCenter(mainWindow);
    }
}