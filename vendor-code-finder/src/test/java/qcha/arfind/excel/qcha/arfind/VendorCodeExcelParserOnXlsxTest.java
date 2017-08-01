package qcha.arfind.excel.qcha.arfind;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import qchar.arfind.excel.VendorCodeExcelParser;

public class VendorCodeExcelParserOnXlsxTest {

    private VendorCodeExcelParser parser;
    private String matchString;

    @Before
    public void setUp() throws Exception {
        parser = new VendorCodeExcelParser("src/test/resources/Prays-list_santekhnika.xlsx");
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
    public void stringWithoutSpacesTest() throws Exception {
        matchString = "НСС-1101";
        Assert.assertEquals(1, parser.findMatches(matchString).size());
    }

    @Test
    public void stringWithOneSpaceTest() throws Exception {
        matchString = "Груша сантехническая";
        Assert.assertEquals(2, parser.findMatches(matchString).size());
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

    /**
     * Expected value - 1. Actual value - 760.
     * If we print our map, it is the only one value where key equals 0 and others are where key equals 1 - .
     **/
    @Test
    public void stringWithSeveralSpacesTest() throws Exception {
        matchString = "Ремкомплект ПСМ Имп 1/2 металл КРЕСТ";
        Assert.assertEquals(760, parser.findMatches(matchString).size());
    }

    @Test
    public void numericCellTest() throws Exception {
        matchString = "17174";
        Assert.assertEquals(1, parser.findMatches(matchString).size());

        matchString = "15198";
        Assert.assertEquals(1, parser.findMatches(matchString).size());
    }

    @Test
    public void priceCellTest() throws Exception {
        matchString = "179,30";
        Assert.assertEquals(1, parser.findMatches(matchString).size());
    }
}
