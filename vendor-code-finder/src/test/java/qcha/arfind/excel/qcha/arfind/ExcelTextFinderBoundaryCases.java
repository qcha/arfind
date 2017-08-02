package qcha.arfind.excel.qcha.arfind;

import com.google.common.base.VerifyException;
import org.junit.Assert;
import org.junit.Test;
import qchar.arfind.excel.ExcelTextFinder;
import qchar.arfind.excel.UnknownExcelExtensionException;

import java.io.FileNotFoundException;

public class ExcelTextFinderBoundaryCases {

    private ExcelTextFinder parser;

    @Test(expected = FileNotFoundException.class)
    public void noSuchFileTest() throws Exception {
        parser = new ExcelTextFinder("src/test/resources/pray.xls");
    }

    @Test(expected = UnknownExcelExtensionException.class)
    public void unknownExcelExceptionDueToTxtFileFormat()  throws Exception {
        parser = new ExcelTextFinder("src/test/resources/prays.txt");
    }

    @Test(expected = UnknownExcelExtensionException.class)
    public void unknownExcelExceptionDueToCsvFileFormat() throws Exception {
        parser = new ExcelTextFinder("src/test/resources/prays.csv");
    }

    @Test(expected = UnknownExcelExtensionException.class)
    public void unknownExcelExceptionDueToOfficeDocFileFormat() throws Exception {
        parser = new ExcelTextFinder("src/test/resources/prays.doc");
    }

    @Test(expected = UnknownExcelExtensionException.class)
    public void unknownExcelExceptionDueToOfficePptFileFormat() throws Exception {
        parser = new ExcelTextFinder("src/test/resources/prays.ppt");
    }

    @Test(expected = UnknownExcelExtensionException.class)
    public void unknownExcelExceptionDueToOfficeAccdbFileFormat() throws Exception {
        parser = new ExcelTextFinder("src/test/resources/prays.accdb");
    }

    @Test(expected = VerifyException.class)
    public void verifyExceptionDueToNotSelectedSheetInEmptyXlsFile() throws Exception {
        //has to throw exception for user to select sheet for this file
        parser = new ExcelTextFinder("src/test/resources/empty.xls");
        Assert.assertEquals(0, parser.findMatches("").size());
    }

    @Test(expected = VerifyException.class)
    public void verifyExceptionDueToNotSelectedSheetInEmptyXlsxFile() throws Exception {
        //has to throw exception for user to select sheet for this file
        parser = new ExcelTextFinder("src/test/resources/empty.xlsx");
        Assert.assertEquals(0, parser.findMatches("").size());
    }

    @Test
    public void emptyXlsFileAndNotEmptySheetTest() throws Exception {
        parser = new ExcelTextFinder("src/test/resources/empty.xls");
        parser.workWithSheet("TDSheet");
        Assert.assertEquals(0, parser.findMatches("").size());
    }

    @Test
    public void emptyXlsxFileAndNotEmptySheetTest() throws Exception {
        parser = new ExcelTextFinder("src/test/resources/empty.xlsx");
        parser.workWithSheet("TDSheet");
        Assert.assertEquals(0, parser.findMatches("").size());
    }
}
