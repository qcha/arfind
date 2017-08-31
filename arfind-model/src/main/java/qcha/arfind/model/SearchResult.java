package qcha.arfind.model;

import lombok.Data;

import java.util.List;

@Data
public class SearchResult {
    private final String name;
    private final List<String> result;

    public SearchResult(String name, List<String> result) {
        this.name = name;
        this.result = result;
    }
}
