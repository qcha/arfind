package qcha.arfind.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import qcha.arfind.Constants;
import qcha.arfind.model.Company;
import qcha.arfind.view.Main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class ConfigController {
    private Main main;
    private ObservableList<Company> tableData;
    private List<String> tableStringData;


    @FXML
    private TableView<Company> configurationCompanyTable;
    @FXML
    private TableColumn<Company, String> companyNameColumn;
    @FXML
    private TableColumn<Company, String> filePathColumn;


    @FXML
    public void initialize() {
        tableData = FXCollections.observableArrayList();
        tableData.add(new Company("Asus", null));
        tableData.add(new Company("Apple", null));
        tableData.add(new Company("Acer", null));
        tableData.add(new Company("Dell", null));
        companyNameColumn.setCellValueFactory(cellData -> cellData.getValue().companyNameProperty());
        filePathColumn.setCellValueFactory(cellData -> cellData.getValue().filePathProperty());
    }

    @FXML
    private void addCompany() {
        Company company = new Company();
        boolean okClicked = showCompanyEditDialog(company);
        if (okClicked && company.getCompanyName() != null && company.getFilePath() != null) {
            getCompanyData().add(company);
        }
    }

    @FXML
    private void editCompany() {
        Company company = configurationCompanyTable.getSelectionModel().getSelectedItem();
        if (company != null) {
            showCompanyEditDialog(company);
        }
    }

    @FXML
    private void removeCompany() {
        int selectedIndex = configurationCompanyTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            configurationCompanyTable.getItems().remove(selectedIndex);
        }
    }

    @FXML
    private void removeAll() {
        configurationCompanyTable.getItems().clear();
    }

    @FXML
    private void saveConfigurations() {
        try {
            convertTableDataToString();
            FileUtils.writeLines(new File(Constants.ConfigFileConstants.CONFIG_FILENAME), Constants.ConfigFileConstants.DEFAULT_CHARSET, tableStringData);
            RootLayoutController.getConfigurationWindow().close();
        } catch (IOException e) {
            throw new RuntimeException("Cannot find such file", e);
        }
    }

    private ObservableList<Company> getCompanyData() {
        return tableData;
    }

    public void setMain(Main main) {
        this.main = main;
        configurationCompanyTable.setItems(getCompanyData());
    }

    private boolean showCompanyEditDialog(Company company) {
        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("../view/application-editcompany-dialog.fxml"));
            AnchorPane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Company");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(configurationCompanyTable.getScene().getWindow());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            CompanyEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setCompany(company);

            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void convertTableDataToString() {
        tableStringData = new ArrayList<>();
        for (Company company : tableData) {
            tableStringData.add(company.getCompanyName() + " " + company.getFilePath());
        }
    }
}


