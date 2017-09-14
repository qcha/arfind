package qcha.arfind.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Basic class for parsing excel files with vendor codes.
 */
public class ExcelTextFinder implements AutoCloseable {
    private Workbook excelReader;

    public ExcelTextFinder(String filename) {
        try {
            switch (excelExtension(filename)) {
                case XLSX:
                    excelReader = new XSSFWorkbook(new FileInputStream(filename));
                    break;
                default:
                    //default - xls
                    excelReader = new HSSFWorkbook(new FileInputStream(filename));
                    break;
            }
        } catch (IOException e) {
            throw new RuntimeException(String.format("Can't work with file: %s, cause: %s", filename, e), e);
        }
    }

    public List<List<String>> findMatches(String matchString) {
        //modify for search
        //trim, replace chars and lower case for compare
        matchString = matchString.trim().toLowerCase().replace('ё', 'е');
        List<List<String>> result = new ArrayList<>();

        for (Sheet sheet : excelReader) {
            for (Row currentRow : sheet) {
                if (isMatched(currentRow, matchString)) {
                    result.add(convertRowDataToStringRepresentation(currentRow));
                }
            }
        }

        return result;
    }

    private boolean isMatched(Row row, String matchString) {
        for (Cell currentCell : row) {
            switch (currentCell.getCellTypeEnum()) {
                case STRING:
                    //plain value
                    String value = currentCell.getStringCellValue();

                    //modify for search
                    //trim, delete redundant spaces, replace chars and lower case for compare
                    value = value.trim()
                            .replaceAll(" +", " ")
                            .toLowerCase()
                            .replace('ё', 'е');

                    if (value.contains(matchString)) {
                        return true;
                    }

                    break;
                default:
                    break;
            }
        }

        return false;
    }

    /**
     * Check extension of excel file.
     *
     * @param name file name.
     * @return Excel extension.
     * @throws UnknownExcelExtensionException if can't understand extension of file.
     */
    private ExcelExtension excelExtension(String name) {
        if (name.endsWith("xls")) {
            return ExcelExtension.XLS;

        } else if (name.endsWith("xlsx")) {
            return ExcelExtension.XLSX;
        }

        throw new UnknownExcelExtensionException(name);
    }

    @Override
    public void close() throws Exception {
        excelReader.close();
    }

    private static List<String> convertRowDataToStringRepresentation(Row row) {
        DataFormatter dataFormatter = new DataFormatter();
        List<String> rowData = new ArrayList<>();
        for (Cell cell : row) {
            rowData.add(dataFormatter.formatCellValue(cell));
        }

        return rowData;
    }

    private enum ExcelExtension {
        XLS,
        XLSX
    }
}
