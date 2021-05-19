/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool;

import PlannTool.ANIMATIONS.animation;
import PlannTool.CONNECTS.connect;
import PlannTool.CONNECTS.postgretraxmon;
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
public class keszletszal extends Thread {

    public void run() {

//kitoroljuk a szűrő ablakokat
        ablak.jTextField1.setText("");
        ablak.jTextField13.setText("");
        ablak.jTextField6.setText("");
        ablak.jTable1.setRowSorter(null);
        ablak.jTable2.setRowSorter(null);

        ablak.model = (DefaultTableModel) ablak.jTable1.getModel();

        URL url = null;

        ablak.model.setRowCount(0);
        ablak.model1.setRowCount(0);

        xmlfeldolg xxx = new xmlfeldolg();
        Object rowdata[][] = null;

        try {

            url = new URL("http://143.116.140.120/rest/request.php?page=planning_shipment_plan_process_all&product=" + ablak.jTextField2.getText().trim().toUpperCase() + "&format=xml");
            ArrayList<String> lista = new ArrayList();

            String nodelist = "planning_shipment_plan_process_all";
            lista.add("Part_Number");
            lista.add("SFDC_Location_Name");
            lista.add("Serial_Number");

            rowdata = (Object[][]) xxx.xmlfeldolg(url, nodelist, lista);

        } catch (MalformedURLException ex) {
            Logger.getLogger(ablak.class.getName()).log(Level.SEVERE, null, ex);
        }

        //betesszuk a wiplistbe a rowdata adatait , de előtte kiürítjük
        ablak.wiplist.clear();

        for (int i = 0; i < rowdata.length; i++) {
            String[] wiplista = new String[3];

            wiplista[0] = rowdata[i][0].toString();
            wiplista[1] = rowdata[i][1].toString();
            wiplista[2] = rowdata[i][2].toString();

            ablak.wiplist.add(wiplista);
        }

        //lefuttatjuk a szűrőt
        wipszuro w = new wipszuro(ablak.jTable1);
        ablak.jTable1.setModel(w.smtkiszed());

        // OH tábla
        synchronized (ablak.jTable2) {
            String mitkeres = ablak.jTextField2.getText().trim();
            ablak.model1 = (DefaultTableModel) ablak.jTable2.getModel();
            ablak.model1.setRowCount(0);
            String query = "SELECT oracle_backu_subinv.item as partnumber , oracle_backup_subinv.subinv , oracle_backup_subinv.locator , oracle_backup_subinv.quantity , oracle_backup_subinv.exported FROM trax_mon.oracle_backup_subinv where item like '%" + mitkeres + "%'";
            connect onhend = null;
            String exportdate = "";
            try {
                onhend = new connect((query));
                while (onhend.rs.next()) {

                    String pn = onhend.rs.getString(1);
                    String subinv = onhend.rs.getString(2);
                    String locator = onhend.rs.getString(3);
                    String qty = onhend.rs.getString(4);
                    ablak.model1.addRow(new Object[]{pn, subinv, locator, qty});
                    exportdate = "Az oracle export parserer futott: " + onhend.rs.getString(5);

                }
            } catch (Exception e) {

                //ide kell tenni az uj kapcsolatot ha a régi hibát dob------------------------------------------------------------------------------
                query = "SELECT  item, subinv, locator, quantity, exported FROM ois.subinventory_quantities_report where item like '%" + mitkeres.toUpperCase() + "%'";
                postgretraxmon ptm = new postgretraxmon();
                try {
                    ptm.lekerdez(query);
                    while (ptm.rs.next()) {
                        String pn = ptm.rs.getString("item");
                        String subinv = ptm.rs.getString("subinv");
                        String locator = ptm.rs.getString("locator");
                        String qty = ptm.rs.getString("quantity");
                        ablak.model1.addRow(new Object[]{pn, subinv, locator, qty});
                        exportdate = ptm.rs.getString("exported").substring(0, 16);

                    }
                } catch (SQLException ex) {
                    Logger.getLogger(keszletszal.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(keszletszal.class.getName()).log(Level.SEVERE, null, ex);
                }

                ptm.kinyir();

            }
            try {
                onhend.kinyir();
            } catch (Exception e) {
            }

            ablak.jLabel21.setText(exportdate);
            ablak.jTable2.setModel(ablak.model1);

            animation.rajzol = false;

            ablak.stat.beir(System.getProperty("user.name"), ablak.jTabbedPane1.getTitleAt(ablak.jTabbedPane1.getSelectedIndex()), "", "gabor.hanacsek@sanmina.com");

//        try {
//            warning wg = new warning();
//            wg.keszlet(a);
//        } catch (Exception e) {
//        }
        }
    }

}
