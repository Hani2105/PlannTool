/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool.CTB_CALC;

import static PlannTool.CTB_CALC.CTB.jTable1;
import PlannTool.ExcelExporter;
import static PlannTool.ablak.jTable1;
import static PlannTool.ablak.jTable2;
import PlannTool.infobox;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import static org.apache.poi.hssf.usermodel.HeaderFooter.file;

/**
 *
 * @author gabor_hanacsek
 */
public class CTB_Scenario {

    CTB c;

    public CTB_Scenario(CTB c) {

        this.c = c;

    }

    public void ScenarioSave() throws IOException {
//gyakorlatilag azt szeretnenk csinalni , hogy vegigmegyünk a tabokon es elmentjuk egy textbe az adatokat.

//ki kell tallozni , hogy hova szeretnenk menteni
        JFileChooser fileChooser = CTB_Filechooser.getFileChooser();
        fileChooser.setAcceptAllFileFilterUsed(true);
        fileChooser.setDialogTitle("Mentés helye!");

        int userSelection = fileChooser.showSaveDialog(c);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            CTB_Filechooser.setLastDir(fileChooser.getSelectedFile());
            File fileToSave = fileChooser.getSelectedFile();
            File file = new File(fileToSave.getAbsolutePath().replace(".scen", "") + ".scen");
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            //beirunk valami faszssagot
            bw.write("Kezdodik a file!");
            bw.newLine();

            JTable[] tablakneve = new JTable[10];
            tablakneve[0] = c.jTable1;
            tablakneve[1] = c.jTable2;
            tablakneve[2] = c.jTable3;
            tablakneve[3] = c.jTable4;
            tablakneve[4] = c.jTable5;
            tablakneve[5] = c.jTable6;
            tablakneve[6] = c.jTable7;
            tablakneve[7] = c.jTable8;
            tablakneve[8] = c.jTable11;
            tablakneve[9] = c.jTable10;

            for (int i = 0; i < tablakneve.length; i++) {

                TableToText(tablakneve[i], file, bw);

            }

            //beirunk valami faszssagot
            bw.write("Vege a Filenak!");
            bw.newLine();

            bw.close();
            fw.close();
            CTB.jLabel6.setText(fileToSave.getName().replace(".scen", ""));
        }

        infobox info = new infobox();
        info.infoBox("Mentés kész!", "Mentés!");

    }

    public static void TableToText(JTable j, File f, BufferedWriter bw) throws IOException {

//beirjuk h kezdodik a columneve oszlop
        bw.write(j.getName() + ";" + "!!COLNAME" + ";");
//beirjuk a column neveket 
        for (int i = 0; i < j.getColumnCount(); i++) {

            bw.write(j.getColumnName(i).toString() + ";");

        }
//uj sorban kezdjuk az adatokat

        bw.newLine();

//az adatok
        for (int r = 0; r < j.getRowCount(); r++) {//rows
//a tábla neve           
            bw.write(j.getName() + ";");

            for (int i = 0; i < j.getColumnCount(); i++) {//columns
                try {
                    bw.write(j.getValueAt(r, i).toString() + ";");
                } catch (Exception e) {
                    bw.write(";");
                }
            }
            bw.newLine();
        }

        bw.write(j.getName() + ";" + "!!VEGE ");
        bw.newLine();

    }

    public void ScenarioLoad() throws FileNotFoundException, IOException {

//be kell tallózni a text filet
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Scenario", "scen");
        JFileChooser chooser = CTB_Filechooser.getFileChooser();
        chooser.setFileFilter(filter);
        int filename = chooser.showOpenDialog(null);

        if (filename == JFileChooser.APPROVE_OPTION) {
            //et lesz a fileunk 
            CTB_Filechooser.setLastDir(chooser.getSelectedFile());
            CTB.f = chooser.getSelectedFile();
//kell egy bufferedreader is

            //kitöröljük az összes tábla adatát
            JTable[] tablakneve = new JTable[10];
            tablakneve[0] = c.jTable1;
            tablakneve[1] = c.jTable2;
            tablakneve[2] = c.jTable3;
            tablakneve[3] = c.jTable4;
            tablakneve[4] = c.jTable5;
            tablakneve[5] = c.jTable6;
            tablakneve[6] = c.jTable7;
            tablakneve[7] = c.jTable8;
            tablakneve[8] = c.jTable11;
            tablakneve[9] = c.jTable10;

            outerloop:
            for (int i = 0; i < tablakneve.length; i++) {
                BufferedReader in;
                in = new BufferedReader(new FileReader(CTB.f));

//kiszedjuk a modellt majd kinullazuk a sorokat es az oszlopokat
                DefaultTableModel model = new DefaultTableModel();
                model = (DefaultTableModel) tablakneve[i].getModel();
                model.setRowCount(0);
                model.setColumnCount(0);
                String line = in.readLine();
                String tablaneve = tablakneve[i].getName();

                while ((line = in.readLine()) != null) {

                    String[] cells = line.split(";");

                    //ha a végéhez érünk , lépünk a következő táblához
                    if (cells[0].contains(tablaneve) && cells[1].contains("!!VEGE")) {
                        in.close();
                        tablakneve[i].setModel(model);
                        c.TablaOszlopSzelesseg(tablakneve[i]);
                        continue outerloop;
                    } // ha a coname részhez érünk beszetteljük columnszámot és beírjuk a nevét is
                    else if (cells[0].contains(tablaneve) && cells[1].contains("!!COLNAME")) {

                        for (int col = 0; col < cells.length - 2; col++) {

                            model.addColumn(cells[col + 2]);

                        }

                    } else if (cells[0].contains(tablaneve)) {

                        String[] csokkentett = Arrays.copyOfRange(cells, 1, cells.length);

                        model.addRow(csokkentett);

                    }

                }

            }
            
            CTB.jLabel6.setText(CTB.f.getName().replace(".scen",""));

        }
        CTB_Wipquery wip = new CTB_Wipquery(CTB.jTable1);
        wip.start();
        infobox info = new infobox();
        info.infoBox("Import kész!", "Betöltés!");

    }

}
