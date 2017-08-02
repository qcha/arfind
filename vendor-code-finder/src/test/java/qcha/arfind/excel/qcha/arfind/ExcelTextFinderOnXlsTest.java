package qcha.arfind.excel.qcha.arfind;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import qchar.arfind.excel.ExcelTextFinder;

public class ExcelTextFinderOnXlsTest {

    private ExcelTextFinder parser;
    private String matchString;

    @Before
    public void setUp() throws Exception {
        parser = new ExcelTextFinder("src/test/resources/prays.xls");
        parser.workWithSheet("TDSheet");
    }

    @After
    public void tearDown() throws Exception {
        parser.close();
    }

    @Test
    public void caseSensitivityTest() throws Exception {
        matchString = "ВАГОНКА";
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
    public void firstStringWithOneSpaceTest() throws Exception {
        matchString = "Соединитель желоба";
        Assert.assertEquals(4, parser.findMatches(matchString).size());
    }

    @Test
    public void firstStringWithTrimmedLastWord() throws Exception {
        matchString = "Соединитель жел";
        Assert.assertEquals(5, parser.findMatches(matchString).size());
    }

    @Test
    public void secondStringWithTrimmedLastWord() throws Exception {
        matchString = "Соединитель ж";
        Assert.assertEquals(5, parser.findMatches(matchString).size());
    }

    @Test
    public void secondStringWithOneTest() throws Exception {
        matchString = "Труба водосточная";
        Assert.assertEquals(14, parser.findMatches(matchString).size());
    }

    @Test
    public void thirdStringWithTrimmedLastWord() throws Exception {
        matchString = "Труба водо";
        Assert.assertEquals(14, parser.findMatches(matchString).size());
    }

    @Test
    public void fourthStringWithTrimmedLastWord() throws Exception {
        matchString = "Труба в";
        Assert.assertEquals(14, parser.findMatches(matchString).size());
    }

    @Test
    public void fifthStringWithTrimmedLastWord() throws Exception {
        matchString = "Миксер для красок";
        Assert.assertEquals(2, parser.findMatches(matchString).size());
    }

    @Test
    public void sixthStringWithTrimmedLastWord() throws Exception {
        matchString = "Миксер для кр";
        Assert.assertEquals(2, parser.findMatches(matchString).size());
    }

    @Test
    public void seventhStringWithTrimmedLastWord() throws Exception {
        matchString = "Миксер для";
        Assert.assertEquals(4, parser.findMatches(matchString).size());
    }

    @Test
    public void stringWithSeveralSpacesTest() throws Exception {
        matchString = "Держатель желоба на стену";
        Assert.assertEquals(1, parser.findMatches(matchString).size());
    }

    @Test
    public void fullNameTest() throws Exception {
        matchString = "Прокладка из ДСП 3,5х1,75";
        Assert.assertEquals(1, parser.findMatches(matchString).size());
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
}
