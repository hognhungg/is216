package com.thuexe.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.JTable;

import java.io.File;
import java.io.FileOutputStream;

public class ExcelExporter {

    public void exportTable(
            JTable table,
            File file
    ) throws Exception {

        // Tự thêm .xlsx nếu thiếu
        if (!file.getName().endsWith(".xlsx")) {

            file = new File(
                    file.getAbsolutePath() + ".xlsx"
            );
        }

        Workbook workbook =
                new XSSFWorkbook();

        Sheet sheet =
                workbook.createSheet("Bao Cao");

        // HEADER
        Row headerRow =
                sheet.createRow(0);

        for (int i = 0;
             i < table.getColumnCount();
             i++) {

            Cell cell =
                    headerRow.createCell(i);

            cell.setCellValue(
                    table.getColumnName(i)
            );
        }

        // DATA
        for (int i = 0;
             i < table.getRowCount();
             i++) {

            Row row =
                    sheet.createRow(i + 1);

            for (int j = 0;
                 j < table.getColumnCount();
                 j++) {

                Cell cell =
                        row.createCell(j);

                Object value =
                        table.getValueAt(i, j);

                if (value != null) {

                    cell.setCellValue(
                            value.toString()
                    );
                }
            }
        }

        // Auto size
        for (int i = 0;
             i < table.getColumnCount();
             i++) {

            sheet.autoSizeColumn(i);
        }

        // Save file
        try (
                FileOutputStream out =
                        new FileOutputStream(file)
        ) {

            workbook.write(out);
        }

        workbook.close();
    }
}
