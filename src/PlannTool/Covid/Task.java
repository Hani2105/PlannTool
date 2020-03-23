/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool.Covid;

import java.util.TimerTask;

/**
 *
 * @author hanac
 */
public class Task extends TimerTask {

    Victim v;

    public Task(Victim v) {

        this.v = v;
    }

    @Override
    public void run() {
        //ha nem tudunk visszafertőződni
        if (!MainForm.jCheckBox1.isSelected()) {
            
            v.setBeteg(false);
            v.setGyogyult(true);

//ha vissza tudunk fertőződni
        } else {
            v.setBeteg(false);
            v.setGyogyult(false);
        }

    }
}
