package qcha.arfind;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
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
                    try {
                        Source source = new Source(fields[0], fields[1], Boolean.valueOf(fields[2]));
                        cache.put(source.getName(), source);
                        log.debug("Line {} added to file {}.", line, CONFIG_FILENAME);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        log.error("Error occurs while parsing line: {}, maybe it's old configuration?", line);
                        log.warn("Ignore line of configuration: {}.", line);
                    }
                });
            } catch (IOException e) {
                log.error("Cannot read file {}, cause: {}.", CONFIG_FILENAME, e);
                throw new InitConfigurationException("Cannot read file: " + CONFIG_FILENAME, e);
            }
        }

        return cache;
    }
}
