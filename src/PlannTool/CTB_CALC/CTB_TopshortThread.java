/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool.CTB_CALC;

import static PlannTool.CTB_CALC.CTB.jTable1;
import static PlannTool.CTB_CALC.CTB.jTable2;
import static PlannTool.CTB_CALC.CTB.jTable8;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author gabor_hanacsek
 */
public class CTB_TopshortThread extends Thread {

    public void run() {

        //betesszük a használatos anyagokat atáblába
        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) jTable1.getModel();
        DefaultTableModel shortmodel = new DefaultTableModel();
        shortmodel = (DefaultTableModel) CTB.jTable9.getModel();
        shortmodel.setRowCount(0);
        DefaultTableModel rawohmodel = new DefaultTableModel();
        rawohmodel = (DefaultTableModel) jTable8.getModel();
        DefaultTableModel ohmodel = new DefaultTableModel();
        ohmodel = (DefaultTableModel) jTable2.getModel();
        String[][] tmppns = new String[model.getColumnCount() - 7][2];
        DefaultTableModel indentedmodel = new DefaultTableModel();
        indentedmodel = (DefaultTableModel) CTB.jTable6.getModel();

//betesszük az adatokat a tömbbe , első a pn , aztán a darab
        for (int i = 7; i < model.getColumnCount(); i++) {

            tmppns[i - 7][0] = model.getColumnName(i);

            try {
                tmppns[i - 7][1] = model.getValueAt(jTable1.getSelectedRow(), i).toString();
                if (tmppns[i - 7][1].toString().equals("")) {

                    tmppns[i - 7][1] = "99999999";

                }

            } catch (Exception e) {

                tmppns[i - 7][1] = "99999999";

            }

        }

//most kéne buborékrendezni..
        int i;
        int j;
        String darabszam;
        String partnumber;
        for (i = tmppns.length - 1; 0 < i; --i) {
            for (j = 0; j < i; ++j) {
                if (Integer.parseInt(tmppns[j][1].toString()) > Integer.parseInt(tmppns[j + 1][1].toString())) {
                    // csere
                    partnumber = tmppns[j][0].toString();
                    darabszam = tmppns[j][1].toString();

                    tmppns[j][0] = tmppns[j + 1][0];
                    tmppns[j][1] = tmppns[j + 1][1];

                    tmppns[j + 1][0] = partnumber;
                    tmppns[j + 1][1] = darabszam;

                }
            }
        }

        //betesszük a shortmodelbe
        for (i = 0; i < tmppns.length; i++) {

            if (!tmppns[i][1].toString().equals("99999999")) {
                shortmodel.addRow(new Object[]{tmppns[i][0], tmppns[i][1]});
            }

        }

        CTB.jTable9.setModel(shortmodel);

//megkereseeük a pn ek leírását és beállítjuk a táblába
        for (i = 0; i < shortmodel.getRowCount(); i++) {
            String pn = shortmodel.getValueAt(i, 0).toString();
//vegigtekerjuk az oh táblát is sajna és kiszedjük a kommenteket a pn -ekhez  
            String komment = "";

            for (int n = 0; n < ohmodel.getRowCount(); n++) {

                if (pn.equals(ohmodel.getValueAt(n, 2).toString())) {
                    try {
                        komment += ohmodel.getValueAt(n, 10).toString() + ",";
                    } catch (Exception e) {
                    }

                }

            }

            for (int k = 0; k < rawohmodel.getRowCount(); k++) {
                try {
                    if (shortmodel.getValueAt(i, 0).toString().equals(rawohmodel.getValueAt(k, 0).toString())) {

                        //shortmodel.setValueAt(rawohmodel.getValueAt(k, 5), i, 2);
                        shortmodel.setValueAt(rawohmodel.getValueAt(k, 1), i, 3);
                        shortmodel.setValueAt(komment, i, 4);

                    }
                } catch (Exception e) {
                }
            }

//még sajnálatosabb , hogy be kell járnunk az intended táblát is az opseq-ért , a supply-ért és a desc -ért
            for (int k = 0; k < indentedmodel.getRowCount(); k++) {
                try {
                    if (pn.equals(indentedmodel.getValueAt(k, 7).toString()) && model.getValueAt(jTable1.getSelectedRow(), 0).toString().equals(indentedmodel.getValueAt(k, 0))) {
//opseq
                        shortmodel.setValueAt(indentedmodel.getValueAt(k, 6).toString(), i, 5);
//supply
                        shortmodel.setValueAt(indentedmodel.getValueAt(k, 11).toString(), i, 6);
//desc
                        shortmodel.setValueAt(indentedmodel.getValueAt(k, 9).toString(), i, 2);
                        break;

                    }

                } catch (Exception e) {
                }
            }

        }

        CTB.jTable9.setModel(shortmodel);
        CTB.TablaOszlopSzelesseg(CTB.jTable9);
        CTB.jTable9.repaint();
    }

}
