/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool;

import static PlannTool.ablak.jTabbedPane1;
import static PlannTool.ablak.stat;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author gabor_hanacsek
 */
public class Gyartasinfoszal extends Thread {

    public void run() {

        //felvesszuk a query változóját
        String valtozo = "";
        for (int i = 0; i < ablak.jTable15.getRowCount(); i++) {

            //ha van írva valami a pn oszlopba
            if (ablak.jTable15.getValueAt(i, 0) != null && !ablak.jTable15.getValueAt(i, 0).toString().equals("")) {

                valtozo += " terv.partnumber like '%" + ablak.jTable15.getValueAt(i, 0).toString() + "%' or";

            }

        }

        //levagjuk az utolso or-t
        valtozo = valtozo.substring(0, valtozo.length() - 2);

        //a query maga
        String query = "select distinct terv.partnumber as pn , stations.name as sor , min(terv.startdate) as elso , max(terv.startdate) as utolso ,  count(terv.job) as hanyszor , datediff (now() , max(terv.startdate)) as hanynapja\n"
                + "from terv\n"
                + "left join stations on stations.id = terv.stationid\n"
                + "where (" + valtozo
                + ") and terv.active = 1\n"
                + "group by sor , pn\n"
                + "order by sor";

        //peldanositunk egy plan connectet
        planconnect pc = new planconnect();
        try {
            pc.planconnect(query);
        } catch (SQLException ex) {
            Logger.getLogger(ablak.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ablak.class.getName()).log(Level.SEVERE, null, ex);
        }
        //tabla lepkedeshez kell
        int i = 0;
        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) ablak.jTable15.getModel();

        //kitoroljuk a modell adatait
        for (int o = 0; o < ablak.jTable15.getColumnCount(); o++) {

            for (int r = 0; r < ablak.jTable15.getRowCount(); r++) {

                model.setValueAt("", r, o);

            }

        }

        try {
            //betesszuk az eredmenyt tablaba
            while (pc.rs.next()) {

                model.setValueAt(pc.rs.getString(1), i, 0);
                model.setValueAt(pc.rs.getString(2), i, 1);
                model.setValueAt(pc.rs.getString(3), i, 2);
                model.setValueAt(pc.rs.getString(4), i, 3);
                model.setValueAt(pc.rs.getString(5), i, 4);
                model.setValueAt(pc.rs.getString(6), i, 5);
                i++;

                if (i > 30) {

                    model.addRow(new Object[6]);

                }

            }

        } catch (Exception ex) {

        }

        stat.beir(System.getProperty("user.name"), jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()), "", "gabor.hanacsek@sanmina.com");
        animation.rajzol = false;
        
        pc.kinyir();

    }

}
