package qcha.arfind.excel;

import com.google.common.collect.Lists;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Basic class for finding matches in excel file.
 */
public final class ExcelCrawler implements AutoCloseable {
    private final static Logger logger = LoggerFactory.getLogger(ExcelCrawler.class);

    private final Workbook excelReader;

    public ExcelCrawler(String filename) {
        try {
            switch (excelExtension(filename)) {
                case XLSX:
                    logger.debug("Working with file: {} as XLSX.", filename);
                    excelReader = new XSSFWorkbook(new FileInputStream(filename));
                    break;
                case XLS:
                    logger.debug("Working with file: {} as XLS.", filename);
                    excelReader = new HSSFWorkbook(new FileInputStream(filename));
                    break;
                default:
                    logger.error("The file {} has unknown extension and can't be processed", filename);
                    throw new UnknownExcelExtensionException("Unsupported type of excel file");
            }
        } catch (IOException e) {
            logger.error("An error occurred while processing file: {}, cause: {}.", filename, e);
            throw new UncheckedIOException(String.format("Can't work with file: %s, cause: %s", filename, e), e);
        }
    }

    public List<List<String>> findMatches(String match) {
        //modify for search
        //trim, replace chars and lower case for compare
        final String prepared = match.trim().toLowerCase().replace('ё', 'е');
        final List<List<String>> result = Lists.newArrayList();

        excelReader.forEach(sheet -> sheet.forEach(row -> {
            logger.debug("Searching in {}", sheet.getSheetName());
            if (isMatched(row, prepared)) {
                logger.debug("Match {} in {}.", sheet.getSheetName());
                result.add(convertRowDataToStringRepresentation(row));
            }
        }));

        logger.info("Search complete, found: {} matches", result.size());
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
                        logger.debug("Match was found: {} in {}", match, value);
                        return true;
                    }

                    break;
                default:
                    logger.warn("Not supported cell type in {}, row: {}.", currentCell.getSheet().getSheetName(), currentCell.getRowIndex());
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
        if (FilenameUtils.isExtension(name, "xls")) {
            logger.debug("File {} was marked as xls", name);
            return ExcelExtension.XLS;

        } else if (FilenameUtils.isExtension(name, "xlsx")) {
            logger.debug("File {} was marked as xlsx.", name);
            return ExcelExtension.XLSX;
        }

        logger.error("Unknown excel extension for file {}.", name);
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
