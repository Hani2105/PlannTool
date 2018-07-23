/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.*;
import javax.swing.table.TableModel;
import jxl.*;
import jxl.write.*;

public class ExcelExporter {

    void fillData(JTable table, File file) {

        try {

            WritableWorkbook workbook1 = Workbook.createWorkbook(file);
            WritableSheet sheet1 = workbook1.createSheet("First Sheet", 0);

            for (int i = 0; i < table.getColumnCount(); i++) {
                Label column = new Label(i, 0, table.getColumnName(i));
                sheet1.addCell(column);
            }
            int j = 0;
            for (int i = 0; i < table.getRowCount(); i++) {
                for (j = 0; j < table.getColumnCount(); j++) {
                    if (table.getValueAt(i, j) != null) {
                        Label row = new Label(j, i + 1, table.getValueAt(i, j).toString());
                        sheet1.addCell(row);
                    }
                }
            }
            workbook1.write();
            workbook1.close();
            infobox.infoBox("Az exportálás megtörtént!", "Export");
        } catch (Exception ex) {
            ex.printStackTrace();
            infobox.infoBox(ex.getMessage(), "Export");
            System.out.println(ex.getMessage());

        }
    }
}
