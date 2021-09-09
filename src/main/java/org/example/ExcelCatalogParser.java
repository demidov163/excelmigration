package org.example;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class ExcelCatalogParser {
    public Map<String, CatalogRow> parseCatalog(String fileName) {
        Map<String, CatalogRow> resultList = new HashMap<>();
        try (InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(fileName)) {
            Workbook workbook = new HSSFWorkbook(resourceAsStream);
            Sheet sheet = workbook.getSheetAt(0);
            final Iterator<Row> rowIterator = sheet.rowIterator();
            rowIterator.next();
            while (rowIterator.hasNext()) {
                final Row current = rowIterator.next();
                final CatalogRow catalogRow =
                        new CatalogRow(getValue(current.getCell(0)), getValue(current.getCell(5)));
                resultList.put(catalogRow.oldId(), catalogRow);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        return resultList;
    }

    private String getValue(Cell cell) {
        return switch (cell.getCellTypeEnum()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> StringUtils.leftPad(String.valueOf(Double.valueOf(cell.getNumericCellValue())
                    .intValue()), 11, '0');
            case _NONE, BLANK -> "";
            default -> throw new IllegalStateException("Unexpected value: " + cell.getCellTypeEnum());
        };
    }
}
