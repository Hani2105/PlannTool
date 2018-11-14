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

    public  int db = 0;
    public  String komment = "";

    public Tc_Stringbolint(String szoveg) {

        boolean inte = true;
        int eddigmenni = 1;
        boolean szovege = true;
        int innentol = 1;
        db = 0;
        komment = "";

        //kiszedjuk a stringbol az integert
        while (inte) {
            try {
                db = Integer.parseInt(szoveg.substring(0, eddigmenni));
                eddigmenni++;
            } catch (Exception e) {

                inte = false;

            }
        }

        try {
            komment = szoveg.substring(eddigmenni, szoveg.length());
        } catch (Exception e) {
            komment = "";
        }

    }

}
