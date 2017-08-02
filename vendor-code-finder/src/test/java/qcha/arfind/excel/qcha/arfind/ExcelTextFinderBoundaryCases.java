package qcha.arfind.excel.qcha.arfind;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import qchar.arfind.excel.ExcelTextFinder;

public class ExcelTextFinderBoundaryCases {

    private ExcelTextFinder parser;

    @After
    public void tearDown() throws Exception {
        parser.close();
    }

    @Test
    public void noSuchFileTest() throws Exception {
        //has to throw FileNotFoundException
        parser = new ExcelTextFinder("src/test/resources/pray.xls");
    }

    @Test
    public void firstWrongFileFormat() throws Exception {
        //has to throw UnknownExcelExtensionException
        parser = new ExcelTextFinder("src/test/resources/prays.txt");
    }

    @Test
    public void secondWrongFileFormat() throws Exception {
        //has to throw UnknownExcelExtensionException
        parser = new ExcelTextFinder("src/test/resources/prays.csv");
    }

    @Test
    public void firstOfficeFileFormat() throws Exception {
        //Different files connected with excel(.doc), has to throw UnknownExcelExtensionException
        parser = new ExcelTextFinder("src/test/resources/prays.doc");
    }

    @Test
    public void secondOfficeFileFormat() throws Exception {
        //Different files connected with excel(.ppt), has to throw UnknownExcelExtensionException
        parser = new ExcelTextFinder("src/test/resources/prays.ppt");
    }

    @Test
    public void thirdOfficeFileFormat() throws Exception {
        //Different files connected with excel(.accdb), has to throw UnknownExcelExtensionException
        parser = new ExcelTextFinder("src/test/resources/prays.accdb");
    }

    @Test
    public void emptyXlsFileAndSheetTest() throws Exception {
        //has to throw exception for user to select sheet for this file
        parser = new ExcelTextFinder("src/test/resources/empty.xls");
        Assert.assertEquals(0, parser.findMatches("").size());
    }

    @Test
    public void emptyXlsxFileAndSheetTest() throws Exception {
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
