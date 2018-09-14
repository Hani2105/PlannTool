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
public class animation extends Thread{

    public static boolean rajzol = true;

    public void run() {
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
