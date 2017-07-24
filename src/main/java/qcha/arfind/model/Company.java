package qcha.arfind.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Company {

    private StringProperty companyName;
    private StringProperty filePath;
    private StringProperty fullItemName;
    private StringProperty price;

    public Company() {
        this.companyName = new SimpleStringProperty("");
        this.filePath = new SimpleStringProperty("");
    }

    public Company(String companyName, String filePath) {
        this.companyName = new SimpleStringProperty(companyName);
        this.filePath = new SimpleStringProperty(filePath);
    }

    public Company(String companyName, String fullItemName, String price) {
        this.companyName = new SimpleStringProperty(companyName);
        this.fullItemName = new SimpleStringProperty(fullItemName);
        this.price = new SimpleStringProperty(price);
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


    public String getFullItemName() {
        return fullItemName.get();
    }

    public void setFullItemName(String fullItemName) {
        this.fullItemName.set(fullItemName);
    }

    public StringProperty fullItemNameProperty() {
        return fullItemName;
    }

    public String getPrice() {
        return price.get();
    }

    public void setPrice(String price) {
        this.price.set(price);
    }

    public StringProperty priceProperty() {
        return price;
    }
}
