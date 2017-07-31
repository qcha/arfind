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
    public void findAllMatchesOfMatchString() throws Exception {
        matchString = "ВАГОНКА";
        Assert.assertEquals(20, parser.findMatches(matchString).size());

        matchString = "вагонка";
        Assert.assertEquals(20, parser.findMatches(matchString).size());

        matchString = "Вагонка";
        Assert.assertEquals(20, parser.findMatches(matchString).size());
    }

    @Test
    public void findAllMatchesOfSecondString() throws Exception {
        matchString = "RAL3005";
        Assert.assertEquals(22, parser.findMatches(matchString).size());
    }

    @Test
    public void findAllMatchesOfThirdString() throws Exception {
        matchString = "Соединитель желоба";
        Assert.assertEquals(43, parser.findMatches(matchString).size());

        Assert.assertEquals(4, parser.findMatches(matchString).get(0).size());
    }
}
