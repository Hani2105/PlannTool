/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool.CTB_CALC;

import java.util.Arrays;
import javax.swing.JTable;

/**
 *
 * @author gabor_hanacsek
 */
public class CTB_NEW_CompareTables {

    JTable t1;
    JTable t2;

    public CTB_NEW_CompareTables(JTable t1, JTable t2) {

        this.t1 = t1;
        this.t2 = t2;
    }

    public boolean compare() {
        boolean eq = false;

//pn -ek a tablakbol
        String[] pnlist1 = new String[t1.getRowCount()];
        String[] pnlist2 = new String[t2.getRowCount()];

        for (int i = 0; i < t1.getRowCount(); i++) {
            try {
                pnlist1[i] = t1.getValueAt(i, 0).toString().trim().toLowerCase();
            } catch (Exception e) {
            }

        }
        for (int i = 0; i < t2.getRowCount(); i++) {

            try {
                pnlist2[i] = t2.getValueAt(i, 0).toString().trim().toLowerCase();
            } catch (Exception e) {
            }

        }

//akkor hasonlítsunk
        eq = Arrays.equals(pnlist2, pnlist1);
        //eq = pnlist1.toString().equals(pnlist2.toString());

        return eq;

    }

}
