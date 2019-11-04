/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool.CTB_CALC;

import static PlannTool.CTB_CALC.CTB.jTable1;
import java.awt.ScrollPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 *
 * @author gabor_hanacsek
 */
public class CTB_NEW_LinkTables {

    public CTB_NEW_LinkTables(JScrollPane a, JTable c, JScrollPane b, JTable d, int syncfrom) {

        if (true) {

            //összekapcsoljuk a táblákat
            //b.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
            b.getVerticalScrollBar().setModel(a.getVerticalScrollBar().getModel());

            if (syncfrom == 1) {
                int sor = c.getSelectedRow();

                //beállítjuk a jtable 11 be is a pn re!
                d.changeSelection(sor, 0, false, false);
            } else if (syncfrom == 2) {

                int sor11 = d.getSelectedRow();
                c.changeSelection(sor11, 6, false, false);
                
            }

        } else {

            b.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            b.getVerticalScrollBar().setModel(CTB.buzimodel);

        }

    }

}
