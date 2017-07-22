package qcha.arfind;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import qcha.arfind.model.Company;

class ConfigurationWindow {

    private ObservableList<Company> companies;
    private Stage configurationWindow;
    private TableView<Company> companyTableView;

    ConfigurationWindow() {
        configurationWindow = new Stage();
        companies = FXCollections.observableArrayList();
        companies.add(new Company("Asus", "Jest"));
        companyTableView = createTable();
    }

    ObservableList<Company> getCompanies() {
        return companies;
    }

    TableView<Company> getCompanyTableView() {
        return companyTableView;
    }

    void createConfigurationWindow() {
        VBox configurationWindowLayout = new VBox();
        configurationWindowLayout.getChildren().addAll(createHeader(), createTable(), createButtonBar(), createSaveButton());
        Scene configurationWindowInterface = new Scene(configurationWindowLayout, Constants.ConfigurationWindow.DEFAULT_WIDTH, Constants.ConfigurationWindow.DEFAULT_HEIGHT);
        configurationWindow.setTitle(Constants.ConfigurationWindow.TITLE);
        configurationWindow.setResizable(false);
        configurationWindow.setScene(configurationWindowInterface);
        configurationWindow.initModality(Modality.WINDOW_MODAL);
        configurationWindow.initOwner(MainApplication.getPrimaryStage().getScene().getWindow());
        configurationWindow.show();

    }

    private Label createHeader() {
        Label header = new Label();
        header.setMinWidth(Constants.LabelConstants.DEFAULT_WIDTH);
        header.setText("Настройки конфигурации");
        header.setMinHeight(Constants.LabelConstants.DEFAULT_HEIGHT);
        header.setAlignment(Pos.TOP_CENTER);
        header.setTextAlignment(TextAlignment.CENTER);
        header.setFont(Font.font(18));
        return header;
    }

    private TableView<Company> createTable() {
        companyTableView = new TableView<>();
        TableColumn<Company, String> companyColumn = new TableColumn<>("Название фирмы");
        TableColumn<Company, String> filePathColumn = new TableColumn<>("Путь к файлу");

        //noinspection unchecked
        companyTableView.getColumns().addAll(companyColumn, filePathColumn);
        companyTableView.setItems(companies);
        companyTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        companyTableView.setFocusTraversable(false);

        companyColumn.setCellValueFactory(cellData -> cellData.getValue().companyNameProperty());
        filePathColumn.setCellValueFactory(cellData -> cellData.getValue().filePathProperty());

        return companyTableView;
    }

    private AnchorPane createButtonBar() {
        AnchorPane buttonBarAnchor = new AnchorPane();

        HBox buttonBar = new HBox(Constants.HBoxConstants.DEFAULT_SPACING);
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
        return buttonBarAnchor;
    }

    private AnchorPane createSaveButton() {
        AnchorPane saveButtonBarAnchor = new AnchorPane();
        HBox saveButtonBar = new HBox(Constants.HBoxConstants.DEFAULT_SPACING);
        saveButtonBar.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        Button saveButton = new Button("Сохранить");
        //saveButton.setOnAction(e -> new CompanyShowDialogs().saveConfigurations());

        saveButtonBar.getChildren().add(saveButton);
        AnchorPane.setRightAnchor(saveButtonBar, 10.0);
        saveButtonBarAnchor.getChildren().add(saveButtonBar);
        return saveButtonBarAnchor;
    }
}



