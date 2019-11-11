/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool.CTB_CALC;

import java.awt.Cursor;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

/**
 *
 * @author gabor_hanacsek
 */
public class CTB_NEW_ExportShortyThread extends Thread {

    CTB c;

    public CTB_NEW_ExportShortyThread(CTB c) {
        this.c = c;
    }

    public void run() {

        try {
            getData();
        } catch (InterruptedException ex) {
            Logger.getLogger(CTB_NEW_ExportShortyThread.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void getData() throws InterruptedException {
        //ebben tároljuk a string tömböket melyek pn enként jönnek létre
        ArrayList<String[][]> adatok = new ArrayList<>();

        //a ctb táblán fogunk végigmenni és kijelölgetjük a sorokat
        for (int i = 0; i < CTB.jTable11.getRowCount(); i++) {
            c.exportshorty.jProgressBar1.setMaximum(c.jTable11.getRowCount() - 1);
            c.exportshorty.jProgressBar1.setValue(i);
            c.exportshorty.jProgressBar1.setString(i + " / " + c.jTable11.getRowCount());

            //lefuttatjuk a shorty osztályt
            CTB_NEW_TopShortThread t = new CTB_NEW_TopShortThread(i);
            t.start();
            t.join();
            //létrehozunk egy String tömböt
            String[][] pnadatok = new String[CTB_NEW_Variables.topshortmodel.getRowCount()][8];
            //bejárjuk a modellt és bepakolunk mindent is a tömbbe
            for (int m = 0; m < CTB_NEW_Variables.topshortmodel.getRowCount(); m++) {
                pnadatok[m][0] = CTB.jTable1.getValueAt(i, 0).toString();
                for (int p = 0; p < 7; p++) {
                    try {
                        pnadatok[m][p + 1] = CTB_NEW_Variables.topshortmodel.getValueAt(m, p).toString();
                    } catch (Exception e) {
                        pnadatok[m][p + 1] = "";
                    }
                }

            }

            adatok.add(pnadatok);

        }

        c.exportshorty.setVisible(false);

        //megvannak az adatok , be kell járni és ki kell szedni ami nem kell , és le kell írni ami kell
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setAcceptAllFileFilterUsed(true);
        fileChooser.setDialogTitle("Mentés helye:");
        int userSelection = fileChooser.showSaveDialog(c);
        if (userSelection == JFileChooser.APPROVE_OPTION) {

            File fileToSave = fileChooser.getSelectedFile();
            try {
                ExportToFile(adatok, new File(fileToSave.getAbsolutePath() + ".xls"));
            } catch (IOException ex) {
                Logger.getLogger(CTB_NEW_ExportShorty.class.getName()).log(Level.SEVERE, null, ex);
            } catch (WriteException ex) {
                Logger.getLogger(CTB_NEW_ExportShorty.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    public void ExportToFile(ArrayList<String[][]> lista, File file) throws IOException, WriteException {
        try {
            WritableWorkbook workbook1 = Workbook.createWorkbook(file);
            WritableSheet sheet1 = workbook1.createSheet("First Sheet", 0);

            Label column = new Label(0, 0, "Parent");
            sheet1.addCell(column);
            column = new Label(1, 0, "Partnumber");
            sheet1.addCell(column);
            column = new Label(2, 0, "Calculated QTY");
            sheet1.addCell(column);
            column = new Label(3, 0, "Description");
            sheet1.addCell(column);
            column = new Label(4, 0, "Raw OH");
            sheet1.addCell(column);
            column = new Label(5, 0, "Master comment");
            sheet1.addCell(column);
            column = new Label(6, 0, "OpSeq");
            sheet1.addCell(column);
            column = new Label(7, 0, "Supply");
            sheet1.addCell(column);

//feltöltjük sorokkal 
//hányadik sorban vagyunk
            int sorszama = 1;
            for (int i = 0; i < lista.size(); i++) {

                for (int s = 0; s < lista.get(i).length; s++) {

                    if (c.exportshorty.jTextField1.getText().equals("")) {

                        c.warning.SetMessage("Nem adtál meg darabszámot!");

                    }
                    if (Integer.parseInt(lista.get(i)[s][2]) <= Integer.parseInt(c.exportshorty.jTextField1.getText())) {

//hozzáadjuk a parentet
                        Label row = new Label(0, sorszama, lista.get(i)[s][0]);
                        sheet1.addCell(row);

                        for (int oszlopok = 0; oszlopok < 7; oszlopok++) {

                            row = new Label(oszlopok + 1, sorszama, lista.get(i)[s][oszlopok + 1]);
                            sheet1.addCell(row);

                        }
//hozzáadunk egyet a sorokhoz mert írtunk
                        sorszama++;

                    }

                }

            }

            workbook1.write();
            workbook1.close();
            //  infobox.infoBox("Az exportálás megtörtént!", "Export");
            c.tick.SetMessage("Az exportálás megtörtént!");

        } catch (Exception e) {
            c.warning.SetMessage("Nem sikerült az exportálás!");
        }
    }

}
