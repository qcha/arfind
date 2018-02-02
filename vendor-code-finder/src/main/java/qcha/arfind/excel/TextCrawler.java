package qcha.arfind.excel;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import qcha.arfind.model.SearchResult;
import qcha.arfind.model.Source;

import java.util.List;

public final class TextCrawler {
    private static final Logger logger = LoggerFactory.getLogger(TextCrawler.class);

    private TextCrawler() {
    }

    public static List<SearchResult> findAnyMatches(String match, List<Source> sources) {
        List<SearchResult> results = Lists.newArrayList();

        sources.forEach(source -> {
            try (ExcelCrawler finder = new ExcelCrawler(source.getPath())) {
                finder.findMatches(match).forEach(row -> {
                    results.add(new SearchResult(source.getName(), row));
                    logger.debug("Match was found: {}, in row: {}", source.getName(), row);
                });
            } catch (Exception e) {
                logger.error("Can't close resource: {}, cause: {}.", source.getPath(), e);
                //todo re-throw error?
            }

        });

        return results;
    }
}
