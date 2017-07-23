package qcha.arfind.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Company {

    private final StringProperty companyName;
    private final StringProperty filePath;

    public Company() {
        this.companyName = new SimpleStringProperty("");
        this.filePath = new SimpleStringProperty("");
    }

    public Company(String companyName, String filePath) {
        this.companyName = new SimpleStringProperty(companyName);
        this.filePath = new SimpleStringProperty(filePath);
    }

    public String getCompanyName() {
        return companyName.get();
    }

    public void setCompanyName(String companyName) {
        this.companyName.set(companyName);
    }

    public StringProperty companyNameProperty() {
        return companyName;
    }

    public String getFilePath() {
        return filePath.get();
    }

    public void setFilePath(String filePath) {
        this.filePath.set(filePath);
    }

    public StringProperty filePathProperty() {
        return filePath;
    }
}
