package qcha.arfind.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import qcha.arfind.model.Company;
import qcha.arfind.view.Main;

import java.util.ArrayList;
import java.util.List;


public class ConfigurationWindowInterface {

    private static ObservableList<Company> tableData;
    private static List<String> tableStringData;
    private static Stage configurationWindow;

    private static TableView<Company> configurationCompanyTable;

    static ObservableList<Company> getCompanyData() {
        return tableData;
    }
    static TableView<Company> getConfigurationCompanyTable() {
        return configurationCompanyTable;
    }

    static Stage getConfigurationWindow() {
        return configurationWindow;
    }


    @SuppressWarnings({"unchecked"})
    public void loadConfigurations() {
        configurationWindow = new Stage();
        VBox configurationWindowLayout = new VBox();

        Label configurationSettings = new Label();
        configurationSettings.setMinWidth(640);
        configurationSettings.setText("Настройки конфигурации");
        configurationSettings.setMinHeight(30);
        configurationSettings.setAlignment(Pos.TOP_CENTER);
        configurationSettings.setTextAlignment(TextAlignment.CENTER);
        configurationSettings.setFont(Font.font(18));

        configurationCompanyTable = new TableView<>();
        TableColumn<Company, String> companyNameColumn = new TableColumn<>("Название фирмы");
        TableColumn<Company, String> filePathColumn = new TableColumn<>("Путь к файлу");
        tableData = FXCollections.observableArrayList();
        tableData.add(new Company("Asus", null));
        tableData.add(new Company("Apple", null));
        tableData.add(new Company("Acer", null));
        tableData.add(new Company("Dell", null));
        configurationCompanyTable.getColumns().addAll(companyNameColumn, filePathColumn);
        configurationCompanyTable.setItems(tableData);
        configurationCompanyTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        configurationCompanyTable.setFocusTraversable(false);

        companyNameColumn.setCellValueFactory(cellData -> cellData.getValue().companyNameProperty());
        filePathColumn.setCellValueFactory(cellData -> cellData.getValue().filePathProperty());

        AnchorPane buttonBarAnchor = new AnchorPane();

        HBox buttonBar = new HBox(10);
        Button addButton = new Button("Добавить");
        addButton.setOnAction(e -> new CompanyShowDialogs().addCompany());
        Button editButton = new Button("Изменить");
        editButton.setOnAction(e -> new CompanyShowDialogs().editCompany());
        Button removeButton = new Button("Удалить");
        removeButton.setOnAction(e -> new CompanyShowDialogs().removeCompany());
        Button removeAllButton = new Button("Удалить всё");
        removeAllButton.setOnAction(e -> new CompanyShowDialogs().removeAll());
        buttonBar.getChildren().addAll(addButton, editButton, removeButton, removeAllButton);
        buttonBar.setFocusTraversable(false);

        buttonBarAnchor.getChildren().add(buttonBar);
        AnchorPane.setLeftAnchor(buttonBar, 10.0);

        AnchorPane saveButtonBarAnchor = new AnchorPane();
        HBox saveButtonBar = new HBox(10);
        saveButtonBar.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        Button saveButton = new Button("Сохранить");
        saveButton.setOnAction(e -> new CompanyShowDialogs().saveConfigurations());
        saveButtonBar.getChildren().add(saveButton);
        AnchorPane.setRightAnchor(saveButtonBar, 10.0);
        saveButtonBarAnchor.getChildren().add(saveButtonBar);


        configurationWindowLayout.getChildren().addAll(configurationSettings, configurationCompanyTable, buttonBarAnchor, saveButtonBarAnchor);
        Scene configurationWindowInterface = new Scene(configurationWindowLayout, 640, 480);
        configurationWindow.setTitle("Конфигуарации компаний");
        configurationWindow.setResizable(false);
        configurationWindow.setScene(configurationWindowInterface);
        configurationWindow.initModality(Modality.WINDOW_MODAL);
        configurationWindow.initOwner(Main.getMenuBar().getScene().getWindow());
        configurationWindow.show();
    }

    static void convertTableDataToString() {
        tableStringData = new ArrayList<>();
        for (Company company : tableData) {
            tableStringData.add(String.format("%s,%s", company.getCompanyName(), company.getFilePath()));
        }
    }
    static List<String> getTableStringData() {
        return tableStringData;
    }
}



