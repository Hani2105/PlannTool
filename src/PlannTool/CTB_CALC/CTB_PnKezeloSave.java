/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool.CTB_CALC;

import PlannTool.CONNECTS.planconnect;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gabor_hanacsek
 */
public class CTB_PnKezeloSave extends Thread {

    CTB c;
    CTB_Pnkezelo p;
   

    
    

    public CTB_PnKezeloSave(CTB c , CTB_Pnkezelo p) {

        this.c = c;
        this.p = p;

    }

    public void run() {

        //mentjük a pn-eket
        //megigtekerjuk a tablat es ahol van pipa azt becsapjuk a stringbe
        
        p.setVisible(false);
        CTB_Pnkezelo.jTable1.setRowSorter(null);
        String pnids = "";
        for (int i = 0; i < CTB_Pnkezelo.jTable1.getRowCount(); i++) {
            
            if (CTB_Pnkezelo.jTable1.getValueAt(i, 1).equals(true)) {
                
                pnids += CTB_Pnkezelo.adatok[i][0] + ",";
                
            }
            
        }
        
        pnids = pnids.substring(0, pnids.length() - 1);
        
        String query = "update CTB_UserData set idpns = '" + pnids + "'  where username = '" + CTB.jLabel1.getText() + "'";
        
        planconnect pc = new planconnect();
        pc.feltolt(query, false);
        
        try {
            c.PnToTable();
        } catch (SQLException ex) {
            Logger.getLogger(CTB_Pnkezelo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CTB_Pnkezelo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            c.PnToTable();
        } catch (SQLException ex) {
            Logger.getLogger(CTB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CTB.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            c.UploadsDate();
        } catch (SQLException ex) {
            Logger.getLogger(CTB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CTB.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            c.FillOhTable();
        } catch (SQLException ex) {
            Logger.getLogger(CTB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CTB.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            c.FillAllocTable();
        } catch (SQLException ex) {
            Logger.getLogger(CTB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CTB.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            c.FillDemandTable();
        } catch (SQLException ex) {
            Logger.getLogger(CTB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CTB.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            c.FillWoTable();
        } catch (SQLException ex) {
            Logger.getLogger(CTB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CTB.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            c.FillBomTable();
        } catch (SQLException ex) {
            Logger.getLogger(CTB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CTB.class.getName()).log(Level.SEVERE, null, ex);
        }
        c.OpenPoCalc();
        c.StockCalc();
        c.OpenOrderCalc();
//a pn-jeink bom matrixat rakja össze
        c.BomCalc();
//ez kiszámolja a felhasználható oh mennyiséget  (lasúúúúúúúú)
        c.Ohcalc(0);
//kiteszi a ctb táblába azokat az alkatrészeket amiket használ a termék
        c.CompToCtb();
//kitölti a ctb táblába , hogy hány termékre vagyunk tiszták
        c.CtbKalk();
        c.NeedToBuild();
        c.SaveCtbTableData();
        
        c.TablaOszlopSzelesseg(c.jTable1);
        
        CTB_AnimationThread.fut = false;
        p.dispose();
        
    }

}
