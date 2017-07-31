package qcha.arfind.excel.qcha.arfind;

import org.junit.Assert;
import org.junit.Test;
import qchar.arfind.excel.VendorCodeExcelParser;

public class VendorCodeExcelParserOnXlsTest {
    @Test
    public void findAllMathesOfMatchString() throws Exception {
        String matchString = "Вагонка";
        VendorCodeExcelParser parser = new VendorCodeExcelParser("src/test/resources/prays.xls");
        parser.workWithSheet("TDSheet");

        Assert.assertEquals(false, parser.findMatches(matchString).isEmpty());
        Assert.assertEquals(19, parser.findMatches(matchString).size());
    }
}
