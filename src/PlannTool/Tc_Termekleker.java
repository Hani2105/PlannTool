/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

/**
 *
 * @author gabor_hanacsek
 */
public class Tc_Termekleker extends Thread {

    Tc_Besheet b;

    public Tc_Termekleker(Tc_Besheet b) {
        this.b = b;

    }

    public void run() {

        String pn = "";
        //felvesszük a partnumbert
        pn = b.jTable2.getValueAt(b.jTable2.getSelectedRow(), 0).toString().trim();
        //idopontok kitalalasa
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

        //összeadjuk az ominózus PN darabszámait állomásonként
        Tc_Termeklekerablak a = new Tc_Termeklekerablak();
        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) a.jTable1.getModel();

        //bejárjuk az sfdc eredmenyet
        for (int i = 0; i < rowdata.length; i++) {
            //ha egyezik a pn bejarjuk a modellt es megnezzuk hogy ezzel a ws el irtunk e már
            boolean irtunke = false;
            int beir = 0;

//ha egyezik a pn beirjuk a tablaba
            if (rowdata[i][1].toString().equals(pn)) {

                model.addRow(new Object[]{pn, rowdata[i][3].toString(), rowdata[i][0].toString(), rowdata[i][4].toString()});

//                for (int t = 0; t < model.getRowCount(); t++) {
//
//                    //ha találunk ilyet
//                    if (rowdata[i][0].toString().equals(model.getValueAt(t, 1))) {
//                        irtunke = true;
//                        int qty = 0;
//
//                        qty = Integer.parseInt(model.getValueAt(t, 2).toString());
//                        beir += qty;
//
//                    }
//
//                }
//                //ha nem találunk akkor hozzáadunk
//                if (irtunke == false) {
//
//                    model.addRow(new Object[]{pn, rowdata[i][0].toString(), rowdata[i][4].toString()});
//
//                }
            }

        }

        a.jTable1.setModel(model);
        a.setVisible(true);
        Tc_AnimationSFDC.rajzole = false;

    }

}
