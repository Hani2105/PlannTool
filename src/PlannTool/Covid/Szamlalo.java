/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool.Covid;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

/**
 *
 * @author hanac
 */
public class Szamlalo extends Thread {

    int halott = 0;
    int beteg = 0;
    int egeszseges = 0;

    public void run() {
        while (MainForm.futunkemeg) {
            try {
                Thread.sleep(Integer.parseInt(MainForm.jTextField4.getText()));
            } catch (InterruptedException ex) {
                Logger.getLogger(Szamlalo.class.getName()).log(Level.SEVERE, null, ex);
            }

            synchronized (MainForm.Victims) {
                halott = 0;
                beteg = 0;
                egeszseges = 0;
                for (Victim v : MainForm.Victims) {

                    if (v.isHalott()) {
                        halott++;
                    } else if (v.isBeteg() && !v.isHalott()) {
                        beteg++;
                    } else {
                        egeszseges++;

                    }

                }

                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        MainForm.jLabel12.setText(String.valueOf(halott));
                        MainForm.jLabel8.setText(String.valueOf(egeszseges));
                        MainForm.jLabel9.setText(String.valueOf(beteg));
                    }
                });

//                System.out.println(halott);
            }
            
            MainForm.jPanel2.repaint();
        }
    }

}
