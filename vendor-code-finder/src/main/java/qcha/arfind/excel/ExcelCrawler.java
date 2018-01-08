package qcha.arfind.excel;

import com.google.common.collect.Lists;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Basic class for finding matches in excel file.
 */
public class ExcelCrawler implements AutoCloseable {
    private final Workbook excelReader;

    public ExcelCrawler(String filename) {
        try {
            switch (excelExtension(filename)) {
                case XLSX:
                    excelReader = new XSSFWorkbook(new FileInputStream(filename));
                    break;
                default:
                    //default - xls
                    excelReader = new HSSFWorkbook(new FileInputStream(filename));
            }
        } catch (IOException e) {
            throw new UncheckedIOException(String.format("Can't work with file: %s, cause: %s", filename, e), e);
        }
    }

    public List<List<String>> findMatches(String match) {
        //modify for search
        //trim, replace chars and lower case for compare
        final String prepared = match.trim().toLowerCase().replace('ё', 'е');
        final List<List<String>> result = Lists.newArrayList();

        excelReader.forEach(sheet -> sheet.forEach(row -> {
            if (isMatched(row, prepared)) {
                result.add(convertRowDataToStringRepresentation(row));
            }
        }));

        return result;
    }

    private boolean isMatched(Row row, String match) {
        for (Cell currentCell : row) {
            switch (currentCell.getCellTypeEnum()) {
                case STRING:
                    //plain value
                    String value = currentCell.getStringCellValue();

                    //modify for search
                    //trim, delete redundant spaces, replace chars and lower case for compare
                    value = value.trim()
                            .trim()
                            .toLowerCase()
                            //only for russian search
                            .replace('ё', 'е');

                    if (value.contains(match)) {
                        return true;
                    }

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

    private List<String> convertRowDataToStringRepresentation(Row row) {
        final DataFormatter dataFormatter = new DataFormatter();
        final List<String> rowData = Lists.newArrayList();

        row.forEach(cell -> rowData.add(dataFormatter.formatCellValue(cell)));

        return rowData;
    }

    private enum ExcelExtension {
        XLS,
        XLSX
    }
}
