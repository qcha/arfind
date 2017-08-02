package qcha.arfind.excel.qcha.arfind;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import qchar.arfind.excel.ExcelTextFinder;

public class ExcelTextFinderOnXlsxTest {

    private ExcelTextFinder parser;
    private String matchString;

    @Before
    public void setUp() throws Exception {
        parser = new ExcelTextFinder("src/test/resources/Prays-list_santekhnika.xlsx");
        parser.workWithSheet("TDSheet");
    }

    @After
    public void tearDown() throws Exception {
        parser.close();
    }

    @Test
    public void caseSensitivityTest() throws Exception {
        String matchString = "КЛАПАН";
        Assert.assertEquals(30, parser.findMatches(matchString).size());

        matchString = "клапан";
        Assert.assertEquals(30, parser.findMatches(matchString).size());

        matchString = "Клапан";
        Assert.assertEquals(30, parser.findMatches(matchString).size());

    }

    @Test
    public void oneNotFullWordTest() throws Exception {
        matchString = "кл";
        Assert.assertEquals(128, parser.findMatches(matchString).size());
    }

    @Test
    public void testWithStringInQuotes() {
        matchString = "Клапан нижний \"Ани\"";
        Assert.assertEquals(2, parser.findMatches(matchString).size());
    }

    @Test
    public void stringWithoutSpacesTest() throws Exception {
        matchString = "НСС-1101";
        Assert.assertEquals(1, parser.findMatches(matchString).size());
    }

    @Test
    public void firstStringWithOneSpaceTest() throws Exception {
        matchString = "Груша сантехническая";
        Assert.assertEquals(1, parser.findMatches(matchString).size());
    }

    @Test
    public void firstStringWithTrimmedLastWord() throws Exception {
        matchString = "Груша сантех";
        Assert.assertEquals(1, parser.findMatches(matchString).size());
    }

    @Test
    public void secondStringWithTrimmedLastWord() throws Exception {
        matchString = "Груша сан";
        Assert.assertEquals(1, parser.findMatches(matchString).size());
    }

    @Test
    public void secondStringWithOneTest() throws Exception {
        matchString = "Колонка боковая латунь";
        Assert.assertEquals(1, parser.findMatches(matchString).size());
    }

    @Test
    public void thirdStringWithTrimmedLastWord() throws Exception {
        matchString = "Колонка боковая";
        Assert.assertEquals(1, parser.findMatches(matchString).size());
    }

    @Test
    public void fourthStringWithTrimmedLastWord() throws Exception {
        matchString = "Колонка бок";
        Assert.assertEquals(1, parser.findMatches(matchString).size());
    }

    @Test
    public void stringWithDifficultLetters() throws Exception {
        matchString = "Ёрш";
        Assert.assertEquals(16, parser.findMatches(matchString).size());

        matchString = "Ерш";
        Assert.assertEquals(16, parser.findMatches(matchString).size());

        matchString = "Тётётёр";
        Assert.assertEquals(6, parser.findMatches(matchString).size());

        matchString = "Тететер";
        Assert.assertEquals(6, parser.findMatches(matchString).size());

    }


    @Test
    public void stringWithSeveralSpacesTest() throws Exception {
        matchString = "Ремкомплект ПСМ Имп 1/2 металл КРЕСТ";
        Assert.assertEquals(1, parser.findMatches(matchString).size());
    }

}
