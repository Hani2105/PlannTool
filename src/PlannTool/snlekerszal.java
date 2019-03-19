/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool;

import static PlannTool.ablak.jTabbedPane1;
import static PlannTool.ablak.lista;
import static PlannTool.ablak.modelstatus;
import static PlannTool.ablak.stat;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author gabor_hanacsek
 */
public class snlekerszal extends Thread {

    public void run() {

        modelstatus = (DefaultTableModel) ablak.jTable4.getModel();
        String SN = "";
        URL url = null;

        for (int i = 0; i < modelstatus.getRowCount(); i++) {

            for (int j = 0; j < modelstatus.getColumnCount() - 1; j++) {

                modelstatus.setValueAt("", i, j + 1);
            }

            if (modelstatus.getValueAt(i, 0) != null) {
                SN += modelstatus.getValueAt(i, 0).toString().toUpperCase().trim() + ";";
                SN = SN.replace(" ", "");
            }
        }

        SN = normalizer.removeAccents(SN);
        SN = SN.replace("#", "%23");

        xmlfeldolg xxx = new xmlfeldolg();
        Object rowdata[][] = null;

        try {

            url = new URL("http://143.116.140.120/rest/request.php?page=planning_snisoh&serial=" + SN + "&format=xml");
            ArrayList<String> lista = new ArrayList();

            String nodelist = "planning_snisoh";
            lista.add("Serial_Number");
            lista.add("Shop_Order_Number");
            lista.add("Part_Number");
            lista.add("unit_status");
            lista.add("last_complete_dateTime");

            rowdata = (Object[][]) xxx.xmlfeldolg(url, nodelist, lista);

        } catch (MalformedURLException ex) {
            Logger.getLogger(ablak.class.getName()).log(Level.SEVERE, null, ex);
        }

        modelstatus = (DefaultTableModel) xxx.totablestatus(modelstatus, rowdata);

        ablak.jTable4.setModel(modelstatus);
        ablak.jButton7.setEnabled(true);

        // összesítés
        String pn = "";
        int pndarab = 0;
        int k = 0;
        boolean vanemar = false;
        DefaultTableModel sumtabla = new DefaultTableModel();
        sumtabla = (DefaultTableModel) ablak.jTable11.getModel();
        sumtabla.setRowCount(0);

        for (int i = 0; i < ablak.jTable4.getRowCount(); i++) {

            try {
                if (!ablak.jTable4.getValueAt(i, 0).toString().equals(""));
                {

                    pn = ablak.jTable4.getValueAt(i, 3).toString();
                    vanemar = false;
                    pndarab = 0;

                    for (int m = 0; m < sumtabla.getRowCount(); m++) {
                        if (pn.equals(sumtabla.getValueAt(m, 0).toString())) {
                            vanemar = true;

                        }
                    }

                    if (vanemar == false) {
                        for (int n = i; n < ablak.jTable4.getRowCount(); n++) {

                            if (pn.equals(ablak.jTable4.getValueAt(n, 3).toString())) {
                                pndarab++;
                            }

                        }

                        try {

                            sumtabla.addRow(new Object[]{pn, pndarab});

                        } catch (Exception e) {
                            System.out.println("nem jott ossze");
                        }

                    }

                }
            } catch (Exception e) {
            };

        }

        ablak.jTable11.setModel(sumtabla);
        //berakjuk az adatokat a sum táblából

        String adatok = "";

        for (int i = 0; i < ablak.jTable11.getRowCount(); i++) {

            adatok += "'" + ablak.jTable11.getValueAt(i, 0).toString() + "',";

        }

        adatok = adatok.substring(0, adatok.length() - 1);

        String Query = "SELECT oracle_backup_subinv.item , oracle_backup_subinv.subinv , oracle_backup_subinv.quantity FROM trax_mon.oracle_backup_subinv where oracle_backup_subinv.item in (" + adatok + ")";

        connect con = new connect(Query);

        // betesszuk tombbe
        try {

            int utsosor;
            con.rs.last();
            utsosor = con.rs.getRow();
            con.rs.beforeFirst();
            String[][] listaelem = new String[utsosor][3];
            int i = 0;
            while (con.rs.next()) {

                listaelem[i][0] = con.rs.getString(1);
                listaelem[i][1] = con.rs.getString(2);
                listaelem[i][2] = con.rs.getString(3);

                i++;

            }

            //betesszuk a tombot a listbe
            lista.clear();
            lista.add(listaelem);

        } catch (SQLException ex) {
            Logger.getLogger(ablak.class.getName()).log(Level.SEVERE, null, ex);
        }

        animation.rajzol = false;
        ablak.jTable4.repaint();

        stat.beir(System.getProperty("user.name"), jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()), "", "gabor.hanacsek@sanmina.com");

    }

}
