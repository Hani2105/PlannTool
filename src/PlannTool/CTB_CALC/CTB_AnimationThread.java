/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool.CTB_CALC;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gabor_hanacsek
 */
public class CTB_AnimationThread extends Thread {

    public static boolean fut = true;

    public void run() {

        CTB_Animation a = new CTB_Animation();
        a.setVisible(true);
        while (fut) {

            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(CTB_AnimationThread.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        a.dispose();

    }

}
