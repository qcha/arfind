package qcha.arfind.excel.qcha.arfind;

import org.junit.Assert;
import org.junit.Test;
import qcha.arfind.excel.ExcelCrawler;
import qcha.arfind.excel.UnknownExcelExtensionException;

import java.io.UncheckedIOException;

public class ExcelCrawlerTest {
    private ExcelCrawler parser;

    @Test(expected = UncheckedIOException.class)
    public void itShouldThrowUncheckedIOExceptionIfFileDoesntExists() {
        parser = new ExcelCrawler("src/test/resources/pray.xls");
    }

    //todo rename tests below
    @Test(expected = UnknownExcelExtensionException.class)
    public void unknownExcelExceptionDueToTxtFileFormat() {
        parser = new ExcelCrawler("src/test/resources/prays.txt");
    }

    @Test(expected = UnknownExcelExtensionException.class)
    public void unknownExcelExceptionDueToCsvFileFormat() {
        parser = new ExcelCrawler("src/test/resources/prays.csv");
    }

    @Test(expected = UnknownExcelExtensionException.class)
    public void unknownExcelExceptionDueToOfficeDocFileFormat() {
        parser = new ExcelCrawler("src/test/resources/prays.doc");
    }

    @Test(expected = UnknownExcelExtensionException.class)
    public void unknownExcelExceptionDueToOfficePptFileFormat() {
        parser = new ExcelCrawler("src/test/resources/prays.ppt");
    }

    @Test(expected = UnknownExcelExtensionException.class)
    public void unknownExcelExceptionDueToOfficeAccdbFileFormat() {
        parser = new ExcelCrawler("src/test/resources/prays.accdb");
    }

    @Test
    public void notSelectedSheetInEmptyXlsFile() {
        parser = new ExcelCrawler("src/test/resources/empty.xls");
        Assert.assertEquals(0, parser.findMatches("").size());
    }

    @Test
    public void notSelectedSheetInEmptyXlsxFile() {
        parser = new ExcelCrawler("src/test/resources/empty.xlsx");
        Assert.assertEquals(0, parser.findMatches("").size());
    }

    @Test
    public void emptyXlsFileAndNotEmptySheetTest() {
        parser = new ExcelCrawler("src/test/resources/empty.xls");
        Assert.assertEquals(0, parser.findMatches("").size());
    }

    @Test
    public void emptyXlsxFileAndNotEmptySheetTest() {
        parser = new ExcelCrawler("src/test/resources/empty.xlsx");
        Assert.assertEquals(0, parser.findMatches("").size());
    }
}
