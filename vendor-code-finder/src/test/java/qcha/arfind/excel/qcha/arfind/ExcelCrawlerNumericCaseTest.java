package qcha.arfind.excel.qcha.arfind;

import org.junit.Assert;
import org.junit.Test;
import qcha.arfind.excel.ExcelCrawler;

import java.util.List;

public class ExcelCrawlerNumericCaseTest {
    private final ExcelCrawler parser = new ExcelCrawler("src/test/resources/testnumeric.xlsx");

    @Test
    public void findOneFullMatchAndOnePartial() {
        final List<List<String>> matches = parser.findMatches("1");

        Assert.assertEquals(2, matches.size());

        final List<String> firstRow = matches.get(0);

        Assert.assertEquals(2, firstRow.size());
        Assert.assertEquals("Общий", firstRow.get(0));
        Assert.assertEquals("1", firstRow.get(1));

        final List<String> secondRow = matches.get(1);

        Assert.assertEquals(2, secondRow.size());
        Assert.assertEquals("Числовой(Через ,)", secondRow.get(0));
        Assert.assertEquals("3,51", secondRow.get(1));
    }

    @Test
    public void findAndMatchSimpleNumbuer() {
        final List<List<String>> matches = parser.findMatches("2");

        Assert.assertEquals(1, matches.size());

        final List<String> matched = matches.get(0);

        Assert.assertEquals(2, matched.size());
        Assert.assertEquals("Числовой (простой)", matched.get(0));
        Assert.assertEquals("2,00", matched.get(1));
    }

    @Test
    public void replacePointToCommaAndMatchNumber() {
        final List<List<String>> matches = parser.findMatches("3,51");

        Assert.assertEquals(1, matches.size());

        final List<String> matched = matches.get(0);

        Assert.assertEquals(2, matched.size());
        Assert.assertEquals("Числовой(Через ,)", matched.get(0));
        Assert.assertEquals("3,51", matched.get(1));
    }
}
