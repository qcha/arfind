package qcha.arfind.view;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import qcha.arfind.Constants;
import qcha.arfind.controller.CompanyOverviewController;
import qcha.arfind.model.Company;

import java.io.IOException;

public class Main extends Application {
    private Stage primaryStage;
    private BorderPane rootLayout;

    private ObservableList<Company> tableData = FXCollections.observableArrayList();

    public Main() {
        tableData.add(new Company("Asus"));
        tableData.add(new Company("Apple"));
        tableData.add(new Company("Acer"));
        tableData.add(new Company("Dell"));
    }

    public ObservableList<Company> getCompanyData() {
        return tableData;
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle(Constants.MainWindow.TITLE);
        loadRootLayout();
        loadMainWindow();
    }

    private void loadRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("../view/root-layout.fxml"));
            rootLayout = loader.load();
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            throw new RuntimeException("Cannot find fxml for loading root");
        }
    }

    private void loadMainWindow() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("../view/main-window.fxml"));
            AnchorPane mainWindow = loader.load();
            rootLayout.setCenter(mainWindow);
            CompanyOverviewController controller = loader.getController();
            controller.setMain(this);
        } catch (IOException e) {
            throw new RuntimeException("Cannot find fxml for loading main window");
        }
    }

}
