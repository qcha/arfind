package qcha.arfind.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Company {
    private StringProperty name;
    private StringProperty pathToPrice;

    public Company() {
        this.name = new SimpleStringProperty("");
        this.pathToPrice = new SimpleStringProperty("");
    }

    public Company(String name, String pathToPrice) {
        this.name = new SimpleStringProperty(name);
        this.pathToPrice = new SimpleStringProperty(pathToPrice);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getPathToPrice() {
        return pathToPrice.get();
    }

    public void setPathToPrice(String pathToPrice) {
        this.pathToPrice.set(pathToPrice);
    }

    public StringProperty pathToPriceProperty() {
        return pathToPrice;
    }
}
