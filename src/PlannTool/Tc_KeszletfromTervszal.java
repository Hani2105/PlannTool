/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool;

import static PlannTool.Tc_Keszletfromterv.jTable1;
import static PlannTool.Tc_Keszletfromterv.jTable2;
import static PlannTool.Tc_Keszletfromterv.jTextField2;
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
public class Tc_KeszletfromTervszal extends Thread {

    Tc_Keszletfromterv k;

    public Tc_KeszletfromTervszal(Tc_Keszletfromterv k) {

        this.k = k;

    }

    public void run() {

//kinullazzuk a keresoket
        k.jTextField1.setText("");
        k.jTextField13.setText("");
        k.jTextField6.setText("");
        k.jTable1.setRowSorter(null);
        k.jTable2.setRowSorter(null);

        DefaultTableModel model = (DefaultTableModel) k.jTable1.getModel();
        DefaultTableModel model1 = (DefaultTableModel) k.jTable2.getModel();

        URL url = null;

        model.setRowCount(0);
        model1.setRowCount(0);

        xmlfeldolg xxx = new xmlfeldolg();
        Object rowdata[][] = null;

        try {

            url = new URL("http://143.116.140.120/rest/request.php?page=planning_shipment_plan_process_all&product=" + k.jTextField2.getText().trim().toUpperCase() + "&format=xml");
            ArrayList<String> lista = new ArrayList();

            String nodelist = "planning_shipment_plan_process_all";
            lista.add("Part_Number");
            lista.add("SFDC_Location_Name");
            lista.add("Serial_Number");

            rowdata = (Object[][]) xxx.xmlfeldolg(url, nodelist, lista);

        } catch (MalformedURLException ex) {
            Logger.getLogger(ablak.class.getName()).log(Level.SEVERE, null, ex);
        }

        model = (DefaultTableModel) xxx.totable(model, rowdata);

        k.jTable1.setModel(model);

        // OH tábla
        String mitkeres = k.jTextField2.getText().trim();

        String query = "SELECT oracle_backup_subinv.item as partnumber , oracle_backup_subinv.subinv , oracle_backup_subinv.locator , oracle_backup_subinv.quantity FROM trax_mon.oracle_backup_subinv where item like '%" + mitkeres + "%'";
        connect onhend = new connect((query));

        model1 = (DefaultTableModel) k.jTable2.getModel();
        model1.setRowCount(0);

        try {
            while (onhend.rs.next()) {

                String pn = onhend.rs.getString(1);
                String subinv = onhend.rs.getString(2);
                String locator = onhend.rs.getString(3);
                String qty = onhend.rs.getString(4);
                model1.addRow(new Object[]{pn, subinv, locator, qty});

            }
        } catch (SQLException ex) {

        }

        k.jTable2.setModel(model1);

        //fun
        if (System.getProperty("user.name").toString().equals("eva_istenes")) {

            Stitch s = new Stitch();
            s.setVisible(true);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {

            }

            s.dispose();

        }

        animation.rajzol = false;

        ablak.stat.beir(System.getProperty("user.name"), ablak.jTabbedPane1.getTitleAt(ablak.jTabbedPane1.getSelectedIndex()), "", "gabor.hanacsek@sanmina.com");

    }

}
