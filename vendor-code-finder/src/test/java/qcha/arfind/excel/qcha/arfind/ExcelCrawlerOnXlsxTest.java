package qcha.arfind.excel.qcha.arfind;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import qcha.arfind.excel.ExcelCrawler;

public class ExcelCrawlerOnXlsxTest {

    private ExcelCrawler parser;

    @Before
    public void setUp() throws Exception {
        parser = new ExcelCrawler("src/test/resources/Prays-list_santekhnika.xlsx");
    }

    @After
    public void tearDown() throws Exception {
        parser.close();
    }

    @Test
    public void caseSensitivityTest() throws Exception {
        //upper case
        Assert.assertEquals(30, parser.findMatches("КЛАПАН").size());
        //lower case
        Assert.assertEquals(30, parser.findMatches("клапан").size());
        //default
        Assert.assertEquals(30, parser.findMatches("Клапан").size());

    }

    @Test
    public void oneNotFullWordTest() throws Exception {
        Assert.assertEquals(128, parser.findMatches("кл").size());
    }

    @Test
    public void testWithStringInQuotes() {
        Assert.assertEquals(2, parser.findMatches("Клапан нижний \"Ани\"").size());
    }

    @Test
    public void stringWithoutSpacesTest() throws Exception {
        Assert.assertEquals(1, parser.findMatches("НСС-1101").size());
    }

    @Test
    public void firstStringWithOneSpaceTest() throws Exception {
        Assert.assertEquals(1, parser.findMatches("Груша сантехническая").size());
    }

    @Test
    public void firstStringWithTrimmedLastWord() throws Exception {
        Assert.assertEquals(1, parser.findMatches("Груша сантех").size());
    }

    @Test
    public void secondStringWithTrimmedLastWord() throws Exception {
        Assert.assertEquals(1, parser.findMatches("Груша сан").size());
    }

    @Test
    public void secondStringWithOneTest() throws Exception {
        Assert.assertEquals(1, parser.findMatches("Колонка боковая латунь").size());
    }

    @Test
    public void thirdStringWithTrimmedLastWord() throws Exception {
        Assert.assertEquals(1, parser.findMatches("Колонка боковая").size());
    }

    @Test
    public void fourthStringWithTrimmedLastWord() throws Exception {
        Assert.assertEquals(1, parser.findMatches("Колонка бок").size());
    }

    @Test
    public void stringWithDifficultLetters() throws Exception {
        Assert.assertEquals(16, parser.findMatches("Ёрш").size());

        Assert.assertEquals(16, parser.findMatches("Ерш").size());

        Assert.assertEquals(6, parser.findMatches("Тётётёр").size());

        Assert.assertEquals(6, parser.findMatches("Тететер").size());

    }

    @Test
    public void stringWithSeveralSpacesTest() throws Exception {
        Assert.assertEquals(1, parser.findMatches("Ремкомплект ПСМ Имп 1/2 металл КРЕСТ").size());
    }

    @Test
    public void strangeBehaviourWithCustomCellOne() throws Exception {
        parser = new ExcelCrawler("src/test/resources/test.xlsx");
        Assert.assertEquals(2, parser.findMatches("руб").size());

    }

    @Test
    public void strangeBehaviourWithCustomCellTwo() throws Exception {
        parser = new ExcelCrawler("src/test/resources/test.xlsx");
        Assert.assertEquals(1, parser.findMatches("650").size());

    }

    @Test
    public void strangeBehaviourWithCustomCellThree() throws Exception {
        parser = new ExcelCrawler("src/test/resources/test.xlsx");
        Assert.assertEquals(1, parser.findMatches("510").size());

    }

}
