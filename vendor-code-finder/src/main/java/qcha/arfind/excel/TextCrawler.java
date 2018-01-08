package qcha.arfind.excel;

import com.google.common.collect.Lists;
import qcha.arfind.model.Source;
import qcha.arfind.model.SearchResult;

import java.util.List;

public final class TextCrawler {
    private TextCrawler() {
    }

    public static List<SearchResult> findAnyMatches(String match, List<Source> sources) {
        List<SearchResult> results = Lists.newArrayList();

        sources.forEach(source -> {
            try(ExcelCrawler finder = new ExcelCrawler(source.getPath())) {
                finder.findMatches(match).forEach(row -> results.add(new SearchResult(source.getName(), row)));
            } catch (Exception e) {
                //todo add logger
                System.err.println("Can't close resource.");
            }

        });

        return results;
    }
}
