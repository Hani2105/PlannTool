/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool;

import static PlannTool.ablak.jTabbedPane1;
import static PlannTool.ablak.stat;
import java.net.URL;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author gabor_hanacsek
 */
public class Jobfigyeloszal extends Thread {

    public void run() {

        List<String> allomasok = new ArrayList<String>();
        allomasok = ablak.jList1.getSelectedValuesList();
        boolean irtunke;

        //feldolgozzuk stringge
        String allomasokquerybe = "";
        for (int i = 0; i < allomasok.size(); i++) {

            allomasokquerybe += "'" + allomasok.get(i) + "',";

        }

        allomasokquerybe = allomasokquerybe.substring(0, allomasokquerybe.length() - 1);

        //kiszedjuk a datumot
        Date tol = ablak.jDateChooser5.getDate();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd 06:00:00");
        String stol = df.format(tol);

        //smt lekérdezés sor , job , pn  a megadott dátumtól
        String Query = "select distinct stations.name , terv.partnumber , terv.job , terv.startdate , terv.qty_full from stations left join terv on terv.stationid = stations.id where terv.active = 1 and terv.startdate >= '" + stol + "' and stations.name in (" + allomasokquerybe + ") group by terv.job order by stations.name asc , terv.startdate asc";
        //vegrehajtjuk

        planconnect pc = new planconnect();
        try {
            pc.planconnect(Query);
        } catch (SQLException ex) {
            Logger.getLogger(ablak.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ablak.class.getName()).log(Level.SEVERE, null, ex);
        }

        //betesszuk a tablaba a meglevo adatokat
        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) ablak.jTable14.getModel();
        model.setRowCount(0);

        try {
            while (pc.rs.next()) {

                model.addRow(new Object[]{pc.rs.getString(1), pc.rs.getString(2), pc.rs.getString(3).trim(), pc.rs.getString(4), pc.rs.getString(5)});

            }
        } catch (SQLException ex) {
            Logger.getLogger(ablak.class.getName()).log(Level.SEVERE, null, ex);
        }

        Query = "Select distinct tc_becells.cellname , tc_bepns.partnumber , tc_terv.job ,tc_terv.date , sum(tc_terv.qty) from tc_becells\n"
                + "left join tc_terv on tc_terv.idtc_becells = tc_becells.idtc_cells\n"
                + "left join tc_bepns on tc_bepns.idtc_bepns = tc_terv.idtc_bepns\n"
                + "where tc_terv.date >= '" + stol + "' and tc_terv.active = 2 and tc_becells.cellname in (" + allomasokquerybe + ") group by tc_terv.job , tc_bepns.partnumber order by tc_becells.cellname asc , tc_terv.date asc;";
        try {
            pc.planconnect(Query);
        } catch (SQLException ex) {
            Logger.getLogger(ablak.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ablak.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            while (pc.rs.next()) {

                model.addRow(new Object[]{pc.rs.getString(1), pc.rs.getString(2), pc.rs.getString(3), pc.rs.getString(4), pc.rs.getString(5)});

            }
        } catch (SQLException ex) {
            Logger.getLogger(ablak.class.getName()).log(Level.SEVERE, null, ex);
        }

        pc.kinyir();

        ablak.jTable14.setModel(model);

        //kiszedjuk a tablabol az adatokat es lekerdezzuk
        String jobszamok = "";
        for (int i = 0; i < ablak.jTable14.getRowCount(); i++) {

            jobszamok += ablak.jTable14.getValueAt(i, 2).toString().trim() + ";";

        }

        try {

            jobszamok = jobszamok.substring(0, jobszamok.length() - 1);
        } catch (Exception e) {
        }

        xmlfeldolg xxx = new xmlfeldolg();
        Object rowdata[][] = null;
        URL url = null;
        try {
            url = new URL("http://143.116.140.120/rest/request.php?page=planning_shop_order&shoporder=" + jobszamok + "&format=xml");
            ArrayList<String> lista = new ArrayList();

            String nodelist = "planning_shop_order";
            lista.add("Shop_Order_Number");
            lista.add("Part_Number");
            lista.add("Workstation");
            lista.add("Qty");
            lista.add("Unit_Status");
            rowdata = (Object[][]) xxx.xmlfeldolg(url, nodelist, lista);

        } catch (Exception e) {
        }

        //betesszuk a tablaba
        model = (DefaultTableModel) ablak.jTable14.getModel();

        for (int i = 0; i < ablak.jTable14.getRowCount(); i++) {

            irtunke = false;

            for (int n = 0; n < rowdata.length; n++) {

                if (ablak.jTable14.getValueAt(i, 2).toString().equals(rowdata[n][0]) && (rowdata[n][4].equals("Traveler Printed") || rowdata[n][4].equals("Unit Skeleton"))) {

                    model.setValueAt(rowdata[n][3], i, 5);
                    irtunke = true;

                } else if (ablak.jTable14.getValueAt(i, 2).toString().equals(rowdata[n][0]) && irtunke == false) {

                    model.setValueAt("Minden elindult!", i, 5);

                }

            }

            try {
                if (model.getValueAt(i, 5) == null || model.getValueAt(i, 5).toString().equals("")) {

                    model.setValueAt("Nem létezik SFDC-ben!", i, 5);

                }
            } catch (Exception e) {
            }

        }

        ablak.jTable14.setModel(model);

        animation.rajzol = false;

        stat.beir(System.getProperty("user.name"), jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()), "", "gabor.hanacsek@sanmina.com");

    }

}
