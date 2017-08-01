package qcha.arfind.excel.qcha.arfind;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import qchar.arfind.excel.VendorCodeExcelParser;

public class VendorCodeExcelParserOnXlsTest {

    private VendorCodeExcelParser parser;
    private String matchString;

    @Before
    public void setUp() throws Exception {
        parser = new VendorCodeExcelParser("src/test/resources/prays.xls");
        parser.workWithSheet("TDSheet");
    }

    @After
    public void tearDown() throws Exception {
        parser.close();
    }

    @Test
    public void caseSensitivityTest() throws Exception {
        String matchString = "ВАГОНКА";
        Assert.assertEquals(20, parser.findMatches(matchString).size());

        matchString = "вагонка";
        Assert.assertEquals(20, parser.findMatches(matchString).size());

        matchString = "Вагонка";
        Assert.assertEquals(20, parser.findMatches(matchString).size());

    }

    @Test
    public void stringWithoutSpacesTest() throws Exception {
        matchString = "RAL3005";
        Assert.assertEquals(22, parser.findMatches(matchString).size());
    }

    @Test
    public void stringWithOneSpaceTest() throws Exception {
        matchString = "Соединитель желоба";
        Assert.assertEquals(43, parser.findMatches(matchString).size());
    }

    @Test
    public void stringWithSeveralSpacesTest() throws Exception {
        matchString = "Держатель желоба на стену";
        Assert.assertEquals(455, parser.findMatches(matchString).size());
    }

    @Test
    public void stringWithDifficultLetters() throws Exception {
        matchString = "Жёлоб";
        Assert.assertEquals(45, parser.findMatches(matchString).size());

        matchString = "Желоб";
        Assert.assertEquals(45, parser.findMatches(matchString).size());

        matchString = "Крепеж";
        Assert.assertEquals(39, parser.findMatches(matchString).size());

        matchString = "Крепёж";
        Assert.assertEquals(39, parser.findMatches(matchString).size());

        matchString = "Черная";
        Assert.assertEquals(14, parser.findMatches(matchString).size());

        matchString = "Чёрная";
        Assert.assertEquals(14, parser.findMatches(matchString).size());

        matchString = "Темный";
        Assert.assertEquals(4, parser.findMatches(matchString).size());

        matchString = "Тёмный";
        Assert.assertEquals(4, parser.findMatches(matchString).size());

        matchString = "Плёнка";
        Assert.assertEquals(11, parser.findMatches(matchString).size());

        matchString = "Пленка";
        Assert.assertEquals(11, parser.findMatches(matchString).size());

        matchString = "трёхраспорный";
        Assert.assertEquals(1, parser.findMatches(matchString).size());

        matchString = "трехраспорный";
        Assert.assertEquals(1, parser.findMatches(matchString).size());

    }

    /**
     * Expected value - 1. Actual value - 1852.
     * If we print our map, it is the only one value where key equals 0 and others are where key equals 1 - .
     **/
    @Test
    public void priorityTesting() throws Exception {
        matchString = "Вагонка сорта А -3.0м (Кострома)";
        Assert.assertEquals(1, parser.findMatches(matchString).size());
    }

    @Test
    public void numericCellTest() throws Exception {
        matchString = "39082";
        Assert.assertEquals(1, parser.findMatches(matchString).size());

        matchString = "34651";
        Assert.assertEquals(1, parser.findMatches(matchString).size());
    }

    /**
     * Some bad stuff with excel file - when I search through excel "650.00 руб." it gives me 3 coincidences
     * Here it also gives me three (if we print our map) full coincidences, but in excel file there are 6.
     **/
    @Test
    public void priceCellTest() throws Exception {
        matchString = "650,00 руб.";
        Assert.assertEquals(3, parser.findMatches(matchString).size());
    }
}
