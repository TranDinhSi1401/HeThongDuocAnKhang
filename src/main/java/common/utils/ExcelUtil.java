package common.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Utility class for Excel operations (Export/Import).
 */
public class ExcelUtil {

    /**
     * Exports a JTable's data to an Excel file (.xlsx).
     *
     * @param table    The JTable to export.
     * @param title    The title or sheet name.
     * @param filePath The destination file path.
     * @throws IOException If an I/O error occurs.
     */
    public static void exportTableToExcel(JTable table, String title, String filePath) throws IOException {
        TableModel model = table.getModel();
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(title);

            // Create header row
            Row headerRow = sheet.createRow(0);
            for (int col = 0; col < model.getColumnCount(); col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(model.getColumnName(col));
                
                // Style header
                CellStyle headerStyle = workbook.createCellStyle();
                Font font = workbook.createFont();
                font.setBold(true);
                headerStyle.setFont(font);
                headerStyle.setAlignment(HorizontalAlignment.CENTER);
                cell.setCellStyle(headerStyle);
            }

            // Create data rows
            for (int row = 0; row < model.getRowCount(); row++) {
                Row excelRow = sheet.createRow(row + 1);
                for (int col = 0; col < model.getColumnCount(); col++) {
                    Cell cell = excelRow.createCell(col);
                    Object value = model.getValueAt(row, col);
                    if (value != null) {
                        if (value instanceof Number) {
                            cell.setCellValue(((Number) value).doubleValue());
                        } else {
                            cell.setCellValue(value.toString());
                        }
                    }
                }
            }

            // Auto-size columns
            for (int col = 0; col < model.getColumnCount(); col++) {
                sheet.autoSizeColumn(col);
            }

            // Write to file
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }
        }
    }
}
