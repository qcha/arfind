package qcha.arfind.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class Company {

    private final StringProperty companyName;
    private final StringProperty filePath;

    public Company() {
        this(null, null);
    }
    public Company(String sourceColumn, String filePath) {
        this.companyName = new SimpleStringProperty(sourceColumn);
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
