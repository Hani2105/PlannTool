/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool.BACKEND;

import PlannTool.CONNECTS.planconnect;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Tc_Tervvaltozasellenor {

    public static List<String[][]> tervellenor = new ArrayList<String[][]>();

    public void leker() throws SQLException, ClassNotFoundException {

        //lekerdezzuk a tervet es minden cellahoz elrakjuk az utolso terv id-t
        String query = "select max(tc_terv.timestamp) as id , tc_becells.cellname  \n"
                + "from tc_terv \n"
                + "left join tc_becells on tc_becells.idtc_cells = tc_terv.idtc_becells \n"
                + "where (select distinct  tc_becells.idtc_cells) and tc_terv.tt = 3 group by cellname ";

        planconnect pc = new planconnect();
        pc.lekerdez(query);
        pc.rs.last();
        int sorokszama = pc.rs.getRow();
        pc.rs.beforeFirst();
        String[][] adatok = new String[2][sorokszama];
        int n = 0;
        while (pc.rs.next()) {

            adatok[0][n] = pc.rs.getString(1);
            adatok[1][n] = pc.rs.getString(2);
            n++;

        }
        
        pc.kinyir();

        tervellenor.add(adatok);

    }

}
