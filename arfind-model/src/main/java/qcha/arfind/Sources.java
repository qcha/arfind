package qcha.arfind;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import qcha.arfind.model.Source;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static qcha.arfind.utils.Constants.ConfigFileConstants.*;

public final class Sources {
    private final static Logger logger = LoggerFactory.getLogger(Sources.class);
    private static ObservableMap<String, Source> cache;

    public static synchronized ObservableMap<String, Source> getOrCreate() {
        if (Objects.isNull(cache)) {
            logger.debug("Creating new cache.");
            cache = createNewCache();
            logger.info("Cache was created.");
        }

        return cache;
    }

    public static void saveCacheToFile() {
        try {
            logger.debug("Trying to save cache to file {}.", CONFIG_FILENAME);
            FileUtils.writeLines(
                    new File(CONFIG_FILENAME),
                    DEFAULT_CHARSET,
                    cacheToLines()
            );
            logger.info("Cache was successfully saved to file {}.", CONFIG_FILENAME);
        } catch (IOException e) {
            logger.error("Cannot save data to file {}, cause: {}.", CONFIG_FILENAME, e);
            throw new RuntimeException(String.format("Cannot save data to file: %s", CONFIG_FILENAME), e);
        }
    }

    private static ObservableMap<String, Source> createNewCache() {
        return FXCollections.observableMap(getAll());
    }

    private static ObservableMap<String, Source> getAll() {
        ObservableMap<String, Source> cache = FXCollections.observableHashMap();

        if (Files.exists(Paths.get(CONFIG_FILENAME))) {
            try (FileReader fr = new FileReader(CONFIG_FILENAME); BufferedReader br = new BufferedReader(fr)){
                br.lines().forEach(line -> {
                    String[] fields = line.split(DEFAULT_FIELD_DELIMITER);
                    Source source = new Source(fields[0], fields[1]);
                    cache.put(source.getName(), source);
                    logger.debug("Line {} added to file {}.", line, CONFIG_FILENAME);
                });

                return cache;
            } catch (IOException e) {
                logger.error("Cannot read file {}, cause: {}.", CONFIG_FILENAME, e);
                throw new InitConfigurationException(String.format("Cannot read file - %s", CONFIG_FILENAME), e);
            }
        }

        return cache;
    }

    private static List<String> cacheToLines() {
        logger.debug("Preparing data for writing to file {}", CONFIG_FILENAME);
        return cache.values().stream()
                .map(detail -> String.format("%s%s%s", detail.getName(), DEFAULT_FIELD_DELIMITER, detail.getPath()))
                .collect(Collectors.toList());
    }
}
