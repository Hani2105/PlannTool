/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool.Covid;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 *
 * @author hanac
 */
public class fopanel extends JPanel {
ImageIcon icon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("covid.png")));
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(icon.getImage(), this.getWidth()/2-icon.getImage().getWidth(this)/2, this.getHeight()/2-icon.getImage().getHeight(this)/2,this );

        for (Victim v : MainForm.Victims) {
            if (!v.isBeteg() && !v.isHalott()) {
                g.setColor(Color.GREEN);
            } else if (v.isBeteg() && !v.isHalott()) {
                g.setColor(Color.RED);
            } else if (v.isHalott()) {

                g.setColor(Color.BLACK);
            }

            g.fillOval(v.x, v.y, 25, 25);
            if (!v.getNevem().equals("")) {
                g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
                g.drawString(v.getNevem(), v.x + 25, v.y);
            }

        }

    }

}
