/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool.CTB_CALC;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author gabor_hanacsek
 */
public class CTB_Ini {

    JTable t;

    enum indit {
        part, scenpath, riportpath
    }

    public CTB_Ini(JTable tervtabla) {

        this.t = tervtabla;

    }

    public void Olvas() throws FileNotFoundException, IOException {
        //beolvassuk az ini file-t
        String filepath = "C:\\Users\\" + System.getProperty("user.name") + "\\";
        File file = new File(filepath + "CTB.ini");
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (Exception e) {

            JOptionPane.showMessageDialog(t,
                    "Nincs ini file, a következő kilépéskor mentjük a beállításokat!");
            return;

        }
        String line = null;

//feldolgozzuk az ini filet
        String[] pnek = null;
        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) t.getModel();
        model.setRowCount(0);
        while ((line = br.readLine()) != null) {
            if (line.contains("PNList")) {
                pnek = line.split(";");
            } else if (line.contains("Scenpath")) {

                CTB.scenpath = line.replace("Scenpath: ", "");

            } else if (line.contains("Riportpath")) {

                CTB.riportpath = line.replace("Riportpath: ", "");

            }

        }
        if (pnek != null) {
            pnek[0] = pnek[0].replace("PNList: ", "");

            for (int i = 0; i < pnek.length; i++) {

                model.addRow(new Object[]{pnek[i]});

            }

        }

    }

    public void inikezel(indit e) throws IOException {

        //beolvassuk az ini file-t
        String filepath = "C:\\Users\\" + System.getProperty("user.name") + "\\";
        File file = new File(filepath + "CTB.ini");
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException ex) {

        }
        String line = null;
        ArrayList<String> sorok = new ArrayList<>();
//olvassuk a filet
        try {
            while ((line = br.readLine()) != null) {

                sorok.add(line);

            }
        } catch (Exception ex) {
        }
        try {
            br.close();
        } catch (Exception ex) {
        }

        switch (e) {

            case part:
//beolvassuk a part listat
                //kell a pn lista
                String pnlist = "";
                for (int i = 0; i < t.getRowCount(); i++) {
                    try {
                        if (!t.getValueAt(i, 0).equals("") && t.getValueAt(i, 0) != null) {

                            pnlist += t.getValueAt(i, 0) + ";";

                        }
                    } catch (Exception ex) {
                    }
                }

                pnlist = pnlist.substring(0, pnlist.length() - 1);

//kicseréljük a sorok listben a pn sort az uj ra
                for (int i = 0; i < sorok.size(); i++) {

                    if (sorok.get(i).contains("PNList")) {

                        sorok.remove(i);

                    }

                }
                sorok.add("PNList: " + pnlist);
                break;
            case scenpath:
//kicseréljük a statikus elérési utra fileban lévőt
                for (int i = 0; i < sorok.size(); i++) {

                    if (sorok.get(i).contains("Scenpath:")) {

                        sorok.remove(i);

                    }

                }
                sorok.add("Scenpath: " + CTB.scenpath);
                break;
            case riportpath:
//kicseréljük a statikus elérési utra fileban lévőt
                for (int i = 0; i < sorok.size(); i++) {

                    if (sorok.get(i).contains("Riportpath:")) {

                        sorok.remove(i);

                    }

                }
                sorok.add("Riportpath: " + CTB.riportpath);
                break;

        }

//kiirjuk a filet
        //ki kell írni egy fileba az adatokat
        file = new File(filepath + "CTB.ini");
        file.createNewFile();
        FileWriter writer = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(writer);

        for (int i = 0; i < sorok.size(); i++) {

            bw.write(sorok.get(i));
            bw.newLine();

        }

//        writer.flush();
//        writer.close();
        bw.close();

    }

}
