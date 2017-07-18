package qcha.arfind.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import qcha.arfind.model.Company;
import qcha.arfind.view.Main;

public final class ConfigController {
    private Main main;

    @FXML
    private TableView<Company> configurationCompanyTableView;
    @FXML
    private TableColumn<Company, String> companyNameColumn;
    @FXML
    private TableColumn<Company, HBox> filePathColumn;


    @FXML
    public void initialize() {
        companyNameColumn.setCellValueFactory(cellData -> cellData.getValue().sourceColumnProperty());
        filePathColumn.setCellValueFactory(cellData -> cellData.getValue().filePathProperty());
    }

    public void setMain(Main main) {
        this.main = main;
        configurationCompanyTableView.setItems(main.getCompanyData());
    }

    @FXML
    private void removeCompany() {
        int selectedIndex = configurationCompanyTableView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            configurationCompanyTableView.getItems().remove(selectedIndex);
        }
    }

    @FXML
    private void removeAll() {
        configurationCompanyTableView.getItems().clear();
    }

}

