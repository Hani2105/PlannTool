/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool.CTB_CALC;

import PlannTool.CONNECTS.*;
import java.sql.SQLException;

/**
 *
 * @author gabor_hanacsek
 */
public class CTB_PnDatas {

    public void adatleker() throws SQLException, ClassNotFoundException {

//üresre állítjuk az adat tárolót
        CTB.PnDatas.clear();

        String query = "select * from CTB_PN_Datas";
        planconnect pc = new planconnect();

        pc.lekerdez(query);

        while (pc.rs.next()) {

            String[] adatok = new String[4];

            adatok[0] = pc.rs.getString(1);
            adatok[1] = pc.rs.getString(2);
            adatok[2] = pc.rs.getString(3);
            adatok[3] = pc.rs.getString(4);

            CTB.PnDatas.add(adatok);

        }

        pc.kinyir();
    }

}
