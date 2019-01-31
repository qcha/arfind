package qcha.arfind.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static qcha.arfind.utils.Constants.ConfigFileConstants.DEFAULT_FIELD_DELIMITER;

@Slf4j
@ToString
public class Source {
    private StringProperty name;
    private StringProperty pathToSource;
    private BooleanProperty isValid;
    private StringProperty size;
    private StringProperty lastModified;

    public Source(String name, String pathToSource) {
        this(name, pathToSource, true);
    }

    public Source(String name, String pathToSource, boolean isValid) {
        this.name = new SimpleStringProperty(name);
        this.pathToSource = new SimpleStringProperty(pathToSource);
        this.isValid = new SimpleBooleanProperty(isValid);

        if (isValid) {
            try {

                size = new SimpleStringProperty(String.format("%d Кб",
                        (int) (Files.size(Paths.get(pathToSource)) / FileUtils.ONE_KB) + 1)); // bytes to kilobytes
                lastModified = new SimpleStringProperty(
                        DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm").format(
                                ZonedDateTime.ofInstant(Instant.ofEpochMilli(Files.getLastModifiedTime(
                                        Paths.get(pathToSource)).toMillis()), ZoneOffset.UTC)));
            } catch (IOException e) {
                log.error("File {} does not exist.", pathToSource);
            }
        }
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

    public String getPath() {
        return pathToSource.get();
    }

    public void setPathToSource(String pathToSource) {
        this.pathToSource.set(pathToSource);
    }

    public StringProperty pathToSourceProperty() {
        return pathToSource;
    }

    public boolean isValid() {
        return isValid.getValue();
    }

    public BooleanProperty getIsValidProperty() {
        return isValid;
    }

    public void setValidTo(boolean value) {
        this.isValid.set(value);
    }

    public String getSize() {
        return size.get();
    }

    public StringProperty sizeProperty() {
        return size;
    }

    public String getLastModified() {
        return lastModified.get();
    }

    public StringProperty lastModifiedProperty() {
        return lastModified;
    }

    public String toCsv() {
        return String.format("%s%s%s%s%s", name.get(), DEFAULT_FIELD_DELIMITER, pathToSource.get(), DEFAULT_FIELD_DELIMITER, isValid.get());
    }
}
