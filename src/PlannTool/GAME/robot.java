/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool.GAME;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gabor_hanacsek
 */
public class robot extends Thread {

    int x;
    int y;
    int z;
    int q;
    Color randomcolor;
    String name;
    

    public List<int[]> robotkord = new ArrayList<>();
    jatek j;

    robot(jatek j, int szama) {
        this.j = j;
        Random rand = new Random();
        float r = rand.nextFloat();
        float g = rand.nextFloat();
        float b = rand.nextFloat();
        randomcolor = new Color(r, g, b);
       

    }

    public void run() {

        boolean mehet = true;
        int magassag = (int) j.jPanel1.getSize().getHeight();
        int szelesseg = (int) j.jPanel1.getSize().getWidth();

        x = szelesseg / 2;
        y = magassag / 2;

        while (mehet) {
            Random r = new Random();

            int[] v = new int[4];
            z = x;
            q = y;

            v[2] = z;
            v[3] = q;
            x += -50 + r.nextInt(100);
            y += -50 + r.nextInt(100);
            v[0] = x;
            v[1] = y;
            robotkord.add(v);

            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(robot.class.getName()).log(Level.SEVERE, null, ex);
            }

            j.repaint();

            if ((x > szelesseg || y > magassag) || (x < 0 || y < 0)) {

                mehet = false;
                j.jLabel2.setText(this.name + " kinyúvadt!!");
            }

        }

    }

}
