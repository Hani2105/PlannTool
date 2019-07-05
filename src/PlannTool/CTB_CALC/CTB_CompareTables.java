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
public class CTB_CompareTables {

    JTable t1;
    JTable t2;

    public CTB_CompareTables(JTable t1, JTable t2) {

        this.t1 = t1;
        this.t2 = t2;
    }

    public boolean compare() {
        boolean eq;

//pn -ek a tablakbol
        String[] pnlist1 = new String[t1.getRowCount()];
        String[] pnlist2 = new String[t2.getRowCount()];

        for (int i = 0; i < t1.getRowCount(); i++) {
            try {
                pnlist1[i] = t1.getValueAt(i, 0).toString();
            } catch (Exception e) {
            }

        }

        for (int i = 0; i < t2.getRowCount(); i++) {
            try {
                pnlist2[i] = t2.getValueAt(i, 0).toString();
            } catch (Exception e) {
            }

        }

//akkor hasonlítsunk
        if (Arrays.equals(pnlist1, pnlist2)) {

            eq = true;
        } else {

            eq = false;
        }

//        for (int i = 0; i < pnlist1.length; i++) {
//
//            if (!pnlist1[i].equals(pnlist2[i])) {
//
//                eq = false;
//                break;
//            }
//
//        }
        return eq;
    }

}
