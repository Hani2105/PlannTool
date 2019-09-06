/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool.KAPACITAS;

import static PlannTool.BACKEND.Tc_Betervezo.c;
import static PlannTool.CTB_CALC.CTB.TablaOszlopSzelesseg;
import static PlannTool.CTB_CALC.CTB.jTable7;
import PlannTool.CTB_CALC.CTB_Filechooser;
import java.awt.Cursor;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javafx.stage.FileChooser;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.Cursor;
import java.awt.Component;
import java.awt.*;

/**
 *
 * @author gabor_hanacsek
 */
public class Kapacitas_Methods {

    Kapacitas k;

    public Kapacitas_Methods(Kapacitas k) {

        this.k = k;
    }

    public void PlannedordersToTable() throws FileNotFoundException, IOException {

        JFileChooser chooser = new JFileChooser();
//csinálunk egy szűrőt ami csak a tab fileokat mutatja
        FileFilter filter = new FileNameExtensionFilter("TAB File", "tab");
        chooser.setFileFilter(filter);
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        int returnVal = chooser.showOpenDialog(k);
        if (returnVal == JFileChooser.APPROVE_OPTION) {

            DefaultTableModel model = new DefaultTableModel();
            model = (DefaultTableModel) k.jTable1.getModel();
            model.setRowCount(0);
            File file = chooser.getSelectedFile();
//kell a tábla model

            BufferedReader in;

            in = new BufferedReader(new FileReader(file));
            String line = in.readLine();
            while ((line = in.readLine()) != null) {
                if (!line.contains("Start Dates") && !line.contains("Customer")) {
                    String[] cells = line.split("\\t");
                    model.addRow(cells);
                }

            }

            k.jTable1.setModel(model);

        }

        TablaOszlopSzelesseg(k.jTable1);
    }

    public void PlannedordersToRawdata() throws ParseException {

//kiszedjük a planned order modellt
        DefaultTableModel plannedmodel = new DefaultTableModel();
        plannedmodel = (DefaultTableModel) k.jTable1.getModel();
        DefaultTableModel rawmodel = new DefaultTableModel();
        rawmodel = (DefaultTableModel) k.jTable2.getModel();
        rawmodel.setRowCount(0);

//be kell írni a pn-eket és a start dátumokat és összeadni a darabokat
//elindulunk bejárni a plannmodelt
        String pn = "";
        String datum = "";
        String customer = "";
        int db = 0;
        outerloop:
        for (int p = 0; p < plannedmodel.getRowCount(); p++) {
//felvesszük a pn-t és a dátumot
            pn = plannedmodel.getValueAt(p, 1).toString();
            customer = plannedmodel.getValueAt(p, 0).toString();
            datum = plannedmodel.getValueAt(p, 7).toString();
            db = Integer.parseInt(plannedmodel.getValueAt(p, 13).toString().replace(",", ""));
            //boolean irjunke = false;
//ha nulladik nekifutás , mindenképpen hozzáadjuk a sort
            if (p == 0) {

                String input = datum;
                String format = "dd-MMM-yy";

                SimpleDateFormat df = new SimpleDateFormat(format);
                Date date = df.parse(input);

                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                //az évet és a hetet összefűzzük
                String week = datum.substring(datum.length() - 2, datum.length()) + String.format("%02d", cal.get(Calendar.WEEK_OF_YEAR));
                rawmodel.addRow(new Object[]{pn, datum, week, db,customer});

            } else {

//ha nem az első sorban vagyunk akkor bejarjuk a raw modelt is
                for (int r = 0; r < rawmodel.getRowCount(); r++) {
//megnezzuk hogy van e egyezes (pn és a dátum egyezik) , ha igen osszeadjuk a darabokat

                    if (pn.equals(rawmodel.getValueAt(r, 0).toString()) && datum.equals(rawmodel.getValueAt(r, 1).toString())) {
//beállítjuk a darabszamot
                        int osszdarab = Integer.parseInt(rawmodel.getValueAt(r, 3).toString()) + db;
                        rawmodel.setValueAt(osszdarab, r, 3);
//tovabb is lephetunk
                        continue outerloop;

                    }

                }
//ha idáig eljutunk az azt jelenti , hogy kell egy sort mozzáadni a modellhez

                String input = datum;
                String format = "dd-MMM-yy";

                SimpleDateFormat df = new SimpleDateFormat(format);
                Date date = df.parse(input);

                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                //az évet és a hetet összefűzzük
                String week = datum.substring(datum.length() - 2, datum.length()) + String.format("%02d", cal.get(Calendar.WEEK_OF_YEAR));
                rawmodel.addRow(new Object[]{pn, datum, week, db,customer});

            }

        }

        k.jTable2.setModel(rawmodel);

    }

