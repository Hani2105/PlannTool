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
public class Tc_AnimationSFDC extends Thread{

    public static boolean rajzol = true;

    public void run() {

        Tc_SFDCanimationpicture a = new Tc_SFDCanimationpicture();
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
