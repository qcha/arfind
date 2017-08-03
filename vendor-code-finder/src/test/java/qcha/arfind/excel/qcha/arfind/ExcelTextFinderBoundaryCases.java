package qcha.arfind.excel.qcha.arfind;

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

    @Test
    public void notSelectedSheetInEmptyXlsFile() throws Exception {
        parser = new ExcelTextFinder("src/test/resources/empty.xls");
        Assert.assertEquals(0, parser.findMatches("").size());
    }

    @Test
    public void notSelectedSheetInEmptyXlsxFile() throws Exception {
        parser = new ExcelTextFinder("src/test/resources/empty.xlsx");
        Assert.assertEquals(0, parser.findMatches("").size());
    }

    @Test
    public void emptyXlsFileAndNotEmptySheetTest() throws Exception {
        parser = new ExcelTextFinder("src/test/resources/empty.xls");
        Assert.assertEquals(0, parser.findMatches("").size());
    }

    @Test
    public void emptyXlsxFileAndNotEmptySheetTest() throws Exception {
        parser = new ExcelTextFinder("src/test/resources/empty.xlsx");
        Assert.assertEquals(0, parser.findMatches("").size());
    }
}
