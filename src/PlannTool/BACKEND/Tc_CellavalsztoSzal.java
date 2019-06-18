/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool.BACKEND;

import PlannTool.ANIMATIONS.animation;
import static PlannTool.BACKEND.Tc_Cellavalaszto.neve;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gabor_hanacsek
 */
public class Tc_CellavalsztoSzal extends Thread {

    Tc_Cellavalaszto c;

    public Tc_CellavalsztoSzal(Tc_Cellavalaszto c) {

        this.c = c;

    }

    public void run() {

        c.dispose();
        //kiuritjuk a cellak konténerét
        Tc_Betervezo.Besheets.clear();
        //lekérjük a sheeteket
        for (int i = 0; i < Tc_Cellavalaszto.jList2.getModel().getSize(); i++) {

            //megnezzuk hogy van e már ilyen tab
            boolean vanemar = false;
            for (int t = 0; t < Tc_Betervezo.Tervezotabbed.getTabCount(); t++) {

                if (Tc_Betervezo.Tervezotabbed.getTitleAt(t).equals(Tc_Cellavalaszto.jList2.getModel().getElementAt(i))) {

                    vanemar = true;

                }

            }

            if (vanemar == false) {

                Tc_Besheet sheet = null;
                try {
                    sheet = new Tc_Besheet(c.bt);
                } catch (SQLException ex) {
                    Logger.getLogger(Tc_Cellavalaszto.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Tc_Cellavalaszto.class.getName()).log(Level.SEVERE, null, ex);
                }
                Tc_Betervezo.Tervezotabbed.addTab(Tc_Cellavalaszto.jList2.getModel().getElementAt(i), sheet);
                neve = Tc_Cellavalaszto.jList2.getModel().getElementAt(i);
                Tc_Betervezo.Besheets.put(Tc_Cellavalaszto.jList2.getModel().getElementAt(i), sheet);
                try {
                    sheet.parts();
                    sheet.workstations();
                } catch (SQLException ex) {
                    Logger.getLogger(Tc_Cellavalaszto.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Tc_Cellavalaszto.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
        
        
        animation.rajzol = false;
//megnyitunk egy időpont bekérést

      Tc_Idointervallum i = new Tc_Idointervallum();
      i.setVisible(true);
        

    }

}
