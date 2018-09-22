/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool;

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

/**
 *
 * @author gabor_hanacsek
 */
public class Tc_SfdcDataszal extends Thread {

    Tc_SfdcData d;

    public Tc_SfdcDataszal(Tc_SfdcData d) {

        this.d = d;

    }

    public void run() {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        tol = df.format(d.jDateChooser1.getDate()) + "%20" + d.jLabel3.getText().substring(0, 5);
        DateTime dtorg = new DateTime(d.jDateChooser1.getDate());

        if ((d.jRadioButton1.isSelected() && (d.jComboBox1.getSelectedIndex() == 1 || d.jComboBox1.getSelectedIndex() == 2)) || (d.jRadioButton2.isSelected() && d.jComboBox1.getSelectedIndex() == 2)) {

            dtorg = dtorg.plusDays(1);

            ig = df.format(dtorg.toDate()) + "%20" + d.jLabel3.getText().substring(6, 11);

        } else {

            ig = df.format(dtorg.toDate()) + "%20" + d.jLabel3.getText().substring(6, 11);

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
        for (int i = 4; i < d.b.jTable2.getColumnCount(); i++) {

            //ha egyezik az oszlop neve a datummal akkor bejarjuk  sorokat
            if (d.b.jTable2.getColumnName(i).substring(0, d.b.jTable2.getColumnName(i).length() - 4).equals(tol.replace("%20", " "))) {

                //bejarjuk a sorokat es ha egyezik a ws és a pn és teny a sor akkor irunk bele
                for (int r = 0; r < d.b.jTable2.getRowCount(); r++) {

                    //ez lesz az adatok osszege
                    long osszeg = 0;
                    //elkezdjuk porgetni a rowdatat (adatokat) is
                    for (int n = 0; n < rowdata.length; n++) {

                        try {

                            if (rowdata[n][1].toString().equals(d.b.jTable2.getValueAt(r, 0).toString()) && rowdata[n][0].toString().contains(d.b.jTable2.getValueAt(r, 2).toString()) && d.b.jTable2.getValueAt(r, 3).equals("Tény")) {

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
                        for (int sor = 0; sor < d.b.jTable2.getRowCount(); sor++) {

                            //felvesszuk az utolso megfelelo sor szamat , hogy kesobb irhassunk bele ha nincs terve az adott napra
                            try {
                                if (d.b.jTable2.getValueAt(sor, 0).toString().equals(d.b.jTable2.getValueAt(r, 0)) && d.b.jTable2.getValueAt(sor, 3).toString().equals("Tény") && d.b.jTable2.getValueAt(sor, 2).toString().equals(d.b.jTable2.getValueAt(r, 2))) {

                                    sorszam = sor;
                                }
                            } catch (Exception e) {
                            }
                            try {
                                if (d.b.jTable2.getValueAt(sor, 0).toString().equals(d.b.jTable2.getValueAt(r, 0)) && d.b.jTable2.getValueAt(sor, 3).toString().equals("Tény") && d.b.jTable2.getValueAt(sor, 2).toString().equals(d.b.jTable2.getValueAt(r, 2)) && !d.b.jTable2.getValueAt(sor - 1, i).toString().equals("")&& d.b.jTable2.getValueAt(sor -1, 3).toString().equals("Terv") ) {

                                    d.b.jTable2.setValueAt(osszeg, sor, i);
                                    irtunke = true;
                                    break;

                                }
                            } catch (Exception e) {
                            }

                        }

                        if (irtunke == false && sorszam > 0) {

                            d.b.jTable2.setValueAt(osszeg, sorszam, i);

                        }

                    }

                }

            }

        }

        Tc_AnimationSFDC.rajzole = false;
    }

}
