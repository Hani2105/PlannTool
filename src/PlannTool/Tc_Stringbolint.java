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
public class Tc_Stringbolint {

    public static int db = 0;

    public Tc_Stringbolint(String szoveg) {

        boolean inte = true;
        int eddigmenni = 1;

        //kiszedjuk a stringbol az integert
        while (inte) {
            try {
                db = Integer.parseInt(szoveg.substring(0, eddigmenni));
                eddigmenni++;
            } catch (Exception e) {

                inte = false;

            }
        }

        //return db;
    }

}
