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
import javax.swing.table.DefaultTableModel;
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
        List<String> pnws = new ArrayList<>();
//ez lesz a kapcsoloja
        boolean irtunke = false;

//elindulunk , bejarjuk a tablat es megkeressuk a megfelelo oszlopot
        for (int i = 4; i < b.jTable2.getColumnCount(); i++) {

//ha egyezik az oszlop neve a datummal 
            if (b.jTable2.getColumnName(i).substring(0, b.jTable2.getColumnName(i).length() - 4).equals(tol.replace("%20", " "))) {

//bejarjuk a sorokat
                for (int r = 0; r < b.jTable2.getRowCount(); r++) {

                    irtunke = false;

//felvesszuk az elso pn-t és ws-t
//try az info sorok miatt
                    String pn = "";
                    String ws = "";

                    try {
                        pn = b.jTable2.getValueAt(r, 0).toString();
                        ws = b.jTable2.getValueAt(r, 2).toString();
                    } catch (Exception e) {

                    }
// ha megvannak az adatok , megnezzuk , hogy foglalkoztunk e mar ezzel a kombinacioval
                    for (int k = 0; k < pnws.size(); k++) {

                        if (pnws.get(k).equals(pn + ws)) {

//ha egyezik atallitjuk az irtunke booleant igenre
                            irtunke = true;

                        }

                    }

                    
//beforgatjuk a rowdatat es összeszedjuk az össz darabszamot a pn-hez es job-hoz ha nem nulla egyik ertek sem es meg nem irtunk
//osszdarab
                    if (!pn.equals("") && !ws.equals("") && irtunke == false) {
                        Long osszdarab = (long) 0;
                        for (int data = 0; data < rowdata.length; data++) {

//ha egyezik a pn és a ws osszeadjuk az osszegeket (ha nincs kipipalva a checkbox)
                            if (rowdata[data][1].equals(pn) && rowdata[data][0].toString().contains(ws) && b.jCheckBox1.isSelected() == false) {

                                osszdarab += Long.parseLong(rowdata[data][4].toString());

                            }
//ha egyezik a pn és a ws osszeadjuk az osszegeket (ha ki van pipálva)
                            if (rowdata[data][1].equals(pn) && rowdata[data][0].toString().contains(ws) && b.jCheckBox1.isSelected() == true && rowdata[data][2].toString().equals("1")) {

                                osszdarab += Long.parseLong(rowdata[data][4].toString());

                            }

                        }

//megprobalunk keresni egy olyan sort ami megfelel a pn-nek ws-nek es van felette terv
                        int ebbeirni = 0;

                        for (int m = 0; m < b.jTable2.getRowCount(); m++) {

                            try {
                                if (b.jTable2.getValueAt(m, 0).equals(pn) && b.jTable2.getValueAt(m, 2).equals(ws) && b.jTable2.getValueAt(m - 1, i) != null && b.jTable2.getValueAt(m, 3).toString().equals("Tény") && !b.jTable2.getValueAt(m - 1, i).equals("") && b.jTable2.getValueAt(m - 1, 3).equals("Terv")) {

                                    ebbeirni = m;
                                    break;

                                }
                            } catch (Exception e) {
                            }
                        }

//beirjuk a darabot a tablaba ha az nagyobb mint nulla van olyan sorunk amibe tudunk irni mert van felette terv
                        if (osszdarab > 0 && ebbeirni > 0) {

//hogy ottmaradjon a komment hasznaljuk a stringbolintet
                            try {
                                Tc_Stringbolint c = new Tc_Stringbolint(b.jTable2.getValueAt(ebbeirni, i).toString());
                                b.jTable2.setValueAt(osszdarab + " " + c.komment, ebbeirni, i);
                            } catch (Exception e) {
//ha nem tudjuk stringe konvertalni akkor csak a darab marad

                                b.jTable2.setValueAt(osszdarab, ebbeirni, i);

                            }

//ha ugyan ehhez a pn hez van még beírva ebbe az oszlopba másik darab , azt nullra állítom 
                            for (int t = ebbeirni + 1; t < b.jTable2.getRowCount(); t++) {

                                if (b.jTable2.getValueAt(t, i) != null && b.jTable2.getValueAt(t, 0).equals(pn) && b.jTable2.getValueAt(t, 2).equals(ws) && b.jTable2.getValueAt(t, 3).equals("Tény")) {

                                    b.jTable2.setValueAt(null, t, i);

                                }

                            }

//ha irtunk elrakjuk a pn-t ws-t egy listaba , hogy lassuk , foglalkoztunk mar vele
                            pnws.add(pn + ws);

                        } //ha nincs olyan sor amibe írhatnánk de van összegünk                            
                        else if (osszdarab > 0 && ebbeirni == 0 && b.jTable2.getValueAt(r, 0).equals(pn) && b.jTable2.getValueAt(r, 2).equals(ws) && b.jTable2.getValueAt(r, 3).equals("Tény")) {

//probalunk keresni egy olyan sort amibe már van írva
                            int utsosor = 0;

                            for (int t = 0; t < b.jTable2.getRowCount(); t++) {

                                try {
                                    if (b.jTable2.getValueAt(t, 0).equals(pn) && b.jTable2.getValueAt(t, 2).equals(ws) && b.jTable2.getValueAt(t, 3).equals("Tény") && b.jTable2.getValueAt(t, i) != null) {

                                        utsosor = t;

                                    }
                                } catch (Exception e) {
                                }

                            }

//megkeressuk az utolso sort amibe irhatunk mert ugyan az a pn ws es teny es meg nincs sehova írva
                            if (utsosor == 0) {
                                for (int t = 0; t < b.jTable2.getRowCount(); t++) {

                                    try {
                                        if (b.jTable2.getValueAt(t, 0).equals(pn) && b.jTable2.getValueAt(t, 2).equals(ws) && b.jTable2.getValueAt(t, 3).equals("Tény")) {

                                            utsosor = t;

                                        }
                                    } catch (Exception e) {
                                    }

                                }
                            }

                            //hogy ottmaradjon a komment hasznaljuk a stringbolintet
                            try {
                                Tc_Stringbolint c = new Tc_Stringbolint(b.jTable2.getValueAt(utsosor, i).toString());
                                b.jTable2.setValueAt(osszdarab + " " + c.komment, utsosor, i);
                            } catch (Exception e) {
//ha nem tudjuk stringe konvertalni akkor csak a darab marad

                                b.jTable2.setValueAt(osszdarab, utsosor, i);

                            }

//ha ugyan ehhez a pn hez van még beírva ebbe az oszlopba másik darab , azt nullra állítom 
                            for (int t = 0; t < b.jTable2.getRowCount(); t++) {

                                try {
                                    if (b.jTable2.getValueAt(t, i) != null && b.jTable2.getValueAt(t, 0).equals(pn) && b.jTable2.getValueAt(t, 2).equals(ws) && b.jTable2.getValueAt(t, 3).equals("Tény") && t != utsosor) {

                                        b.jTable2.setValueAt(null, t, i);

                                    }
                                } catch (Exception e) {
                                }

                            }

//ha irtunk elrakjuk a pn-t ws-t egy listaba , hogy lassuk , foglalkoztunk mar vele
                            pnws.add(pn + ws);

                        }

                    }

                }

            }

        }

        Tc_AnimationSFDC.rajzole = false;

    }

}
