/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool.BACKEND;

import PlannTool.infobox;
import PlannTool.xmlfeldolg;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

/**
 *
 * @author gabor_hanacsek
 */
public class Tc_SFDCszal1 extends Thread {

    Tc_Besheet b;

    public Tc_SFDCszal1(Tc_Besheet b) {

        this.b = b;

    }

    public void run() {

        // sfdc keres
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
        if (Tc_Betervezo.comboertek == 0) {

            dt = dt.plusMinutes(719);

        }

        // ha 8 van beallitva 8 at adunk hozza
        if (Tc_Betervezo.comboertek == 1) {

            dt = dt.plusMinutes(479);

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

        }

//ebben fogjuk tarolni a mar beirt pn-t és ws-t
        List<String> pnwsjob = new ArrayList<>();
//ez lesz a kapcsoloja

//elindulunk , bejarjuk a tablat es megkeressuk a megfelelo oszlopot
        for (int i = 4; i < b.jTable2.getColumnCount(); i++) {

//ha egyezik az oszlop neve a datummal 
            if (b.jTable2.getColumnName(i).substring(0, b.jTable2.getColumnName(i).length() - 4).equals(tol.replace("%20", " "))) {

//bejarjuk a sorokat
                outerloop:
                for (int r = 0; r < b.jTable2.getRowCount(); r++) {

//csak akkor csinálunk bármit is ha tény sorban vagyunk
                    if (b.jTable2.getValueAt(r, 3).toString().equals("Tény")) {

//felvesszuk az elso pn-t és ws-t
//try az info sorok miatt
                        String pn = "";
                        String ws = "";
                        String job = "";
                        long osszeg = 0;

                        try {
                            pn = b.jTable2.getValueAt(r, 0).toString();
                            ws = b.jTable2.getValueAt(r, 2).toString();
                        } catch (Exception e) {

                        }

                        try {
                            job = b.jTable2.getValueAt(r, 1).toString();
                        } catch (Exception e) {
                        }

// ha megvannak az adatok , megnezzuk , hogy foglalkoztunk e mar ezzel a kombinacioval
                        for (int k = 0; k < pnwsjob.size(); k++) {

                            if (pnwsjob.get(k).equals(pn + ws + job)) {

//ha egyezik atallitjuk az irtunke booleant igenre
                                continue outerloop;

                            }

                        }

//bejárjuk a row datát végig ha idáig eljutottunk
                        for (int n = 0; n < rowdata.length; n++) {

//4 eset lehetséges innentől
//ha nem kell first pass és nem kell grouppolni job ra
                            if (rowdata[n][1].equals(pn) && rowdata[n][0].toString().contains(ws) && !b.jCheckBox1.isSelected() && !b.jCheckBox3.isSelected()) {

                                osszeg += Long.parseLong(rowdata[n][4].toString());

                            }

//ha csak a first passra vagyunk kiváncsiak de nem kell grouppolni JOB ra
                            if (rowdata[n][1].equals(pn) && rowdata[n][0].toString().contains(ws) && b.jCheckBox1.isSelected() && rowdata[n][2].toString().equals("1") && !b.jCheckBox3.isSelected()) {

                                osszeg += Long.parseLong(rowdata[n][4].toString());

                            }

//ha nem csak a firstpass érdekel de grouppolni kell JOB ra
                            if (rowdata[n][1].equals(pn) && rowdata[n][0].toString().contains(ws) && !b.jCheckBox1.isSelected() && b.jCheckBox3.isSelected() && rowdata[n][3].toString().equals(job)) {

                                osszeg += Long.parseLong(rowdata[n][4].toString());

                            }

//ha csak first pass érdekel és grouppolunk is job ra
                            if (rowdata[n][1].equals(pn) && rowdata[n][0].toString().contains(ws) && b.jCheckBox1.isSelected() && b.jCheckBox1.isSelected() && rowdata[n][2].toString().equals("1") && b.jCheckBox3.isSelected() && rowdata[n][3].toString().equals(job)) {

                                osszeg += Long.parseLong(rowdata[n][4].toString());

                            }

                        }

//most jön az , hogy hova tegyük az összeszedett adatot (mindig a leg felső sorba ahol egyezés van és van terv felette)
//de ez az egész csak akkor játszik ha nagyobb az összeg mint nulla
                        if (osszeg > 0) {
                            for (int n = 0; n < b.jTable2.getRowCount(); n++) {

//ha tény sorban vagyunk
                                if (b.jTable2.getValueAt(n, 3).equals("Tény")) {

//ha kell foglalkozni a job-al
                                    try {
                                        if (b.jTable2.getValueAt(n - 1, i) != null && !b.jTable2.getValueAt(n - 1, i).equals("") && b.jTable2.getValueAt(n, 0).equals(pn) && b.jTable2.getValueAt(n, 2).equals(ws) && b.jCheckBox3.isSelected() && b.jTable2.getValueAt(n, 1).equals(job)) {

                                            Tc_Stringbolint c = new Tc_Stringbolint(b.jTable2.getValueAt(n, i).toString());
                                            b.jTable2.setValueAt(osszeg + " " + c.komment, n, i);
                                            pnwsjob.add(pn + ws + job);
                                            continue outerloop;

                                        }
                                    } catch (Exception e) {
                                    }

// ha nem kell foglalkozni a job al
                                    try {
                                        if (b.jTable2.getValueAt(n - 1, i) != null && !b.jTable2.getValueAt(n - 1, i).equals("") && b.jTable2.getValueAt(n, 0).equals(pn) && b.jTable2.getValueAt(n, 2).equals(ws) && !b.jCheckBox3.isSelected()) {

                                            Tc_Stringbolint c = new Tc_Stringbolint(b.jTable2.getValueAt(n, i).toString());
                                            b.jTable2.setValueAt(osszeg + " " + c.komment, n, i);
                                            pnwsjob.add(pn + ws + job);
                                            continue outerloop;
                                        }
                                    } catch (Exception e) {
                                    }
                                }
                            }

//ha eljutunk idáig az azt jelenti , hogy nem volt olyan sor ami felett van terv
                            for (int n = 0; n < b.jTable2.getRowCount(); n++) {
//ha tény sorban vagyunk
                                if (b.jTable2.getValueAt(n, 3).equals("Tény")) {
//ha kell foglalkozni a job-al
                                    try {
                                        if (b.jTable2.getValueAt(n, 0).equals(pn) && b.jTable2.getValueAt(n, 2).equals(ws) && b.jCheckBox3.isSelected() && b.jTable2.getValueAt(n, 1).equals(job)) {

                                            Tc_Stringbolint c = new Tc_Stringbolint(b.jTable2.getValueAt(n, i).toString());
                                            b.jTable2.setValueAt(osszeg + " " + c.komment, n, i);
                                            pnwsjob.add(pn + ws + job);
                                            continue outerloop;

                                        }
                                    } catch (Exception e) {
                                    }

// ha nem kell foglalkozni a job al
                                    try {
                                        if (b.jTable2.getValueAt(n, 0).equals(pn) && b.jTable2.getValueAt(n, 2).equals(ws) && !b.jCheckBox3.isSelected()) {

                                            Tc_Stringbolint c = new Tc_Stringbolint(b.jTable2.getValueAt(n, i).toString());
                                            b.jTable2.setValueAt(osszeg + " " + c.komment, n, i);
                                            pnwsjob.add(pn + ws + job);
                                            continue outerloop;
                                        }
                                    } catch (Exception e) {
                                    }
                                }
                            }
                        }

                    }

                    Tc_AnimationSFDC.rajzole = false;

                }
            }
        }
        Tc_AnimationSFDC.rajzole = false;
    }
}
