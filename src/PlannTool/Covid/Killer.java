/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool.Covid;

import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hanac
 */
public class Killer extends Thread {
//kinyírja a betegeket a megadott százalékig random módon

    public void run() {
        while (MainForm.futunkemeg) {
            try {
                Thread.sleep(Integer.parseInt(MainForm.jTextField5.getText()));
            } catch (InterruptedException ex) {
                Logger.getLogger(Killer.class.getName()).log(Level.SEVERE, null, ex);
            }
//            Collections.shuffle(MainForm.Victims);
//kiszámoljuk mennyi a beteg és mennyit kell kinyírni
            double betegek = Double.parseDouble(MainForm.jLabel9.getText());
            double killrata = Double.parseDouble(MainForm.jTextField6.getText());

            int kinyirni = (int) Math.ceil(betegek / 100.00 * killrata);
            synchronized (MainForm.Victims) {
                for (Victim v : MainForm.Victims) {
//akkor nyírjuk ki ha nem halott :p és még beteg is
                    if (!v.isHalott() && v.isBeteg() && kinyirni > 0) {
                        v.setHalott(true);
                        kinyirni--;

                    }

                }
            }

        }
    }

}
