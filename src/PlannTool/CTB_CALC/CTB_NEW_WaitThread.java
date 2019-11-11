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
public class CTB_NEW_WaitThread extends Thread {

    public static boolean latszik = true;

    @Override

    public void run() {
        CTB_NEW_WaitWindow w = new CTB_NEW_WaitWindow();
        w.setVisible(true);

        while (latszik) {
            w.repaint();

        }

        w.dispose();
        latszik = true;

    }

}
