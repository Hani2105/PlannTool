/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool;

import static PlannTool.Anyagelados.workbook;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import javafx.stage.FileChooser;
import javax.swing.table.DefaultTableModel;
import jxl.Workbook;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author gabor_hanacsek
 */
public class Anyaglistakibontas {

    public void tablabair() {

        DefaultTableModel model2 = new DefaultTableModel();
        model2 = (DefaultTableModel) Anyagelados.jTable2.getModel();
        model2.setRowCount(0);
        int osszdarab = 0;

//elővesszük a lequeryzett adatból a megfelelő pn-t
//a pn ünk 
        String pn = "";
        try {
            pn = Anyagelados.jTable3.getValueAt(Anyagelados.jTable3.getSelectedRow(),4).toString();
        } catch (Exception e) {
        }
//megkeressuk a listankban
        for (int i = 0; i < Anyagelados.eladaslista.size(); i++) {

//ha egyezik a pn akkor összeadjuk a darabszamot es betesszuk az egesz sort a jtable2 modelljébe
            if (Anyagelados.eladaslista.get(i)[4].toString().contains(pn) && !pn.equals("")) {
                try {
                    osszdarab += Integer.parseInt(Anyagelados.eladaslista.get(i)[2]);
                    model2.addRow(new Object[]{Anyagelados.eladaslista.get(i)[4].toString(), Anyagelados.eladaslista.get(i)[2].toString(), Anyagelados.eladaslista.get(i)[9].toString(), Anyagelados.eladaslista.get(i)[8].toString(), osszdarab});

                } catch (Exception e) {
                }
            }

        }

        Anyagelados.jTable2.setModel(model2);

// itt inditjuk a horizontal adatok kitolteset
        //bejarjuk a horizontal gyujtot
        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) Anyagelados.jTable1.getModel();
        model.setRowCount(0);
        int szamlalo = 0;
        for (int i = 0; i < Anyagelados.horizontal.size(); i++) {

            if (Anyagelados.horizontal.get(i)[3].contains(pn) || Anyagelados.horizontal.get(i)[2].contains("Extra Comment")) {

                model.addRow(new Object[54]);
                for (int n = 1; n < 54; n++) {

                    model.setValueAt(Anyagelados.horizontal.get(i)[n], szamlalo, n);

                    //Anyagelados.jTable1.setValueAt(Anyagelados.horizontal.get(i)[n], 0, n);
                }
                szamlalo++;

            }

        }

        Anyagelados.jTable1.setModel(model);

    }

    public void celleditor() throws IOException {

        if (Anyagelados.jTable3.isEditing()) {

            Anyagelados.jTable3.getCellEditor().stopCellEditing();
        }

        //Read the spreadsheet that needs to be updated
        FileInputStream input_document = new FileInputStream(Anyagelados.fileChooser.getSelectedFile());
        //Access the workbook
        XSSFWorkbook my_xls_workbook = new XSSFWorkbook(input_document);
        //Access the worksheet, so that we can update / modify it.
        XSSFSheet my_worksheet = my_xls_workbook.getSheetAt(0);
        // declare a Cell object
        Cell cell = null;
        // Access the cell first to update the value
        cell = my_worksheet.getRow(Anyagelados.jTable3.getSelectedRow()).getCell(Anyagelados.jTable3.getSelectedColumn()-1);
        // Get current value and then add 5 to it 
        if (cell != null) {
            String eztirjukbele = "";
            try {
                eztirjukbele = Anyagelados.jTable3.getValueAt(Anyagelados.jTable3.getSelectedRow(), Anyagelados.jTable3.getSelectedColumn()).toString();
            } catch (Exception e) {

            }
            cell.setCellValue(eztirjukbele);
        } else {

            Row row = my_worksheet.getRow(Anyagelados.jTable3.getSelectedRow());

            cell = row.createCell(Anyagelados.jTable3.getSelectedColumn()-1);
            String eztirjukbele = "";
            try {
                eztirjukbele = Anyagelados.jTable3.getValueAt(Anyagelados.jTable3.getSelectedRow(), Anyagelados.jTable3.getSelectedColumn()).toString();
            } catch (Exception e) {

            }
            cell.setCellValue(eztirjukbele);

        }

        //Close the InputStream
        input_document.close();
        //Open FileOutputStream to write updates
        FileOutputStream output_file = null;
        try {
            output_file = new FileOutputStream(Anyagelados.fileChooser.getSelectedFile());
        } catch (Exception e) {

            infobox info = new infobox();
            info.infoBox(e.getMessage(), "Hiba!");

        }
        //write changes
        try {
            my_xls_workbook.write(output_file);
        } catch (Exception e) {
        }
        //close the stream
        try {
            output_file.close();
        } catch (Exception e) {
        }

    }

}
