package qchar.arfind.excel;

import com.google.common.base.Verify;
import com.google.common.collect.Lists;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * Basic class for parsing excel files with vendor codes.
 */
public class VendorCodeExcelParser implements AutoCloseable {
    private final static String DELIMITERS = "\\s+|,\\s*|\\.\\s*";

    private final String filename;
    private Workbook excelReader;
    private Sheet currentSheet;

    public VendorCodeExcelParser(String filename) throws IOException {
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
        Verify.verify(Objects.nonNull(currentSheet), String.format("Need to set sheet name for %s", filename));

        List<Row> result = Lists.newArrayList();
        String value;

        for (Row currentRow : currentSheet) {
            for (Cell currentCell : currentRow) {
                switch (currentCell.getCellTypeEnum()) {
                    case STRING:
                        value = currentCell.getStringCellValue();
                        if (value.toLowerCase().contains(matchString.toLowerCase())) {
                            result.add(currentRow);
                        } else {
                            //check on containing words in string
                            String[] words = matchString.split(DELIMITERS);
                            for (String word : words) {
                                if (value.toLowerCase().contains(word.toLowerCase())) {
                                    result.add(currentRow);
                                    break;
                                }
                            }
                        }
                    default:
                        break;
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

    /**
     * Set sheet for work
     *
     * @param name sheet name.
     */
    public void workWithSheet(String name) {
        currentSheet = excelReader.getSheet(name);
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
