package qcha.arfind;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import org.apache.commons.io.FileUtils;
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

public class Sources {
    private static ObservableMap<String, Source> cache;

    public static synchronized ObservableMap<String, Source> getOrCreate() {
        if (Objects.isNull(cache)) {
            cache = createNewCache();
        }

        return cache;
    }

    public static void saveCacheToFile() {
        try {
            FileUtils.writeLines(
                    new File(CONFIG_FILENAME),
                    DEFAULT_CHARSET,
                    cacheToLines()
            );
        } catch (IOException e) {
            throw new RuntimeException(String.format("Cannot save data to file: %s", CONFIG_FILENAME), e);
        }
    }

    private static ObservableMap<String, Source> createNewCache() {
        return FXCollections.observableMap(getAll());
    }

    private static ObservableMap<String, Source> getAll() {
        ObservableMap<String, Source> cache = FXCollections.observableHashMap();

        if (Files.exists(Paths.get(CONFIG_FILENAME))) {
            try (BufferedReader br = new BufferedReader(new FileReader(CONFIG_FILENAME))) {
                br.lines().forEach(line -> {
                    String[] fields = line.split(DEFAULT_FIELD_DELIMITER);
                    Source source = new Source(fields[0], fields[1]);
                    cache.put(source.getName(), source);
                });

                return cache;
            } catch (IOException e) {
                throw new InitConfigurationException(String.format("Cannot read file - %s", CONFIG_FILENAME), e);
            }
        }

        return cache;
    }

    private static List<String> cacheToLines() {
        return cache.values().stream()
                .map(detail -> String.format("%s%s%s", detail.getName(), DEFAULT_FIELD_DELIMITER, detail.getPath()))
                .collect(Collectors.toList());
    }
}
