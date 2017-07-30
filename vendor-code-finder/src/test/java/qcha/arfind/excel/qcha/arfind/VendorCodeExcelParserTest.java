package qcha.arfind.excel.qcha.arfind;

import org.junit.Ignore;
import org.junit.Test;
import qchar.arfind.excel.VendorCodeExcelParser;

//todo
@Ignore
public class VendorCodeExcelParserTest {
    @Test
    public void findFirstMatch() throws Exception {
        String matchString = "Вагонка";
        VendorCodeExcelParser parser = new VendorCodeExcelParser("/Users/aarexer/Coding/arfind/vendor-code-finder/src/test/resources/prays.xls");
        parser.workWithSheet("TDSheet");
        System.out.println(parser.findMatches(matchString));
    }
}
