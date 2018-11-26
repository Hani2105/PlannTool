/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool;

import static PlannTool.ablak.rev;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gabor_hanacsek
 */
public class Revconrolszal extends Thread {

    ablak a;
    int res = 0;

    public Revconrolszal(ablak a) {

        this.a = a;

    }

    public void run() {

//beallitjuk az ablaknak a jelenlegi reviziot
        planconnect pc = new planconnect();
        String query = "SELECT revizio , restart FROM planningdb.tc_rev order by tc_rev.idtc_rev desc limit 1";

        try {
            pc.planconnect(query);

            while (pc.rs.next()) {

                a.setTitle(pc.rs.getString(1));

            }

        } catch (SQLException ex) {
            Logger.getLogger(Revconrolszal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Revconrolszal.class.getName()).log(Level.SEVERE, null, ex);
        }

        //revizio beallitas , lekerdezes
        while (1 == 1) {
            query = "SELECT revizio , restart FROM planningdb.tc_rev order by tc_rev.idtc_rev desc limit 1";

            pc = new planconnect();
            try {
                pc.planconnect(query);

                while (pc.rs.next()) {

                    rev = pc.rs.getString(1);
                    res = pc.rs.getInt(2);

                }

            } catch (Exception e) {
            }

//osszehasonlitjuk a frame nevet a revizioval
            if (!a.getTitle().equals(rev) && res == 1) {

                infobox info = new infobox();
                info.infoBox("A programot újra kell indítani!", "Figyelem!");
                System.exit(0);
            }

            try {
                Thread.sleep(1 * 60 * 1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Revconrolszal.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

}
