/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool;

import PlannTool.ANIMATIONS.animation;
import static PlannTool.ablak.jTabbedPane1;
import static PlannTool.ablak.stat;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author gabor_hanacsek
 */
public class Activityszal extends Thread {

    public void run() {

        String part;
        part = ablak.jTextField3.getText().trim().toUpperCase();
        Date tol = ablak.jDateChooser1.getDate();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd%20HH:mm:ss");
        String stol = df.format(tol);
        stol = stol.substring(0, 13) + ablak.jTextField8.getText() + ":00";
        Date ig = ablak.jDateChooser3.getDate();
        String sig = df.format(ig);
        sig = sig.substring(0, 13) + ablak.jTextField9.getText() + ":00";

        ablak.modelacti = (DefaultTableModel) ablak.jTable3.getModel();
        ablak.modelacti.setRowCount(0);
        URL url = null;

        xmlfeldolg xxx = new xmlfeldolg();
        Object rowdata[][] = null;

        try {

            url = new URL("http://143.116.140.120/rest/request.php?page=planning_realisation&product=" + part + "&starttime=" + stol + "&endtime=" + sig + "&format=xml");
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
            Logger.getLogger(ablak.class.getName()).log(Level.SEVERE, null, ex);
        }

        ablak.modelacti = (DefaultTableModel) xxx.totable(ablak.modelacti, rowdata);

        ablak.jTable3.setModel(ablak.modelacti);

        ablak.modelactilist.clear();
        String[][] listaba = new String[ablak.modelacti.getRowCount()][7];
        for (int i = 0; i < ablak.modelacti.getRowCount(); i++) {

            listaba[i][0] = ablak.modelacti.getValueAt(i, 0).toString();
            listaba[i][1] = ablak.modelacti.getValueAt(i, 1).toString();
            listaba[i][2] = ablak.modelacti.getValueAt(i, 2).toString();
            listaba[i][3] = ablak.modelacti.getValueAt(i, 3).toString();
            listaba[i][4] = ablak.modelacti.getValueAt(i, 4).toString();
            listaba[i][5] = ablak.modelacti.getValueAt(i, 5).toString();
            listaba[i][6] = ablak.modelacti.getValueAt(i, 6).toString();

        }

        ablak.modelactilist.add(listaba);

        activityszuro sz = new activityszuro();

//        sz.listabolszurokprefixet();
//        sz.listabolszurokallomast();
        sz.listabolszurok();
        sz.activitygroup();

        stat.beir(System.getProperty("user.name"), jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()), "", "gabor.hanacsek@sanmina.com");
        animation.rajzol = false;
    }

}
