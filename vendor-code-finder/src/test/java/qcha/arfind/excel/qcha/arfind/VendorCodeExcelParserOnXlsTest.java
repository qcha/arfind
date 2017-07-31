package qcha.arfind.excel.qcha.arfind;

import org.junit.Assert;
import org.junit.Test;
import qchar.arfind.excel.VendorCodeExcelParser;

public class VendorCodeExcelParserOnXlsTest {
    @Test
    public void findAllMatchesOfMatchString() throws Exception {
        VendorCodeExcelParser parser = new VendorCodeExcelParser("vendor-code-finder/src/test/resources/prays.xls");
        parser.workWithSheet("TDSheet");

        String matchString = "ВАГОНКА";
        Assert.assertEquals(20, parser.findMatches(matchString).size());

        matchString = "вагонка";
        Assert.assertEquals(20, parser.findMatches(matchString).size());

        matchString = "Вагонка";
        Assert.assertEquals(20, parser.findMatches(matchString).size());

        matchString = "RAL3005";
        Assert.assertEquals(22, parser.findMatches(matchString).size());

        matchString = "Соединитель желоба";
        Assert.assertEquals(4, parser.findMatches(matchString).size());
    }
}
