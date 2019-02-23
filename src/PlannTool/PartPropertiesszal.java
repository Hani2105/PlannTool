/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool;

import static PlannTool.Anyagelados.fileChooser;
import static PlannTool.Anyagelados.workbook;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author gabor_hanacsek
 */
public class PartPropertiesszal extends Thread {

    ablak a;

    public PartPropertiesszal(ablak a) {

        this.a = a;
    }

    public void run() {

        JFileChooser fc = new JFileChooser();
        Workbook wb = null;
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        planconnect pc = new planconnect();

//kiuritjuk a tablat
//        String truncate = "truncate table part_properties";
//        pc.feltolt(truncate, false);

//bekérjük a part properties filet
        fc.setCurrentDirectory(new File("S:\\SiteData\\BUD1\\EMS\\planning\\Reports\\APS Weekly REPORTS\\"));
        int result = fc.showOpenDialog(a);
//ha megvan a kiválasztás
        if (result == JFileChooser.APPROVE_OPTION) {
//inditjuk a progress bart
            a.jProgressBar1.setIndeterminate(true);
            try {

                //proba
                //wb = new XSSFWorkbook(fc.getSelectedFile().toString());
                
                File excelFile = new File(fc.getSelectedFile().toString());
                FileInputStream fis = new FileInputStream(excelFile);
                wb = new XSSFWorkbook(fis);

            }catch (EncryptedDocumentException ex) {
                Logger.getLogger(Anyagelados.class.getName()).log(Level.SEVERE, null, ex);
                infobox info = new infobox();
                info.infoBox(ex.getMessage(), "Hiba!");
            } catch (FileNotFoundException ex) {
                Logger.getLogger(PartPropertiesszal.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(PartPropertiesszal.class.getName()).log(Level.SEVERE, null, ex);
            }
            org.apache.poi.ss.usermodel.Sheet sheet = wb.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.rowIterator();

            StringBuffer adatok = new StringBuffer();
            String adatokstring = "";
            int sorokszama = 0;
            String query = "insert ignore part_properties values";

            while (rowIterator.hasNext()) {

                Row row = rowIterator.next();
                adatok.append("(");

                for (int i = 0; i < 90; i++) {

                    Cell cell = row.getCell(i);

                    String value = "";

                    if (cell != null) {

                        switch (cell.getCellType()) {
                            case STRING:
                                value = cell.getStringCellValue();
                                break;
                            case NUMERIC:

                                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                                    value = cell.getDateCellValue().toString();
                                } else {
                                    value = String.valueOf(cell.getNumericCellValue());
                                }
                                break;

                            case FORMULA:

                                switch (cell.getCachedFormulaResultType()) {

                                    case STRING:
                                        value = cell.getRichStringCellValue().toString();
                                        break;
                                    case NUMERIC:
                                        value = String.valueOf(cell.getNumericCellValue());
                                        break;

                                }

                        }

                    }

                    if (value.contains("'")) {

                        value = value.replace("'", "");
                    }

                    value = value.replace("[^a-zA-Z0-9]", "");

                    if (value.length() > 500) {

                        value = value.substring(0, 470);
                        value = "Nem fért ki a teljes adat!" + value;

                    }

                    adatok.append("'");
                    adatok.append(value);
                    adatok.append("',");

                }

                adatok.append("'");
                adatok.append(date);
                adatok.append("'");
                adatok.append("),");
                sorokszama++;
//fel kell darabolni soronkent a propertiest (10000)

                if (sorokszama % 5000 == 0) {

                    adatokstring = adatok.toString();
                    adatokstring = adatokstring.substring(0, adatokstring.length() - 1);
                    //query = query + adatokstring;

                    pc.feltolt1(query + adatokstring, false);
                    adatok.delete(0, adatok.length());
                    adatokstring = "";

                }

            }

//mindenkepp lefuttatjuk a feltoltest a maradek adattal
            adatokstring = adatok.toString();
            adatokstring = adatokstring.substring(0, adatokstring.length() - 1);
            pc.feltolt1(query + adatokstring, true);

            try {
                wb.close();
            } catch (IOException ex) {
                Logger.getLogger(Anyagelados.class.getName()).log(Level.SEVERE, null, ex);
            }
            

//            org.apache.poi.ss.usermodel.Sheet sheet = wb.getSheetAt(0);
//            Iterator<Row> rowIterator = sheet.rowIterator();
//
//            StringBuffer adatok = new StringBuffer();
//            String adatokstring = "";
//            int sorokszama = 0;
//            String query = "insert ignore part_properties values";

            while (rowIterator.hasNext()) {

                Row row = rowIterator.next();
                adatok.append("(");

                for (int i = 0; i < 90; i++) {

                    Cell cell = row.getCell(i);

                    String value = "";

                    if (cell != null) {

                        switch (cell.getCellType()) {
                            case STRING:
                                value = cell.getStringCellValue();
                                break;
                            case NUMERIC:

                                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                                    value = cell.getDateCellValue().toString();
                                } else {
                                    value = String.valueOf(cell.getNumericCellValue());
                                }
                                break;

                            case FORMULA:

                                switch (cell.getCachedFormulaResultType()) {

                                    case STRING:
                                        value = cell.getRichStringCellValue().toString();
                                        break;
                                    case NUMERIC:
                                        value = String.valueOf(cell.getNumericCellValue());
                                        break;

                                }

                        }

                    }

                    if (value.contains("'")) {

                        value = value.replace("'", "");
                    }

                    value = value.replace("[^a-zA-Z0-9]", "");

                    if (value.length() > 500) {

                        value = value.substring(0, 470);
                        value = "Nem fért ki a teljes adat!" + value;

                    }

                    adatok.append("'");
                    adatok.append(value);
                    adatok.append("',");

                }

                adatok.append("'");
                adatok.append(date);
                adatok.append("'");
                adatok.append("),");
                sorokszama++;
//fel kell darabolni soronkent a propertiest (10000)

                if (sorokszama % 5000 == 0) {

                    adatokstring = adatok.toString();
                    adatokstring = adatokstring.substring(0, adatokstring.length() - 1);
                    //query = query + adatokstring;

                    pc.feltolt1(query + adatokstring, false);
                    adatok.delete(0, adatok.length());
                    adatokstring = "";

                }

            }

//mindenkepp lefuttatjuk a feltoltest a maradek adattal
            adatokstring = adatok.toString();
            adatokstring = adatokstring.substring(0, adatokstring.length() - 1);
            pc.feltolt1(query + adatokstring, true);

            try {
                wb.close();
            } catch (IOException ex) {
                Logger.getLogger(Anyagelados.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        a.jProgressBar1.setIndeterminate(false);

    }

}
