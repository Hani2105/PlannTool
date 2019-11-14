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
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author gabor_hanacsek
 */
public class CTB_Ini {

    JTable t;
    JTextField tx;

    enum indit {
        part, scenpath, riportpath, horizontals, workorders, ures, onhands, indentedbom, demand, allocations
    }

    public CTB_Ini() {

    }

    public CTB_Ini(JTable tervtabla) {

        this.t = tervtabla;

    }

    public CTB_Ini(JTextField tx) {

        this.tx = tx;

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
                    "Nincs ini file, most létrehozunkegyet!");
            CTB_Ini i = new CTB_Ini();
            i.inikezel(indit.ures);
            return;

        }
        String line = null;

//feldolgozzuk az ini filet
        String[] pnek = null;
        String[] horizontals = null;
        String[] Workorders = null;
        String[] Onhands = null;
        String[] Indentedbom = null;
        String[] Demand = null;
        String[] Allocation = null;
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

            } else if (line.contains("Horizontals:")) {
                horizontals = line.split(";");
            } else if (line.contains("Workorders:")) {
                Workorders = line.split(";");
            } else if (line.contains("Onhands:")) {
                Onhands = line.split(";");
            } else if (line.contains("Indentedbom:")) {
                Indentedbom = line.split(";");
            } else if (line.contains("Demand:")) {
                Demand = line.split(";");
            } else if (line.contains("Allocations:")) {
                Allocation = line.split(";");
            }

        }
        if (pnek != null) {
            pnek[0] = pnek[0].replace("PNList: ", "");

            for (int i = 0; i < pnek.length; i++) {

                model.addRow(new Object[]{pnek[i]});

            }

        }

        if (horizontals != null) {
            horizontals[0] = horizontals[0].replace("Horizontals: ", "");

            for (int i = 0; i < horizontals.length; i++) {

                CTB.Horizontals.add(horizontals[i]);

            }

        }

        if (Workorders != null) {
            Workorders[0] = Workorders[0].replace("Workorders: ", "");

            for (int i = 0; i < Workorders.length; i++) {

                CTB.Workorders.add(Workorders[i]);

            }

        }

        if (Onhands != null) {
            Onhands[0] = Onhands[0].replace("Onhands: ", "");

            for (int i = 0; i < Onhands.length; i++) {

                CTB.Onhands.add(Onhands[i]);

            }

        }
        if (Indentedbom != null) {
            Indentedbom[0] = Indentedbom[0].replace("Indentedbom: ", "");

            for (int i = 0; i < Indentedbom.length; i++) {

                CTB.Indentedbom.add(Indentedbom[i]);

            }

        }
        if (Demand != null) {
            Demand[0] = Demand[0].replace("Demand: ", "");

            for (int i = 0; i < Demand.length; i++) {

                CTB.Demand.add(Demand[i]);

            }

        }
        if (Allocation != null) {
            Allocation[0] = Allocation[0].replace("Allocations: ", "");

            for (int i = 0; i < Allocation.length; i++) {

                CTB.Allocations.add(Allocation[i]);

            }

        }

    }

    public void inikezel(indit e) throws IOException {
        //a fileok nevei lesznek itt
        String nevek = "";
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
                try {
                    pnlist = pnlist.substring(0, pnlist.length() - 1);
                } catch (Exception ex) {
                }

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

            case horizontals:

//öszeállítjuk egy stringbe a fileneveket
                nevek += tx.getText();

                try {

                    //kicseréljük a horizontalok nevet a programban levore ha vannak beírva
                    for (int i = 0; i < sorok.size(); i++) {

                        if (sorok.get(i).contains("Horizontals:")) {

                            sorok.remove(i);

                        }

                    }
                    sorok.add("Horizontals: " + nevek);
                    break;

                } catch (Exception ex) {
//ha nincs beírva semmi kitöröljük a sort
                    for (int i = 0; i < sorok.size(); i++) {

                        if (sorok.get(i).contains("Horizontals:")) {

                            sorok.remove(i);

                        }

                    }

                    break;

                }
            case workorders:
                //öszeállítjuk egy stringbe a fileneveket
                nevek = "";

                nevek += tx.getText() + ";";

                try {

                    //kicseréljük a horizontalok nevet a programban levore ha vannak beírva
                    for (int i = 0; i < sorok.size(); i++) {

                        if (sorok.get(i).contains("Workorders:")) {

                            sorok.remove(i);

                        }

                    }
                    sorok.add("Workorders: " + nevek);
                    break;

                } catch (Exception ex) {

                }

            case ures:
                break;

            case onhands:
                //öszeállítjuk egy stringbe a fileneveket
                nevek = "";

                nevek += tx.getText() + ";";

                try {

                    //kicseréljük a onhandek nevet a programban levore ha vannak beírva
                    for (int i = 0; i < sorok.size(); i++) {

                        if (sorok.get(i).contains("Onhands:")) {

                            sorok.remove(i);

                        }

                    }
                    sorok.add("Onhands: " + nevek);
                    break;

                } catch (Exception ex) {

                }

            case indentedbom:
                //öszeállítjuk egy stringbe a fileneveket
                nevek = "";

                nevek += tx.getText() + ";";

                try {

                    //kicseréljük a onhandek nevet a programban levore ha vannak beírva
                    for (int i = 0; i < sorok.size(); i++) {

                        if (sorok.get(i).contains("Indentedbom:")) {

                            sorok.remove(i);

                        }

                    }
                    sorok.add("Indentedbom: " + nevek);
                    break;

                } catch (Exception ex) {

                }

            case demand:
                //öszeállítjuk egy stringbe a fileneveket
                nevek = "";

                nevek += tx.getText() + ";";

                try {

                    //kicseréljük a onhandek nevet a programban levore ha vannak beírva
                    for (int i = 0; i < sorok.size(); i++) {

                        if (sorok.get(i).contains("Demand:")) {

                            sorok.remove(i);

                        }

                    }
                    sorok.add("Demand: " + nevek);
                    break;

                } catch (Exception ex) {

                }
            case allocations:
                //öszeállítjuk egy stringbe a fileneveket
                nevek = "";

                nevek += tx.getText() + ";";

                try {

                    //kicseréljük a onhandek nevet a programban levore ha vannak beírva
                    for (int i = 0; i < sorok.size(); i++) {

                        if (sorok.get(i).contains("Allocations:")) {

                            sorok.remove(i);

                        }

                    }
                    sorok.add("Allocations: " + nevek);
                    break;

                } catch (Exception ex) {

                }
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

        bw.close();

    }

}