    public void ToStations() {

//végigmegyünk az állomások tabokon és kinullázzuk a modelleket a biztonság kedvéért
        for (int i = 0; i < k.jTabbedPane1.getTabCount() - 1; i++) {
//ha nem a két alapadat tabon vagyunk
            if (!k.jTabbedPane1.getTitleAt(i).equals("Planned ordrers All") && !k.jTabbedPane1.getTitleAt(i).equals("Raw Datas")) {

                DefaultTableModel model = new DefaultTableModel();
                model = (DefaultTableModel) k.allomasok.get(k.jTabbedPane1.getTitleAt(i)).jTable1.getModel();
                model.setRowCount(0);
                k.allomasok.get(k.jTabbedPane1.getTitleAt(i)).jTable1.setModel(model);
            }

        }

        //felvesszük a rawdata modellt
        DefaultTableModel rawdatamodel = new DefaultTableModel();
        rawdatamodel = (DefaultTableModel) k.jTable2.getModel();

        //elkezdjük bejárni
        outerloop:
        for (int r = 0; r < rawdatamodel.getRowCount(); r++) {
//felvesszük az állomás nevet
            String allomas = "";
//felvesszük a darab / órát
            double szorzo = 0;
            try {
                szorzo = Double.parseDouble(rawdatamodel.getValueAt(r, 6).toString());
            } catch (Exception e) {
            }
            try {
                allomas = rawdatamodel.getValueAt(r, 5).toString();
            } catch (Exception e) {
            }
//vegigjarjuk a tabokat es megnezzuk h van e ilyen nevű tab
            for (int t = 0; t < k.jTabbedPane1.getTabCount(); t++) {

                if (k.jTabbedPane1.getTitleAt(t).equals(allomas)) {
//ha talalunk egyezoseget , tehat van ilyen nevű sheet 
//akkor kikaphatjuk a threemapbol es megszerezzuk a tablajat
                    DefaultTableModel model = new DefaultTableModel();
                    model = (DefaultTableModel) k.allomasok.get(allomas).jTable1.getModel();
//felvesszük a pn-t a rawdatából
                    String pn = rawdatamodel.getValueAt(r, 0).toString();
                    String customer = rawdatamodel.getValueAt(r, 4).toString();
//megnezzuk , hogy van e ilyen a modellben
                    for (int m = 0; m < model.getRowCount(); m++) {
                        if (pn.equals(model.getValueAt(m, 0))) {
//ha van akkor be kell jarni es meg kell nezni h van e ilyen het
//tehat felvesszuk a hetet
                            String het = rawdatamodel.getValueAt(r, 2).toString();
//bejarjuk a modell oszlopait kutatva a het utan
                            for (int o = 0; o < model.getColumnCount(); o++) {
//ha egyezik a hét az oszlop nevével
                                if (het.equals(model.getColumnName(o))) {
//hozzaadjuk a darabot az ott szereplo darabszamhoz 

                                    //int elozodarab = Integer.parseInt(model.getValueAt(m, o).toString());
                                    double darab = Double.parseDouble(rawdatamodel.getValueAt(r, 3).toString());
                                    try {
                                        model.setValueAt(darab / szorzo, m, o);
                                    } catch (Exception e) {
                                        model.setValueAt("Hiányzó DB/Óra!", m, o);
                                    }
                                    continue outerloop;
                                }

                            }
//ha ide eljutunk akkor nincs ilyen hét , hozzáadunk egy oszlopot a modellhez

                            model.addColumn(het);
//be kell irni ide a darabot is
                            double darab = Double.parseDouble(rawdatamodel.getValueAt(r, 3).toString());
                            try {
                                model.setValueAt(darab / szorzo, m, model.getColumnCount() - 1);
                            } catch (Exception e) {
                                model.setValueAt("Hiányzó DB/Óra!", m, model.getColumnCount() - 1);

                            }
//be is allitjuk
                            k.allomasok.get(allomas).jTable1.setModel(model);
                            continue outerloop;
                        }
                    }

                    //ha eljutunk eddig az azt jelenti , hogy nincs , kell hozzáadni egy pn-t és egy customert
                    model.addRow(new Object[]{pn,customer});
//fel kell venni a hetet és be kell járni a modellt , hogy va e ilyen hét
                    String het = rawdatamodel.getValueAt(r, 2).toString();
                    for (int o = 0; o < model.getColumnCount(); o++) {
//ha egyezik a hét az oszlop nevével
                        if (het.equals(model.getColumnName(o))) {
//hozzaadjuk a darabot az ott szereplo darabszamhoz                             
                            double darab = Double.parseDouble(rawdatamodel.getValueAt(r, 3).toString());
                            try {
                                model.setValueAt(darab / szorzo, model.getRowCount() - 1, o);
                            } catch (Exception e) {

                                model.setValueAt("Hiányzó DB/Óra!", model.getRowCount() - 1, o);

                            }

                            continue outerloop;
                        }

                    }
//ha eljutunk ide , akkor kell egy hetet is hozzáadni a modellhez
                    model.addColumn(het);
//be kell allitani a darabot
                    double darab = Double.parseDouble(rawdatamodel.getValueAt(r, 3).toString());
                    try {
                        model.setValueAt(darab / szorzo, model.getRowCount() - 1, model.getColumnCount() - 1);
                    } catch (Exception e) {
                        model.setValueAt("Hiányzó DB/Óra!", model.getRowCount() - 1, model.getColumnCount() - 1);

                    }

                    k.allomasok.get(allomas).jTable1.setModel(model);

                    continue outerloop;
                }

            }

        }

    }

}
