package qcha.arfind.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import qcha.arfind.model.Company;


public class CompanyOverviewController {

    private ObservableList<String> companyNameData;

    @FXML
    private TableView<Company> companyDataTable;
    @FXML
    private TableColumn<Company, String> companyDataNameColumn;

    @FXML
    public void initialize() {
        companyNameData = FXCollections.observableArrayList();
        companyDataNameColumn.setCellValueFactory(cellData -> cellData.getValue().companyNameProperty());
    }

}
