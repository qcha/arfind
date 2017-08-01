package qchar.arfind.excel;

import com.google.common.base.Verify;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
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

    public Multimap<Integer, Row> findMatches(String matchString) {
        Verify.verify(Objects.nonNull(currentSheet), String.format("Need to set sheet name for %s", filename));

        Multimap<Integer, Row> priorityMap = ArrayListMultimap.create();
        String value;

        for (Row currentRow : currentSheet) {
            for (Cell currentCell : currentRow) {
                switch (currentCell.getCellTypeEnum()) {
                    case NUMERIC:
                        //bad casting but we have int value so it's ok
                        int num = (int) currentCell.getNumericCellValue();
                        if(String.valueOf(num).equals(matchString)) {
                            priorityMap.put(0, currentRow);
                        }
                        break;

                    case STRING:
                        if(matchString.toLowerCase().contains("ё")) {
                            matchString = matchString.replace('ё', 'е');
                        }
                        value = currentCell.getStringCellValue();
                        if (value.toLowerCase().contains(matchString.toLowerCase())) {
                            priorityMap.put(0, currentRow);
                        } else {
                            //check on containing words in string
                            String[] words = matchString.split(DELIMITERS);
                            for (String word : words) {
                                if (value.toLowerCase().contains(word.toLowerCase()) ||
                                                value.toLowerCase().replace('е', 'ё').contains(word.toLowerCase()) ||
                                                value.toLowerCase().replace('ё', 'е').contains(word.toLowerCase())) {
                                    priorityMap.put(1, (currentRow));
                                    break;
                                }
                            }
                        }
                    default:
                        break;
                }
            }
        }
        return priorityMap;
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
