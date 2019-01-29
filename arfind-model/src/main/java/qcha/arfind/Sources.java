package qcha.arfind;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.scene.control.Alert;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import qcha.arfind.model.Source;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static qcha.arfind.utils.Constants.ConfigFileConstants.*;

@Slf4j
public final class Sources {
    private static ObservableMap<String, Source> cache;

    public static synchronized ObservableMap<String, Source> getOrCreate() {
        if (Objects.isNull(cache)) {
            log.debug("Creating new cache.");
            cache = createCache();
            log.info("Cache was created.");
        }

        return cache;
    }

    public static void saveCacheToFile() {
        try {
            log.debug("Trying to save cache to file {}.", CONFIG_FILENAME);

            List<String> data = cache.values().stream()
                    .map(Source::toCsv)
                    .collect(Collectors.toList());

            FileUtils.writeLines(
                    new File(CONFIG_FILENAME),
                    DEFAULT_CHARSET,
                    data);

            log.info("Cache was successfully saved to file {}.", CONFIG_FILENAME);
        } catch (IOException e) {
            log.error("Cannot save data to file {}, cause: {}.", CONFIG_FILENAME, e);
            throw new UncheckedIOException("Cannot save data to file: " + CONFIG_FILENAME, e);
        }
    }

    private static ObservableMap<String, Source> createCache() {
        ObservableMap<String, Source> cache = FXCollections.observableHashMap();

        if (Files.exists(Paths.get(CONFIG_FILENAME))) {
            try {
                List<String> lines = FileUtils.readLines(new File(CONFIG_FILENAME), DEFAULT_CHARSET);

                lines.forEach(line -> {
                    String[] fields = line.split(DEFAULT_FIELD_DELIMITER);
                    Source source = createSource(fields[0], fields[1]);
                    cache.put(source.getName(), source);
                });
            } catch (IOException e) {
                log.error("Cannot read file {}, cause: {}.", CONFIG_FILENAME, e);
                throw new InitConfigurationException("Cannot read file: " + CONFIG_FILENAME, e);
            }
        }

        return cache;
    }

    private static Source createSource(String name, String path) {
        Source source = new Source(name, path, new File(path).exists());
        if (!source.isValid()) {
            log.error("File {} does not exist.", name);
        }
        log.debug("Line {};{};{} added to file {}.", name, path, source.isValid(), CONFIG_FILENAME);
        return source;
    }
}
