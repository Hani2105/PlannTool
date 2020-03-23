/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool.Covid;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.image.ImageObserver;
import java.text.AttributedCharacterIterator;
import java.util.Random;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author hanac
 */
public class Victim extends Thread {

    int x;
    int y;
    Random r = new Random();
    MainForm m;
    private boolean beteg = false;
    private boolean mozog = false;
    public boolean gyogyult = false;
    public boolean halott = false;
    int lepesszunet;
    int gyogyulido;
    String nevem = "";

    public Timer timer;

    //gyógyulás timertask
    public Victim() {

        this.m = m;
        startlocation();
        lepesszunet = Integer.parseInt(MainForm.jTextField4.getText());
        gyogyulido = Integer.parseInt(MainForm.jTextField5.getText());

    }

    @Override
    public void run() {

        if (this.isBeteg()) {
            timer = new Timer();
            timer.schedule(new Task(this), gyogyulido);
        }

        while (MainForm.futunkemeg) {

            if (isMozog() && !isHalott()) {
                reLocal();
            }

            szomszedkeres();

            try {
                Thread.sleep(lepesszunet);
                m.jPanel1.repaint();
            } catch (InterruptedException ex) {
                Logger.getLogger(Victim.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    public boolean isHalott() {
        return halott;
    }

    public void setHalott(boolean halott) {
        this.halott = halott;
    }

    public void setBeteg(boolean beteg) {
        this.beteg = beteg;
    }

    public boolean isBeteg() {
        return beteg;
    }

    public boolean isMozog() {
        return mozog;
    }

    public void setMozog(boolean mozog) {
        this.mozog = mozog;
    }

    public boolean isGyogyult() {
        return gyogyult;
    }

    public void setGyogyult(boolean gyogyult) {
        this.gyogyult = gyogyult;
    }

    public String getNevem() {
        return nevem;
    }

    public void setNevem(String nevem) {
        this.nevem = nevem;
    }

    public void startlocation() {

        x = r.nextInt(m.jPanel1.getWidth() - 25);
        y = r.nextInt(m.jPanel1.getHeight() - 25);

    }

    public void reLocal() {

        int tempx = -50 + r.nextInt(100);
        if (tempx + x > m.jPanel1.getWidth() - 25) {

            tempx = tempx * -1;
        }

        if (tempx + x < 0) {

            tempx = tempx * -1;
        }

        int tempy = -50 + r.nextInt(100);
        if (tempy + y > m.jPanel1.getHeight() - 25) {

            tempy = tempy * -1;
        }

        if (tempy + y < 0) {

            tempy = tempy * -1;
        }

        x = x + tempx;
        y = y + tempy;

    }

    public void szomszedkeres() {
        synchronized (MainForm.Victims) {
            for (Victim v : MainForm.Victims) {

                if (this.x - v.x > -25 && this.x - v.x < 25 && !v.equals(this)) {

                    if (this.y - v.y > -25 && this.y - v.y < 25) {
                        if (v.beteg && !this.gyogyult && !this.isBeteg() && !v.isHalott()) {
                            this.setBeteg(true);
                            //elindítjuk a számlálót
                            try {
                                timer.cancel();
                            } catch (Exception e) {
                            }
                            timer = new Timer();
                            timer.schedule(new Task(this), gyogyulido, 100);

                        }
                    }
                }

            }
        }
    }

}
