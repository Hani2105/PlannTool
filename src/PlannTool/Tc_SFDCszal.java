/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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

            dt = dt.plusMinutes(719);

        }

        // ha 8 van beallitva 8 at adunk hozza
        if (Tc_Betervezo.jComboBox1.getSelectedIndex() == 1) {

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

        //ebben taroljuk le a pn -eket hogy lassuk foglalkoztunk e már vele
        //List<String> pnlist = new ArrayList<String>();
        //pnlist.add("first");
        String[][] pnlista = new String[b.jTable2.getRowCount()][3];

        //elindulunk , bejarjuk a tablat es megkeressuk a megfelelo oszlopot
        for (int i = 4; i < b.jTable2.getColumnCount(); i++) {

            //ha egyezik az oszlop neve a datummal akkor bejarjuk  sorokat
            if (b.jTable2.getColumnName(i).substring(0, b.jTable2.getColumnName(i).length() - 4).equals(tol.replace("%20", " "))) {

                for (int r = 0; r < b.jTable2.getRowCount(); r++) {

                    //felvesszuk a pn-t valtozonak es a ws-t
                    String pn = "";
                    String ws = "";
                    String job = "";
                    boolean tovabbmegyunk = true;

                    try {
                        pn = b.jTable2.getValueAt(r, 0).toString();
                        ws = b.jTable2.getValueAt(r, 2).toString();
                        job = b.jTable2.getValueAt(r, 1).toString();
                    } catch (Exception e) {
                    };

                    //megvizsgaljuk , hogy foglalkoztunk e már ezzel a pn-el
                    for (int k = 0; k < pnlista.length; k++) {

                        try {
                            if (pn.equals(pnlista[k][0].toString()) && ws.equals(pnlista[k][1].toString()) && job.equals(pnlista[k][2].toString())) {

                                tovabbmegyunk = false;
                                break;
                            }
                        } catch (Exception e) {
                        }
                    } //ha nem akkor tovább megyünk a műveletekkel ha nem infó a sor és tény
                    if (tovabbmegyunk == true && !b.jTable2.getValueAt(r, 3).toString().equals("Infó") && b.jTable2.getValueAt(r, 3).toString().equals("Tény")) {

                        //végigpörgetjük a row datát és összeszedjük a pn hez tartozó darabszámot
                        int osszeg = 0;
                        for (int n = 0; n < rowdata.length; n++) {

                            if (rowdata[n][1].toString().equals(pn) && rowdata[n][0].toString().contains(ws) && b.jTable2.getValueAt(r, 3).toString().equals("Tény")) {

                                //osszeadjuk hatha tobbszor fordul elo a rowdataban
                                osszeg += Long.parseLong(rowdata[n][4].toString());

                            }

                        }

                        //beirjuk az összeget a terve alá vagy az utolsó sorba  és a pn-t a pnlistbe ha nem nulla az összeg
                        if (osszeg != 0) {

                            //bejarjuk az oszlopot vegig soronkent h megkeressuk a tervet vagy az utolso sorat
                            //irtunk e
                            boolean irtunke = false;
                            int sorszam = 0;
                            String cellaadat = "";
                            for (int n = 0; n < b.jTable2.getRowCount(); n++) {

                                String actualterv = "";
                                try {

                                    actualterv = b.jTable2.getValueAt(n - 1, i).toString();

                                } catch (Exception e) {

                                }

                                try {
                                    if (pn.equals(b.jTable2.getValueAt(n, 0).toString()) && ws.equals(b.jTable2.getValueAt(n, 2).toString()) && !actualterv.equals("") && b.jTable2.getValueAt(n, 3).toString().equals("Tény")) {

                                        //kivesszuk a tobo a vizet oszt ottmarad a béka
                                        boolean inte = true;
                                        int eddigmenni = 1;
                                        int db = 0;

                                        while (inte) {
                                            try {
                                                db = Integer.parseInt(b.jTable2.getValueAt(n, i).toString().substring(0, eddigmenni));
                                                eddigmenni++;
                                            } catch (Exception e) {

                                                inte = false;

                                            }
                                        }

                                        //replaceljuk a stringben a darabot semmire
                                        try {
                                            cellaadat = b.jTable2.getValueAt(n, i).toString().trim();
                                        } catch (Exception e) {
                                        }

                                        cellaadat = cellaadat.replace(String.valueOf(db), "");

                                        b.jTable2.setValueAt(osszeg + " " + cellaadat.trim(), n, i);
                                        //pnlist.add(pn);
                                        pnlista[n][0] = pn;
                                        pnlista[n][1] = ws;
                                        pnlista[n][2] = job;
                                        irtunke = true;

                                    } else if (pn.equals(b.jTable2.getValueAt(n, 0).toString()) && ws.equals(b.jTable2.getValueAt(n, 2).toString()) && b.jTable2.getValueAt(n, 3).toString().equals("Tény")) {

                                        sorszam = n;

                                    }
                                } catch (Exception e) {
                                }

                            }

                            //ha nem irtunk irunk az utolso sorba
                            if (irtunke == false && sorszam != 0) {

                                boolean inte = true;
                                int eddigmenni = 1;
                                int db = 0;

                                while (inte) {
                                    try {
                                        db = Integer.parseInt(b.jTable2.getValueAt(sorszam, i).toString().substring(0, eddigmenni));
                                        eddigmenni++;
                                    } catch (Exception e) {

                                        inte = false;

                                    }
                                }

                                //replaceljuk a stringben a darabot semmire
                                try {
                                    cellaadat = b.jTable2.getValueAt(sorszam, i).toString().trim();
                                } catch (Exception e) {
                                }

                                cellaadat = cellaadat.replace(String.valueOf(db), "");

                                b.jTable2.setValueAt(osszeg + " " + cellaadat.trim(), sorszam, i);
                                //pnlist.add(pn);
                                pnlista[sorszam][0] = pn;
                                pnlista[sorszam][1] = ws;
                                pnlista[sorszam][2] = job;

                                irtunke = true;

                            }

                        }
                    }
                }
            }

        }

        Tc_AnimationSFDC.rajzole = false;
        Tc_Calculator c = new Tc_Calculator(b);

    }

}
