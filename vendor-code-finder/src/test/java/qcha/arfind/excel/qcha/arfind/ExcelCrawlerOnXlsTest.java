package qcha.arfind.excel.qcha.arfind;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import qcha.arfind.excel.ExcelCrawler;

import java.util.List;

public class ExcelCrawlerOnXlsTest {
    private ExcelCrawler parser;

    @Before
    public void setUp() {
        parser = new ExcelCrawler("src/test/resources/prays.xls");
    }

    @After
    public void tearDown() throws Exception {
        parser.close();
    }

    @Test
    public void caseSensitivityTest() {
        //upper case
        Assert.assertEquals(20, parser.findMatches("ВАГОНКА").size());
        //lower case
        Assert.assertEquals(20, parser.findMatches("вагонка").size());
        //default
        Assert.assertEquals(20, parser.findMatches("Вагонка").size());
    }

    @Test
    public void oneNotFullWordTest() {
        Assert.assertEquals(60, parser.findMatches("Бр").size());
    }

    @Test
    public void anotherTest() {
        //part of phrase
        Assert.assertEquals(7, parser.findMatches("Вагонка сорта А").size());
    }

    @Test
    public void stringWithoutSpacesTest() {
        Assert.assertEquals(22, parser.findMatches("RAL3005").size());
    }

    @Test
    public void firstStringWithOneSpaceTest() {
        Assert.assertEquals(4, parser.findMatches("Соединитель желоба").size());
    }

    @Test
    public void firstStringWithTrimmedLastWord() {
        Assert.assertEquals(5, parser.findMatches("Соединитель жел").size());
    }

    @Test
    public void secondStringWithTrimmedLastWord() {
        Assert.assertEquals(5, parser.findMatches("Соединитель ж").size());
    }

    @Test
    public void secondStringWithOneTest() {
        Assert.assertEquals(14, parser.findMatches("Труба водосточная").size());
    }

    @Test
    public void thirdStringWithTrimmedLastWord() {
        Assert.assertEquals(14, parser.findMatches("Труба водо").size());
    }

    @Test
    public void fourthStringWithTrimmedLastWord() {
        Assert.assertEquals(14, parser.findMatches("Труба в").size());
    }

    @Test
    public void fifthStringWithTrimmedLastWord() {
        Assert.assertEquals(2, parser.findMatches("Миксер для красок").size());
    }

    @Test
    public void sixthStringWithTrimmedLastWord() {
        Assert.assertEquals(2, parser.findMatches("Миксер для кр").size());
    }

    @Test
    public void seventhStringWithTrimmedLastWord() {
        Assert.assertEquals(4, parser.findMatches("Миксер для").size());
    }

    @Test
    public void stringWithSeveralSpacesTest() {
        Assert.assertEquals(1, parser.findMatches("Держатель желоба на стену").size());
    }

    @Test
    public void fullNameTest() {
        Assert.assertEquals(1, parser.findMatches("Прокладка из ДСП 3,5х1,75").size());
    }

    @Test
    public void stringWithDifficultLetters() {
        Assert.assertEquals(45, parser.findMatches("Жёлоб").size());

        Assert.assertEquals(45, parser.findMatches("Желоб").size());

        Assert.assertEquals(39, parser.findMatches("Крепеж").size());

        Assert.assertEquals(39, parser.findMatches("Крепёж").size());

        Assert.assertEquals(14, parser.findMatches("Черная").size());

        Assert.assertEquals(14, parser.findMatches("Чёрная").size());

        Assert.assertEquals(4, parser.findMatches("Темный").size());

        Assert.assertEquals(4, parser.findMatches("Тёмный").size());

        Assert.assertEquals(11, parser.findMatches("Плёнка").size());

        Assert.assertEquals(11, parser.findMatches("Пленка").size());

        Assert.assertEquals(1, parser.findMatches("трёхраспорный").size());

        Assert.assertEquals(1, parser.findMatches("трехраспорный").size());
    }

    @Test
    public void whiteSpacesTest() {
        Assert.assertEquals(1, parser.findMatches("Вагонка сорта В -2.1м").size());
    }

    //todo
    @Test
    public void strangeBehaviourWithCustomCellOne() {
        parser = new ExcelCrawler("src/test/resources/test.xls");
        Assert.assertEquals(2, parser.findMatches("руб").size());

    }

    //todo
    @Test
    public void strangeBehaviourWithCustomCellTwo() {
        parser = new ExcelCrawler("src/test/resources/test.xls");
        Assert.assertEquals(1, parser.findMatches("650").size());
    }

    //todo
    @Test
    public void strangeBehaviourWithCustomCellThree() {
        parser = new ExcelCrawler("src/test/resources/test.xls");
        Assert.assertEquals(1, parser.findMatches("510").size());
    }

    @Test
    public void getPriceOfFirstMatch() {
        parser = new ExcelCrawler("src/test/resources/test.xls");
        List<List<String>> result = parser.findMatches("Киров");
        Assert.assertEquals(2, result.size());
        //get price
        Assert.assertEquals("650,00 руб.", result.get(0).get(2));
    }
}
