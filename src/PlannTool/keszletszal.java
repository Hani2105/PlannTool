/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool;

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

    ablak a;

    keszletszal(ablak a) {

        this.a = a;

    }

    ;

    public void run() {

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

        ablak.model = (DefaultTableModel) xxx.totable(ablak.model, rowdata);

        ablak.jTable1.setModel(ablak.model);

        // OH tábla
        String mitkeres = ablak.jTextField2.getText().trim();

        String query = "SELECT oracle_backup_subinv.item as partnumber , oracle_backup_subinv.subinv , oracle_backup_subinv.locator , oracle_backup_subinv.quantity FROM trax_mon.oracle_backup_subinv where item like '%" + mitkeres + "%'";
        connect onhend = new connect((query));

        ablak.model1 = (DefaultTableModel) ablak.jTable2.getModel();
        ablak.model1.setRowCount(0);

        try {
            while (onhend.rs.next()) {

                String pn = onhend.rs.getString(1);
                String subinv = onhend.rs.getString(2);
                String locator = onhend.rs.getString(3);
                String qty = onhend.rs.getString(4);
                ablak.model1.addRow(new Object[]{pn, subinv, locator, qty});

            }
        } catch (SQLException ex) {
            Logger.getLogger(ablak.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        ablak.jTable2.setModel(ablak.model1);
        
        //fun
        if (System.getProperty("user.name").toString().equals("eva_istenes")) {

            Stitch s = new Stitch();
            s.setVisible(true);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(keszletszal.class.getName()).log(Level.SEVERE, null, ex);
            }

            s.dispose();

        }

        animation.rajzol = false;

        ablak.stat.beir(System.getProperty("user.name"), ablak.jTabbedPane1.getTitleAt(ablak.jTabbedPane1.getSelectedIndex()), "", "gabor.hanacsek@sanmina.com");

        try {
            warning wg = new warning();
            wg.keszlet(a);
        } catch (Exception e) {
        }

    }

}
