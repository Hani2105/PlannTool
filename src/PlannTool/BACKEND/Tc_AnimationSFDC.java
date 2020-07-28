/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool.BACKEND;

import PlannTool.ANIMATIONS.sfdc_animation_picture;
import PlannTool.ANIMATIONS.pony;
import PlannTool.ablak;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gabor_hanacsek
 */
public class Tc_AnimationSFDC extends Thread {

    public static boolean rajzole = true;

    public void run() {

        if (ablak.user.equals("alexandra_havelda") || ablak.user.equals("miklos_bosnyakovics") || ablak.user.equals("gabor_hanacsek")) {

            pony a = new pony();
            a.setVisible(true);

            while (rajzole == true) {

                a.repaint();
               
            }

            a.dispose();

        } else {

            sfdc_animation_picture a = new sfdc_animation_picture();
            a.setVisible(true);

            while (rajzole == true) {

                a.repaint();
                try {
                    Thread.sleep(100);

                    //System.out.println("futok");
                } catch (InterruptedException ex) {
                    Logger.getLogger(Tc_AnimationSFDC.class.getName()).log(Level.SEVERE, null, ex);
                }

                //System.out.println("futok");
            }

            a.dispose();

        }

//        while (rajzole == true) {
//
//            a.repaint();
//
//            //System.out.println("futok");
//        }
//
//        a.dispose();
        //System.out.println("megalltam");
        rajzole = true;

    }

}
