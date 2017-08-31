package qcha.arfind;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import org.apache.commons.io.FileUtils;
import qcha.arfind.model.SearchDetails;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static qcha.arfind.utils.Constants.ConfigFileConstants.*;

public class SearchModelCache {
    private static ObservableMap<String, SearchDetails> cache;

    public static synchronized ObservableMap<String, SearchDetails> getOrCreateCache() {
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
                    convertSearchDetailsToStringRepresentation(cache.values())
            );
        } catch (IOException exception) {
            throw new RuntimeException(
                    String.format("Cannot save data to file: %s", CONFIG_FILENAME),
                    exception
            );
        }
    }

    private static ObservableMap<String, SearchDetails> createNewCache() {
        return FXCollections.observableMap(getAll());
    }

    private static ObservableMap<String, SearchDetails> getAll() {
        if (Files.exists(Paths.get(CONFIG_FILENAME))) {
            ObservableMap<String, SearchDetails> cache = FXCollections.observableHashMap();
            try {
                BufferedReader br = new BufferedReader(new FileReader(CONFIG_FILENAME));
                String line;
                while ((Objects.nonNull(line = br.readLine()))) {
                    String[] fields = line.split(DEFAULT_FIELD_DELIMITER);
                    SearchDetails searchDetails = new SearchDetails(fields[0], fields[1]);
                    cache.put(searchDetails.getName(), searchDetails);
                }
                return cache;
            } catch (IOException exception) {
                throw new RuntimeException(
                        String.format("Cannot read file - %s", CONFIG_FILENAME),
                        exception
                );
            }
        } else {
            return FXCollections.observableHashMap();
        }
    }

    private static List<String> convertSearchDetailsToStringRepresentation(Collection<SearchDetails> details) {
        return details.stream()
                .map(detail -> String.format("%s%s%s", detail.getName(), DEFAULT_FIELD_DELIMITER, detail.getPath()))
                .collect(Collectors.toList());
    }
}
