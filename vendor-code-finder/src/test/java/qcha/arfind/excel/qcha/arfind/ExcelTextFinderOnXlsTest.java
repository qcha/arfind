package qcha.arfind.excel.qcha.arfind;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import qcha.arfind.excel.ExcelTextFinder;

public class ExcelTextFinderOnXlsTest {

    private ExcelTextFinder parser;

    @Before
    public void setUp() throws Exception {
        parser = new ExcelTextFinder("src/test/resources/prays.xls");
    }

    @After
    public void tearDown() throws Exception {
        parser.close();
    }

    @Test
    public void caseSensitivityTest() throws Exception {
        //upper case
        Assert.assertEquals(20, parser.findMatches("ВАГОНКА").size());
        //lower case
        Assert.assertEquals(20, parser.findMatches("вагонка").size());
        //default
        Assert.assertEquals(20, parser.findMatches("Вагонка").size());

    }

    @Test
    public void oneNotFullWordTest() throws Exception {
        Assert.assertEquals(60, parser.findMatches("Бр").size());
    }

    @Test
    public void anotherTest() {
        //part of phrase
        Assert.assertEquals(7, parser.findMatches("Вагонка сорта А").size());
    }

    @Test
    public void stringWithoutSpacesTest() throws Exception {
        Assert.assertEquals(22, parser.findMatches("RAL3005").size());
    }

    @Test
    public void firstStringWithOneSpaceTest() throws Exception {
        Assert.assertEquals(4, parser.findMatches("Соединитель желоба").size());
    }

    @Test
    public void firstStringWithTrimmedLastWord() throws Exception {
        Assert.assertEquals(5, parser.findMatches("Соединитель жел").size());
    }

    @Test
    public void secondStringWithTrimmedLastWord() throws Exception {
        Assert.assertEquals(5, parser.findMatches("Соединитель ж").size());
    }

    @Test
    public void secondStringWithOneTest() throws Exception {
        Assert.assertEquals(14, parser.findMatches("Труба водосточная").size());
    }

    @Test
    public void thirdStringWithTrimmedLastWord() throws Exception {
        Assert.assertEquals(14, parser.findMatches("Труба водо").size());
    }

    @Test
    public void fourthStringWithTrimmedLastWord() throws Exception {
        Assert.assertEquals(14, parser.findMatches("Труба в").size());
    }

    @Test
    public void fifthStringWithTrimmedLastWord() throws Exception {
        Assert.assertEquals(2, parser.findMatches("Миксер для красок").size());
    }

    @Test
    public void sixthStringWithTrimmedLastWord() throws Exception {
        Assert.assertEquals(2, parser.findMatches("Миксер для кр").size());
    }

    @Test
    public void seventhStringWithTrimmedLastWord() throws Exception {
        Assert.assertEquals(4, parser.findMatches("Миксер для").size());
    }

    @Test
    public void stringWithSeveralSpacesTest() throws Exception {
        Assert.assertEquals(1, parser.findMatches("Держатель желоба на стену").size());
    }

    @Test
    public void fullNameTest() throws Exception {
        Assert.assertEquals(1, parser.findMatches("Прокладка из ДСП 3,5х1,75").size());
    }

    @Test
    public void stringWithDifficultLetters() throws Exception {
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
    public void whiteSpacesTest() throws Exception {
        Assert.assertEquals(1, parser.findMatches("Вагонка сорта В -2.1м").size());
    }

    @Test
    public void strangeBehaviourWithCustomCellOne() throws Exception {
        parser = new ExcelTextFinder("src/test/resources/test.xls");
        Assert.assertEquals(2, parser.findMatches("руб").size());

    }

    @Test
    public void strangeBehaviourWithCustomCellTwo() throws Exception {
        parser = new ExcelTextFinder("src/test/resources/test.xls");
        Assert.assertEquals(1, parser.findMatches("650").size());

    }

    @Test
    public void strangeBehaviourWithCustomCellThree() throws Exception {
        parser = new ExcelTextFinder("src/test/resources/test.xls");
        Assert.assertEquals(1, parser.findMatches("510").size());

    }
}
