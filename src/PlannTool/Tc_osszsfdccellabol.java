/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool;

import static PlannTool.Tc_Betervezo.Besheets;
import static PlannTool.Tc_Betervezo.jTabbedPane1;
import static PlannTool.Tc_SfdcData.ig;
import static PlannTool.Tc_SfdcData.tol;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

/**
 *
 * @author gabor_hanacsek
 */
public class Tc_osszsfdccellabol extends Thread {

    Tc_Besheet b;

    public Tc_osszsfdccellabol(Tc_Besheet b) {

        this.b = b;

    }

    public void run() {

        // az oszlop nevet atalakitjuk stringe , ez lesz a tol datum
        String tol = "";
        try {
            tol = b.jTable2.getColumnName(b.jTable2.getSelectedColumn()).substring(0, b.jTable2.getColumnName(b.jTable2.getSelectedColumn()).length() - 4);
        } catch (Exception e) {

            infobox info = new infobox();
            info.infoBox("Nem jó oszlopot választottál ki , vagy nem választottál ki oszlopot!", "Hiba!");
        }

        // kitalaljuk a meddiget
        org.joda.time.format.DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");

        DateTime dt = null;
        try {
            dt = formatter.parseDateTime(tol);
        } catch (Exception e) {

            infobox info = new infobox();
            info.infoBox("Nem jó oszlopot választottál ki , vagy nem választottál ki oszlopot!", "Hiba!");

        }

        //ha 12 ora van beallitva 12 -t adunk hozza
        if (Tc_Betervezo.jComboBox1.getSelectedIndex() == 0) {

            dt = dt.plusHours(12);

        }

        // ha 8 van beallitva 8 at adunk hozza
        if (Tc_Betervezo.jComboBox1.getSelectedIndex() == 1) {

            dt = dt.plusHours(12);

        }

        //stringe alakitjuk a dt-t
        String ig = dt.toString(formatter);

        //behelyettesitjuk a %20 at
        try {
            ig = ig.replace(" ", "%20");
            tol = tol.replace(" ", "%20");
        } catch (Exception e) {
        }

        //ciklust indítunk és végrehajtjuk a lekért cellákon a dátum beállítást
        //bepakoljuk a maptree be a sheeteket ujból
        Besheets.clear();
        for (int i = 0; i < jTabbedPane1.getTabCount(); i++) {

            String name = jTabbedPane1.getTitleAt(i);

            Besheets.put(name, (Tc_Besheet) jTabbedPane1.getComponentAt(i));

        }

        //johet az eredeti 
        Object rowdata[][] = null;

        try {
            //System.out.println(tol + "  " + ig);
            xmlfeldolg xxx = new xmlfeldolg();

            URL url = new URL("http://143.116.140.120/rest/request.php?page=planning_realisation&product=&starttime=" + tol + "&endtime=" + ig + "&format=xml");

            ArrayList<String> lista = new ArrayList();

            String nodelist = "planning_realisation";
            lista.add("Workstation");
            lista.add("Part_Number");
            lista.add("Pass");
            lista.add("Shop_Order_Number");
            lista.add("SUMPassQty");
            lista.add("move_qty");
            lista.add("manual_move_qty");

            rowdata = (Object[][]) xxx.xmlfeldolg(url, nodelist, lista);

        } catch (MalformedURLException ex) {
            Logger.getLogger(Tc_SfdcData.class.getName()).log(Level.SEVERE, null, ex);
        }
        //inditjuk a nagy ciklus amiben vegigporgetjuk a sheeteket
        for (int q = 0; q < Tc_Betervezo.jTabbedPane1.getTabCount(); q++) {
            //feldolgozzuk a row datát

            //az aktuális besheet neve

            ;
            //elindulunk , bejarjuk a tablat es megkeressuk a megfelelo oszlopot
            for (int i = 4; i < Besheets.get(Tc_Betervezo.jTabbedPane1.getTitleAt(q)).jTable2.getColumnCount(); i++) {

                //ha egyezik az oszlop neve a datummal akkor bejarjuk  sorokat
                if (Besheets.get(Tc_Betervezo.jTabbedPane1.getTitleAt(q)).jTable2.getColumnName(i).substring(0, Besheets.get(Tc_Betervezo.jTabbedPane1.getTitleAt(q)).jTable2.getColumnName(i).length() - 4).equals(tol.replace("%20", " "))) {

                    //bejarjuk a sorokat es ha egyezik a ws és a pn és teny a sor akkor irunk bele
                    for (int r = 0; r < Besheets.get(Tc_Betervezo.jTabbedPane1.getTitleAt(q)).jTable2.getRowCount(); r++) {

                        //ez lesz az adatok osszege
                        long osszeg = 0;
                        //elkezdjuk porgetni a rowdatat (adatokat) is
                        for (int n = 0; n < rowdata.length; n++) {

                            try {

                                if (rowdata[n][1].toString().equals(Besheets.get(Tc_Betervezo.jTabbedPane1.getTitleAt(q)).jTable2.getValueAt(r, 0).toString()) && rowdata[n][0].toString().contains(Besheets.get(Tc_Betervezo.jTabbedPane1.getTitleAt(q)).jTable2.getValueAt(r, 2).toString()) && Besheets.get(Tc_Betervezo.jTabbedPane1.getTitleAt(q)).jTable2.getValueAt(r, 3).equals("Tény")) {

                                    //osszeadjuk hatha tobbszor fordul elo a rowdataban
                                    osszeg += Long.parseLong(rowdata[n][4].toString());

                                }
                            } catch (Exception e) {
                            }

                        }

                        if (osszeg > 0) {

                            //ezzel ellenorizzuk hogy beirtuk e a szamot
                            boolean irtunke = false;
                            int sorszam = 0;

                            //miutan vegigporgettuk a lekerdezes eredmenyet (rowdata) beirjuk a terve alá
                            for (int sor = 0; sor < Besheets.get(Tc_Betervezo.jTabbedPane1.getTitleAt(q)).jTable2.getRowCount(); sor++) {

                                //felvesszuk az utolso megfelelo sor szamat , hogy kesobb irhassunk bele ha nincs terve az adott napra
                                try {
                                    if (Besheets.get(Tc_Betervezo.jTabbedPane1.getTitleAt(q)).jTable2.getValueAt(sor, 0).toString().equals(Besheets.get(Tc_Betervezo.jTabbedPane1.getTitleAt(q)).jTable2.getValueAt(r, 0)) && Besheets.get(Tc_Betervezo.jTabbedPane1.getTitleAt(q)).jTable2.getValueAt(sor, 3).toString().equals("Tény") && Besheets.get(Tc_Betervezo.jTabbedPane1.getTitleAt(q)).jTable2.getValueAt(sor, 2).toString().equals(Besheets.get(Tc_Betervezo.jTabbedPane1.getTitleAt(q)).jTable2.getValueAt(r, 2))) {

                                        sorszam = sor;
                                    }
                                } catch (Exception e) {
                                }
                                try {
                                    if (Besheets.get(Tc_Betervezo.jTabbedPane1.getTitleAt(q)).jTable2.getValueAt(sor, 0).toString().equals(Besheets.get(Tc_Betervezo.jTabbedPane1.getTitleAt(q)).jTable2.getValueAt(r, 0)) && Besheets.get(Tc_Betervezo.jTabbedPane1.getTitleAt(q)).jTable2.getValueAt(sor, 3).toString().equals("Tény") && Besheets.get(Tc_Betervezo.jTabbedPane1.getTitleAt(q)).jTable2.getValueAt(sor, 2).toString().equals(Besheets.get(Tc_Betervezo.jTabbedPane1.getTitleAt(q)).jTable2.getValueAt(r, 2)) && !Besheets.get(Tc_Betervezo.jTabbedPane1.getTitleAt(q)).jTable2.getValueAt(sor - 1, i).toString().equals("") && Besheets.get(Tc_Betervezo.jTabbedPane1.getTitleAt(q)).jTable2.getValueAt(sor - 1, 3).toString().equals("Terv")) {

                                        Besheets.get(Tc_Betervezo.jTabbedPane1.getTitleAt(q)).jTable2.setValueAt(osszeg, sor, i);
                                        irtunke = true;
                                        break;

                                    }
                                } catch (Exception e) {
                                }

                            }

                            if (irtunke == false && sorszam > 0) {

                                Besheets.get(Tc_Betervezo.jTabbedPane1.getTitleAt(q)).jTable2.setValueAt(osszeg, sorszam, i);

                            }

                        }

                    }

                }

            }

            //ez alá
        }

        Tc_AnimationSFDC.rajzole = false;

    }

}
