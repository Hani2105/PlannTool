/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool;

import javax.swing.JFrame;

/**
 *
 * @author gabor_hanacsek
 */
public class animation extends Thread {

    public static boolean rajzol = true;

    public void run() {

        if (System.getProperty("user.name").equals("viktor_feher")) {

            animationzsiraf a = new animationzsiraf();
            a.setVisible(true);

            while (rajzol == true) {

                a.repaint();

                //System.out.println("futok");
            }

            a.dispose();
            //System.out.println("megalltam");
            rajzol = true;

        } else {

            animationpicture a = new animationpicture();
            a.setVisible(true);

            while (rajzol == true) {

                a.repaint();

                //System.out.println("futok");
            }

            a.dispose();
            //System.out.println("megalltam");
            rajzol = true;
        }
    }

}
