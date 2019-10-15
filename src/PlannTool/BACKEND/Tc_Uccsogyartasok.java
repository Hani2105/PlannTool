/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool.BACKEND;

import PlannTool.CONNECTS.*;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gabor_hanacsek
 */
public class Tc_Uccsogyartasok extends Thread {

    Tc_Besheet b;

    public Tc_Uccsogyartasok(Tc_Besheet b) {
        this.b = b;
    }

    @Override

    public void run() {

//összeszedjük a JOB -okat egy Stringbe
        String PNS = "";
//a dátum változója
        String datum = "";

        for (int i = 0; i < b.jTable2.getRowCount(); i++) {
//megnezzuk , hogy van e mar ilyen pn
            try {
                if (!PNS.contains(b.jTable2.getValueAt(i, 0).toString())) {
//ha nincs akkor hozzafüzzük
                    PNS += "'" + b.jTable2.getValueAt(i, 0).toString().trim() + "',";

                }
            } catch (Exception e) {
            }

        }
//levágjuk az utolsó karaktert
        PNS = PNS.substring(0, PNS.length() - 1);
//beállítjuk a dátum változót is , ez az első nap lesz a sheeten
        try {
            datum = b.jTable2.getColumnName(4).substring(0, 10);
        } catch (Exception e) {

        }

//ha van pn és datum is csak akkor megyünk tovább
        if (PNS.length() == 0 || datum.length() == 0) {
            return;
        }

//létrehozzuk a query stringet
        String query = "select max(tc_terv.date) as max , datediff( '" + datum + "' , max(tc_terv.date)) as diff  ,tc_bepns.partnumber from tc_terv \n"
                + "left join tc_bepns on tc_bepns.idtc_bepns = tc_terv.idtc_bepns\n"
                + "where tc_terv.date < '" + datum + "'\n"
                + "and tc_bepns.partnumber in (" + PNS + ")\n"
                + "and tc_terv.qty_teny > 0\n"
                + "and tc_terv.active = 2\n"
                + "group by tc_bepns.partnumber";
//példányosítunk egy planconnectet
        planconnect pc = new planconnect();

        try {
            pc.lekerdez(query);
//kirakjuk egy tömbbe a rs-t
            pc.rs.last();
            String[][] adatok = new String[pc.rs.getRow()][3];
            pc.rs.beforeFirst();
            int i = 0;
            while (pc.rs.next()) {
                adatok[i][0] = pc.rs.getString(1);
                adatok[i][1] = pc.rs.getString(2);
                adatok[i][2] = pc.rs.getString(3);
                i++;

            }

//ha van adat továbbmegyünk , kitesszük egy globális változóba
            b.utsogyartasok = adatok;

        } catch (SQLException ex) {
            Logger.getLogger(Tc_Uccsogyartasok.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Tc_Uccsogyartasok.class.getName()).log(Level.SEVERE, null, ex);
        }

        pc.kinyir();

    }

}
