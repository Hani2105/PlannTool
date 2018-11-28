/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool;

import static PlannTool.ablak.jTabbedPane1;
import static PlannTool.ablak.stat;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author gabor_hanacsek
 */
public class OhQueryszal extends Thread {

    public void run() {

        xmlfeldolg xmlfeldolg_lekerdez_button = new xmlfeldolg();
        SimpleDateFormat api_date_format = new SimpleDateFormat("yyyy-MM-dd");

        Date dt = new Date();

        Calendar c = Calendar.getInstance();
        c.setTime(ablak.jDateChooser4.getDate());
        c.add(Calendar.DATE, 1);
        dt = c.getTime();
        String pn_string = "";

        String api_string = "http://143.116.140.120/rest/request.php?page=planning_activity&starttime=" + api_date_format.format(ablak.jDateChooser2.getDate()) + "&endtime="
                + api_date_format.format(dt);

        for (int x = 0; x < ablak.jTable8.getModel().getRowCount(); x++) {

            pn_string += "&pn" + Integer.toString(x + 1) + "=%" + ablak.jTable8.getValueAt(x, 0) + "%";

        }

        pn_string += "&format=xml";
        api_string += pn_string;
        ArrayList<String> api_lista = new ArrayList();
        api_lista.add("part_number");
        api_lista.add("serial_number");
        Object api_array[][] = null;

        try {
            URL api_url = new URL(api_string);
            api_array = (Object[][]) xmlfeldolg_lekerdez_button.xmlfeldolg(api_url, "planning_activity", api_lista);
        } catch (MalformedURLException ex) {
            Logger.getLogger(ablak.class.getName()).log(Level.SEVERE, null, ex);

        } catch (Exception e) {

            infobox inf = new infobox();
            inf.infoBox("Valószínűleg nem megy a planning_activity API , értesítsd a System csapatot!", "Valami nincs rendben!");
        }

        if (api_array.length != 0) {

            planconnect planconnection_temp = new planconnect();
            String serials = "";
            ResultSet rss = null;
            ArrayList<String> sn_list = new ArrayList<String>();

            for (int x = 0; x < api_array.length; x++) {
                serials = serials + "'" + api_array[x][1].toString() + "',";
            }

            serials = serials.substring(0, serials.length() - 1);

            try {
                rss = (ResultSet) planconnection_temp.planconnect("SELECT serial FROM planningdb.oh_querymain where megcsinalva = 'Y';");
                while (rss.next()) {
                    sn_list.add(rss.getString(1));

                }

            } catch (SQLException ex) {
                Logger.getLogger(ablak.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ablak.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            planconnection_temp.kinyir();

            for (int x = 0; x < api_array.length; x++) {

                for (int y = 0; y < sn_list.size(); y++) {
                    try {
                        if (api_array[x][1].toString().equals(sn_list.get(y).toString())) {
                            api_array[x][0] = null;
                            api_array[x][1] = null;
                        }
                    } catch (Exception e) {

                    }
                }

            }

            // a kivételes pn ek kivétele
            sn_list.clear();

            for (int i = 0; i < ablak.jTable10.getModel().getRowCount(); i++) {

                if (ablak.jTable10.getValueAt(i, 0) != null) {

                    sn_list.add(ablak.jTable10.getValueAt(i, 0).toString());
                }

            }

            for (int x = 0; x < api_array.length; x++) {

                for (int y = 0; y < sn_list.size(); y++) {
                    try {
                        if (api_array[x][0].toString().equals(sn_list.get(y).toString())) {
                            api_array[x][0] = null;
                            api_array[x][1] = null;
                        }
                    } catch (Exception e) {

                    }
                }

            }

            DefaultTableModel maintable = new DefaultTableModel();
            maintable = (DefaultTableModel) ablak.jTable7.getModel();
            maintable.setRowCount(0);
            Object a = api_date_format.format(ablak.jDateChooser4.getDate());
            Object b = api_date_format.format(ablak.jDateChooser2.getDate());

            for (int i = 0; i < api_array.length; i++) {

                if (api_array[i][0] != null) {

                    maintable.addRow(new Object[]{api_array[i][0], api_array[i][1], b, a, false});

                }

            }

            ablak.jTable7.setModel(maintable);

            //osszesites
            String pn = "";
            int pndarab = 0;
            int k = 0;
            boolean vanemar = false;
            DefaultTableModel sumtabla = new DefaultTableModel();
            sumtabla = (DefaultTableModel) ablak.jTable9.getModel();
            sumtabla.setRowCount(0);

            for (int i = 0; i < ablak.jTable7.getRowCount(); i++) {

                pn = ablak.jTable7.getValueAt(i, 0).toString();
                vanemar = false;
                pndarab = 0;

                for (int m = 0; m < sumtabla.getRowCount(); m++) {
                    if (pn.equals(sumtabla.getValueAt(m, 0).toString())) {
                        vanemar = true;

                    }
                }

                if (vanemar == false) {
                    for (int n = i; n < ablak.jTable7.getRowCount(); n++) {

                        if (pn.equals(ablak.jTable7.getValueAt(n, 0).toString())) {
                            pndarab++;
                        }

                    }

                    try {

                        sumtabla.addRow(new Object[]{pn, pndarab, false});

                    } catch (Exception e) {
                        System.out.println("nem jott ossze");
                    }

                }

            }

            ablak.jTable9.setModel(sumtabla);

        } else {
            infobox infobox_api = new infobox();
            infobox_api.infoBox("A megadott paraméterekkel nem volt találat!", "Info");

        }

//feltoltjuk a lekerdezett adatokat
        String adatok = "";
        String mentve = "";
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String user = System.getProperty("user.name");
        String levelbe = " \n Figyelem! \n Query húzás történt ekkor: " + dtf.format(now) + "\n" + "Ezen az időközön: " + api_date_format.format(ablak.jDateChooser2.getDate()) + " - " + api_date_format.format(dt) + "\n A következő találatokkal: \n";
        for (int i = 0; i < ablak.jTable7.getRowCount(); i++) {

            //System.out.println(jTable7.getValueAt(i, 4));
            try {
                if (ablak.jTable7.getValueAt(i, 4).equals(true)) {

                    mentve = "Y";

                } else {
                    mentve = "N";
                }
            } catch (Exception e) {

                mentve = "N";
            }

            adatok += "('" + ablak.jTable7.getValueAt(i, 1) + "','" + ablak.jTable7.getValueAt(i, 0) + "','" + ablak.jTable7.getValueAt(i, 2) + "','" + ablak.jTable7.getValueAt(i, 3) + "','" + mentve + "','" + user + "'),";
            levelbe += ablak.jTable7.getValueAt(i, 0).toString() + "   " + ablak.jTable7.getValueAt(i, 1).toString() + "\n";
        }

        levelbe += " \n Összesen: \n";

        for (int i = 0; i < ablak.jTable9.getRowCount(); i++) {

            levelbe += ablak.jTable9.getValueAt(i, 0).toString() + "  " + ablak.jTable9.getValueAt(i, 1).toString() + " DB\n";

        }

        levelbe += "Az adatokat automatikusan feltöltöttük az adatbázisba!";

        planconnect pc = new planconnect();
        String query = "";
        if (adatok.length() > 0) {
            adatok = adatok.substring(0, adatok.length() - 1);

            query = "insert into oh_querymain (serial,partnumber,tol,ig,megcsinalva,felhasznalo) values" + adatok + "on duplicate key update megcsinalva = values (megcsinalva), felhasznalo = values (felhasznalo)";

        } else {

            infobox info = new infobox();
            info.infoBox("A query nem hozott olyan eredményt amire OH-t kéne adni \n a kivételek és prefixek alapján!", "Query húzás!");

        }

        try {
            pc.feltolt(query, false);

//            infobox inf = new infobox();
//            inf.infoBox("A feltöltés sikeres!", "Feltöltés!");
        } catch (Exception e) {

            e.printStackTrace();

//            infobox inf = new infobox();
//            inf.infoBox("A feltöltés sikertelen!", "Feltöltés!");
        }

        animation.rajzol = false;

        stat.beir(user, jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()), levelbe, "gabor.hanacsek@sanmina.com,roland.bognar@sanmina.com,gina.gerecz@sanmina.com,eva.inczedi@sanmina.com,paloma.pal@sanmina.com");

    }

}
