/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool;

import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author gabor_hanacsek
 */
public class Tc_Calcablakszal extends Thread {

    Tc_Besheet b;
    Tc_Calcablak c;

    public Tc_Calcablakszal(Tc_Besheet b, Tc_Calcablak c) {

        this.b = b;
        this.c = c;

    }

    public void run() {

//először kitörlünk mindet a táblából
        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) this.c.jTable1.getModel();
        model.setRowCount(0);

//ha a teljes időintervallumra vagyunk kiváncsiak
        if (this.c.jRadioButton2.isSelected()) {

//elkezdjük bejárni a táblát
            for (int r = 0; r < this.b.jTable2.getRowCount(); r++) {

//megnezzük , hogy foglalkoztunk e már vele (pn,ws,terv vagy tény)
                boolean irtunke = false;
                Integer melyiksor = 0;

                for (int i = 0; i < model.getRowCount(); i++) {
                    try {
                        if (model.getValueAt(i, 0).equals(this.b.jTable2.getValueAt(r, 0)) && model.getValueAt(i, 1).equals(this.b.jTable2.getValueAt(r, 2)) && model.getValueAt(i, 2).equals(this.b.jTable2.getValueAt(r, 3))) {

                            irtunke = true;
                            melyiksor = i;
                        }
                    } catch (Exception e) {
                    }

                }

//ha nem írtunk létre kell hozni a modellben egy új sort és nem info sorban vagyunk
                if (!irtunke && !this.b.jTable2.getValueAt(r, 3).equals("Infó")) {

                    model.addRow(new Object[]{this.b.jTable2.getValueAt(r, 0), this.b.jTable2.getValueAt(r, 2), this.b.jTable2.getValueAt(r, 3), null});

//vegigrohanunk az oszlopokon es összeadjuk az összeget majd beírjuk az utolsó helyre
                    int osszeg = 0;
                    for (int o = 4; o < this.b.jTable2.getColumnCount() - 2; o++) {

                        osszeg += new Tc_Stringbolint(this.b.jTable2.getValueAt(r, o).toString()).db;

                    }

                    model.setValueAt(osszeg, model.getRowCount() - 1, 3);

                }

//ha írtunk akkor össze kell adni a táblában szereplő összeget 
                if (irtunke) {

                    int osszeg = 0;
                    for (int o = 4; o < this.b.jTable2.getColumnCount() - 1; o++) {

                        osszeg += new Tc_Stringbolint(this.b.jTable2.getValueAt(r, o).toString()).db;

                    }

                    model.setValueAt(Integer.parseInt(model.getValueAt(melyiksor, 3).toString()) + osszeg, melyiksor, 3);

                }

            }

            this.c.jTable1.setModel(model);

        }

//ha időpontig vagyunk kiváncsiak
        if (this.c.jRadioButton1.isSelected()) {

//elkezdjük bejárni a táblát
            for (int r = 0; r < this.b.jTable2.getSelectedRow()+1; r++) {

//megnezzük , hogy foglalkoztunk e már vele (pn,ws,terv vagy tény)
                boolean irtunke = false;
                Integer melyiksor = 0;

                for (int i = 0; i < model.getRowCount(); i++) {
                    try {
                        if (model.getValueAt(i, 0).equals(this.b.jTable2.getValueAt(r, 0)) && model.getValueAt(i, 1).equals(this.b.jTable2.getValueAt(r, 2)) && model.getValueAt(i, 2).equals(this.b.jTable2.getValueAt(r, 3))) {

                            irtunke = true;
                            melyiksor = i;
                        }
                    } catch (Exception e) {
                    }

                }

//ha nem írtunk létre kell hozni a modellben egy új sort és nem info sorban vagyunk
                if (!irtunke && !b.jTable2.getValueAt(r, 3).equals("Infó")) {

                    model.addRow(new Object[]{this.b.jTable2.getValueAt(r, 0), this.b.jTable2.getValueAt(r, 2), this.b.jTable2.getValueAt(r, 3), null});

//vegigrohanunk az oszlopokon es összeadjuk az összeget majd beírjuk az utolsó helyre
                    int osszeg = 0;
                    for (int o = 4; o < b.jTable2.getSelectedColumn()+1; o++) {

                        osszeg += new Tc_Stringbolint(this.b.jTable2.getValueAt(r, o).toString()).db;

                    }

                    model.setValueAt(osszeg, model.getRowCount() - 1, 3);

                }

//ha írtunk akkor össze kell adni a táblában szereplő összeget 
                if (irtunke) {

                    int osszeg = 0;
                    for (int o = 4; o < this.b.jTable2.getSelectedColumn()+1; o++) {

                        osszeg += new Tc_Stringbolint(this.b.jTable2.getValueAt(r, o).toString()).db;

                    }

                    model.setValueAt(Integer.parseInt(model.getValueAt(melyiksor, 3).toString()) + osszeg, melyiksor, 3);

                }

            }

            this.c.jTable1.setModel(model);

        }

    }

}
