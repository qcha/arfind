package qcha.arfind;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import qcha.arfind.model.SearchDetails;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

class SearchModelCache {
    private static ObservableMap<String, SearchDetails> cache;

    static synchronized ObservableMap<String, SearchDetails> getOrCreateCache() {
        if (Objects.isNull(cache)) {
            cache = createNewCache();
        }

        return cache;
    }

    private static ObservableMap<String, SearchDetails> createNewCache() {
        return FXCollections.observableMap(getAll());
    }

    private static ObservableMap<String, SearchDetails> getAll() {
        if (Files.exists(Paths.get("config.csv"))) {
            ObservableMap<String, SearchDetails> cache = FXCollections.observableHashMap();
            try {
                BufferedReader br = new BufferedReader(new FileReader("config.csv"));
                String line;
                while ((Objects.nonNull(line = br.readLine()))) {
                    String[] fields = line.split(";");
                    SearchDetails searchDetails = new SearchDetails(fields[0], fields[1]);
                    cache.put(searchDetails.getName(), searchDetails);
                }
                return cache;
            } catch (IOException exception) {
                throw new RuntimeException(
                        String.format("Cannot read file - %s", "config.csv"),
                        exception
                );
            }
        } else {
            return FXCollections.emptyObservableMap();
        }
    }
}
