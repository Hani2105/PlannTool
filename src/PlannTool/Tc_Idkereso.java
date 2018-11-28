/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool;

import static PlannTool.Tc_Betervezo.Besheets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author gabor_hanacsek
 */
public class Tc_Idkereso {

    //cellak
    private List<String[][]> gyujto = new ArrayList<String[][]>();

    public void adatleker() {
        String query = "select * from tc_becells";
        planconnect pc = new planconnect();

        try {
            int i = 0;
            pc.planconnect(query);
            pc.rs.last();
            int last = pc.rs.getRow();
            pc.rs.beforeFirst();
            String[][] cellist = new String[last][2];

            while (pc.rs.next()) {

                cellist[i][0] = pc.rs.getString(1);
                cellist[i][1] = pc.rs.getString(2);

                i++;

            }

            gyujto.add(cellist);

        } catch (SQLException ex) {
            Logger.getLogger(Tc_Betervezo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Tc_Betervezo.class.getName()).log(Level.SEVERE, null, ex);
        }

        //wsek
        query = "select * from tc_bestations";

        try {
            int i = 0;
            pc.planconnect(query);
            pc.rs.last();
            int last = pc.rs.getRow();
            pc.rs.beforeFirst();
            String[][] wslist = new String[last][2];

            while (pc.rs.next()) {

                wslist[i][0] = pc.rs.getString(1);
                wslist[i][1] = pc.rs.getString(2);

                i++;

            }

            gyujto.add(wslist);

        } catch (SQLException ex) {
            Logger.getLogger(Tc_Betervezo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Tc_Betervezo.class.getName()).log(Level.SEVERE, null, ex);
        }

        //pnek
        query = "select * from tc_bepns";

        try {
            int i = 0;
            pc.planconnect(query);
            pc.rs.last();
            int last = pc.rs.getRow();
            pc.rs.beforeFirst();
            String[][] pnlist = new String[last][2];

            while (pc.rs.next()) {

                pnlist[i][0] = pc.rs.getString(1);
                pnlist[i][1] = pc.rs.getString(2);

                i++;

            }
            
            pc.kinyir();

            gyujto.add(pnlist);

        } catch (SQLException ex) {
            Logger.getLogger(Tc_Betervezo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Tc_Betervezo.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
// itt keressuk meg a cellahoz tartozó id-t

    public String cellakereso(String neve) {

        String cellaid = null;

        for (int k = 0; k < gyujto.get(0).length; k++) {

            if (neve.equals(gyujto.get(0)[k][1])) {

                cellaid = gyujto.get(0)[k][0];
                break;

            }

        }

        return cellaid;

    }

    // itt keressuk meg az allomasokhoz tartozo id-kat
    public String allomaskereso(JTable j, int sor, int oszlop) {

        String wsid = null;

        for (int k = 0; k < gyujto.get(1).length; k++) {

            if (j.getValueAt(sor, oszlop).toString().equals(gyujto.get(1)[k][1])) {

                wsid = gyujto.get(1)[k][0];
                break;

            }

        }

        return wsid;

    }

    // itt keressuk meg az allomasokhoz tartozo id-kat
    public String partnumberkereso(JTable j, int sor, int oszlop) {

        String pnid = null;

        for (int k = 0; k < gyujto.get(2).length; k++) {

            if (j.getValueAt(sor, oszlop).toString().equals(gyujto.get(2)[k][1])) {

                pnid = gyujto.get(2)[k][0];
                break;

            }

        }

        return pnid;

    }

}
