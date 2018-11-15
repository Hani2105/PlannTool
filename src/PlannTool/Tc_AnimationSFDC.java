/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool;

/**
 *
 * @author gabor_hanacsek
 */
public class Tc_AnimationSFDC extends Thread {

    public static boolean rajzole = true;

    public void run() {

        if (System.getProperty("user.name").equals("alexandra_havelda")) {

            Tc_SFDCpony a = new Tc_SFDCpony();
            a.setVisible(true);

            while (rajzole == true) {

                a.repaint();

                //System.out.println("futok");
            }

            a.dispose();

        } else {

            Tc_SFDCanimationpicture a = new Tc_SFDCanimationpicture();
            a.setVisible(true);

            while (rajzole == true) {

                a.repaint();

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
