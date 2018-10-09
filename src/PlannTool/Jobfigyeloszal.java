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
public class Jobfigyeloszal extends Thread{
    
    public void run(){
    
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
        String Query = "select distinct stations.name , terv.partnumber , terv.job , terv.startdate from stations left join terv on terv.stationid = stations.id where terv.active = 1 and terv.startdate >= '" + stol + "' and stations.name in (" + allomasokquerybe + ") group by terv.job order by stations.name asc , terv.startdate asc";
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

                model.addRow(new Object[]{pc.rs.getString(1), pc.rs.getString(2), pc.rs.getString(3).trim(), pc.rs.getString(4)});

            }
        } catch (SQLException ex) {
            Logger.getLogger(ablak.class.getName()).log(Level.SEVERE, null, ex);
        }

        //lekerdezzuk a backendet is
        Query = "SELECT distinct Beterv.sht , Beterv.pn , Beterv.job , Beterv.startdate from Beterv where Beterv.startdate >= '" + stol + "' and Beterv.active = 2 and Beterv.sht in (" + allomasokquerybe + ") group by Beterv.job order by Beterv.sht asc , Beterv.startdate asc";

        try {
            pc.planconnect(Query);
        } catch (SQLException ex) {
            Logger.getLogger(ablak.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ablak.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            while (pc.rs.next()) {

                model.addRow(new Object[]{pc.rs.getString(1), pc.rs.getString(2), pc.rs.getString(3), pc.rs.getString(4)});

            }
        } catch (SQLException ex) {
            Logger.getLogger(ablak.class.getName()).log(Level.SEVERE, null, ex);
        }

        ablak.jTable14.setModel(model);

        //kiszedjuk a tablabol az adatokat es lekerdezzuk
        String jobszamok = "";
        for (int i = 0; i < ablak.jTable14.getRowCount(); i++) {

            jobszamok += ablak.jTable14.getValueAt(i, 2).toString().trim() + ";";

        }

        jobszamok = jobszamok.substring(0, jobszamok.length() - 1);

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

                    model.setValueAt(rowdata[n][3], i, 4);
                    irtunke = true;

                } else if (ablak.jTable14.getValueAt(i, 2).toString().equals(rowdata[n][0]) && irtunke == false) {

                    model.setValueAt("Minden elindult!", i, 4);

                }

            }

            if (model.getValueAt(i, 4) == null) {

                model.setValueAt("Nem létezik SFDC-ben!", i, 4);

            }

        }

        ablak.jTable14.setModel(model);
        
        animation.rajzol = false;

        stat.beir(System.getProperty("user.name"), jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()), "", "gabor.hanacsek@sanmina.com");
    
    
    }
    
}