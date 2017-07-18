package qcha.arfind.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import qcha.arfind.model.Company;
import qcha.arfind.view.Main;

public final class CompanyOverviewController {

    private Main main;

    @FXML
    private TableView<Company> companyTableView;
    @FXML
    private TableColumn<Company, String> companyNameColumn;
    @FXML
    private TableColumn<Company, HBox> filePathColumn;

    @FXML
    private void initialize() {
        companyNameColumn.setCellValueFactory(cellData -> cellData.getValue().sourceColumnProperty());
        filePathColumn.setCellValueFactory(cellData -> cellData.getValue().filePathProperty());
    }


    public void setMain(Main main) {
        this.main = main;
        companyTableView.setItems(main.getCompanyData());
    }
}
