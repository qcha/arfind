package qchar.arfind.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Basic class for parsing excel files with vendor codes.
 */
public class ExcelTextFinder implements AutoCloseable {
    private final String filename;
    private Workbook excelReader;

    public ExcelTextFinder(String filename) throws IOException {
        this.filename = filename;
        switch (excelExtension(filename)) {
            case XLSX:
                excelReader = new XSSFWorkbook(new FileInputStream(filename));
                break;
            default:
                //default - xls
                excelReader = new HSSFWorkbook(new FileInputStream(filename));
                break;
        }
    }

    public List<Row> findMatches(String matchString) {
        matchString = matchString.toLowerCase();
        List<Row> result = new ArrayList<>();
        String value;

        for (Sheet sheet : excelReader) {
            for (Row currentRow : sheet) {
                for (Cell currentCell : currentRow) {
                    switch (currentCell.getCellTypeEnum()) {
                        case STRING:
                            value = currentCell.getStringCellValue().toLowerCase();

                            //for both strings replace difficult letter
                            value = value.replace('ё', 'е');
                            matchString = matchString.replace('ё', 'е');

                            //needed when users add two whitespaces instead of one
                            value = value.trim().replaceAll(" +", " ");
                            if (value.contains(matchString)) {
                                result.add(currentRow);
                            }
                        default:
                            break;
                    }
                }
            }
        }

        return result;
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

    private enum ExcelExtension {
        XLS,
        XLSX
    }

}
