/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool.ANIMATIONS;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 *
 * @author gabor_hanacsek
 */
public class animation extends Thread {

    public static boolean rajzol = true;

    public void run() {

        animationpicture a = new animationpicture();
        //animationpicture a = new animationpicture();
        a.setVisible(true);

        while (rajzol) {

            a.repaint();
            try {
                Thread.sleep(1000);
                
                //System.out.println("futok");
            } catch (InterruptedException ex) {
                Logger.getLogger(animation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        a.dispose();
        //System.out.println("megalltam");
        rajzol = true;
    }

}
