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
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author gabor_hanacsek
 */
public class Jobinfoszal extends Thread {

    public void run() {

        ablak.jobstatus = (DefaultTableModel) ablak.jTable5.getModel();

        URL url = null;

        ablak.jobstatus.setRowCount(0);

        xmlfeldolg xxx = new xmlfeldolg();
        Object rowdata[][] = null;

        try {

            String urlstring = "http://143.116.140.120/rest/request.php?page=planning_shop_order&shoporder=" + ablak.jTextField7.getText().trim() + "&format=xml";
            urlstring = urlstring.trim();
            url = new URL(urlstring);
            ArrayList<String> lista = new ArrayList();

            String nodelist = "planning_shop_order";
            lista.add("Shop_Order_Number");
            lista.add("Part_Number");
            lista.add("Workstation");
            lista.add("Qty");
            lista.add("Unit_Status");

            rowdata = (Object[][]) xxx.xmlfeldolg(url, nodelist, lista);

        } catch (MalformedURLException ex) {
            Logger.getLogger(ablak.class.getName()).log(Level.SEVERE, null, ex);
        }

        ablak.jobstatus = (DefaultTableModel) xxx.totable(ablak.jobstatus, rowdata);

        ablak.jTable5.setModel(ablak.jobstatus);

        int qty = 0;

        for (int i = 0; i < ablak.jobstatus.getRowCount(); i++) {

            qty += Integer.parseInt(ablak.jobstatus.getValueAt(i, 3).toString());

        }

        ablak.jLabel12.setText("JOB total QTY: " + qty);
        
        animation.rajzol = false;

        stat.beir(System.getProperty("user.name"), jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()), "", "gabor.hanacsek@sanmina.com");

    }

}
