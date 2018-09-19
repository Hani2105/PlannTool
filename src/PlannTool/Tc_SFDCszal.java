/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

/**
 *
 * @author gabor_hanacsek
 */
public class Tc_SFDCszal extends Thread {

    Tc_Besheet b;

    public Tc_SFDCszal(Tc_Besheet b) {

        this.b = b;

    }

    public void run() {

        // sfdc keres
        // az oszlop nevet atalakitjuk stringe , ez lesz a tol datum
        String tol = b.jTable2.getColumnName(b.jTable2.getSelectedColumn()).substring(0, b.jTable2.getColumnName(b.jTable2.getSelectedColumn()).length() - 4);

        // kitalaljuk a meddiget
        org.joda.time.format.DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
        DateTime dt = formatter.parseDateTime(tol);

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

        //feldolgozzuk a row datát
        //elindulunk , bejarjuk a tablat es megkeressuk a megfelelo oszlopot
        for (int i = 4; i < b.jTable2.getColumnCount(); i++) {

            //ha egyezik az oszlop neve a datummal akkor bejarjuk  sorokat
            if (b.jTable2.getColumnName(i).substring(0, b.jTable2.getColumnName(i).length() - 4).equals(tol.replace("%20", " "))) {

                //bejarjuk a sorokat es ha egyezik a ws és a pn és teny a sor akkor irunk bele
                for (int r = 0; r < b.jTable2.getRowCount(); r++) {

                    //ez lesz az adatok osszege
                    long osszeg = 0;
                    //elkezdjuk porgetni a rowdatat (adatokat) is
                    for (int d = 0; d < rowdata.length; d++) {

                        try {

                            if (rowdata[d][1].toString().equals(b.jTable2.getValueAt(r, 0).toString()) && rowdata[d][0].toString().contains(b.jTable2.getValueAt(r, 2).toString()) && b.jTable2.getValueAt(r, 3).equals("Tény")) {

                                //osszeadjuk hatha tobbszor fordul elo a rowdataban
                                osszeg += Long.parseLong(rowdata[d][4].toString());

                            }
                        } catch (Exception e) {
                        }

                    }

                    if (osszeg > 0) {

                        //miutan vegigporgettuk a lekerdezes eredmenyet (rowdata) beirjuk a terve alá
                        for (int sor = 0; sor < b.jTable2.getRowCount(); sor++) {

                            try {
                                if (b.jTable2.getValueAt(sor, 0).toString().equals(b.jTable2.getValueAt(r, 0)) && b.jTable2.getValueAt(sor, 3).toString().equals("Tény") && !b.jTable2.getValueAt(sor - 1, i).toString().equals("")) {

                                    b.jTable2.setValueAt(osszeg, sor, i);

                                }
                            } catch (Exception e) {
                            }

                        }

                    }

                }

            }

        }

        Tc_AnimationSFDC.rajzol = false;
    }

}
