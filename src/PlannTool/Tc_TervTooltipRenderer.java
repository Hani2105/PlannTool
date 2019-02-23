/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool;

import java.awt.Color;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author gabor_hanacsek
 */
public class Tc_TervTooltipRenderer extends DefaultTableCellRenderer {

    Tc_Besheet b;

    public Tc_TervTooltipRenderer(Tc_Besheet b) {

        this.b = b;

        //eltesszuk az oszlop szelessegeket
        Tc_Betervezo.szelessegek.clear();
        for (int i = 0; i < b.jTable2.getColumnCount(); i++) {

            Tc_Betervezo.szelessegek.add(b.jTable2.getColumnModel().getColumn(i).getWidth());

        }

    }

    ;
    
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        JLabel c = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        String tooltiptext = "";

        //tooltip beállítása
        try {
            Tc_Stringbolint si = new Tc_Stringbolint(table.getValueAt(row, column).toString());
            if (column > 3 && (table.getValueAt(row, 0) != null && table.getValueAt(row, 2) != null) && table.getValueAt(row, column) != null && !table.getValueAt(row, column).toString().equals("")) {

                String komment = "";
                try {
                    tooltiptext = ("<html>" + "Terv/Tény: " + table.getValueAt(row, 3).toString() + "<br>" + "PN: " + table.getValueAt(row, 0).toString() + "<br>" + "JOB: " + table.getValueAt(row, 1).toString() + "<br>" + "WS: " + table.getValueAt(row, 2).toString());

                    komment = si.komment;

                    tooltiptext += "<br> Komment: " + komment + "</html>";

                } catch (Exception e) {
                };
            } else {

                tooltiptext = null;

            }
        } catch (Exception e) {
        }

//pn szinezes , narancs ha nincs az adott cellahoz
        try {
            if (column == 0 && (table.getValueAt(row, 0) != null && !table.getValueAt(row, 0).toString().equals(""))) {

                boolean narancs = true;

                for (int i = 0; i < b.partnumbers.size(); i++) {

                    if (table.getValueAt(row, 0).toString().equals(b.partnumbers.get(i))) {

                        narancs = false;

                    }

                }

//pn szinezese pirosra ha nincs az adatbazisban!
                boolean piros = true;
                for (int i = 0; i < Tc_Betervezo.partn.size(); i++) {

                    if (table.getValueAt(row, 0).toString().equals(Tc_Betervezo.partn.get(i))) {

                        piros = false;

                    }

                }

//ha nincs ciklusido szurke
                boolean cian = true;

                for (int n = 0; n < Tc_Betervezo.ciklusidok.get(0).length; n++) {

                    if (Tc_Betervezo.ciklusidok.get(0)[n][0].equals(Tc_Betervezo.Tervezotabbed.getTitleAt(Tc_Betervezo.Tervezotabbed.getSelectedIndex())) && Tc_Betervezo.ciklusidok.get(0)[n][1].equals(table.getValueAt(row, 0).toString()) && Tc_Betervezo.ciklusidok.get(0)[n][2].equals(table.getValueAt(row, 2).toString())) {

                        cian = false;

                    }

                }

                if (piros == true) {

                    c.setBackground(Color.RED);
                } else if (narancs == true) {
                    c.setBackground(Color.ORANGE);
                } else if (cian == true) {

                    c.setBackground(Color.LIGHT_GRAY);

                } else {

                    //ha terv a sor
                    if (table.getValueAt(row, 3).toString().equals("Terv")) {

                        c.setBackground(new Color(Tc_Betervezo.slide1, Tc_Betervezo.slide2, Tc_Betervezo.slide3));
                        //ha tény a sor
                    } else if (table.getValueAt(row, 3).toString().equals("Tény")) {

                        c.setBackground(new Color(Tc_Betervezo.slide4, Tc_Betervezo.slide5, Tc_Betervezo.slide6));

                    }

                }

            } //ws szinezes , narancs ha nincs az adott cellaban
            else if (column == 2 && (table.getValueAt(row, 2) != null && !table.getValueAt(row, 2).toString().equals(""))) {

                boolean narancs = true;

                for (int i = 0; i < b.workstations.size(); i++) {

                    if (table.getValueAt(row, 2).toString().equals(b.workstations.get(i))) {

                        narancs = false;

                    }

                }
// ha nem letezik a ws egyaltalan piros

                boolean piros = true;
                for (int i = 0; i < Tc_Betervezo.works.size(); i++) {

                    if (table.getValueAt(row, 2).toString().equals(Tc_Betervezo.works.get(i))) {

                        piros = false;

                    }

                }

//ha nincs ciklusido szurke
                boolean cian = true;

                for (int n = 0; n < Tc_Betervezo.ciklusidok.get(0).length; n++) {

                    if (Tc_Betervezo.ciklusidok.get(0)[n][0].equals(Tc_Betervezo.Tervezotabbed.getTitleAt(Tc_Betervezo.Tervezotabbed.getSelectedIndex())) && Tc_Betervezo.ciklusidok.get(0)[n][1].equals(table.getValueAt(row, 0).toString()) && Tc_Betervezo.ciklusidok.get(0)[n][2].equals(table.getValueAt(row, 2).toString())) {

                        cian = false;

                    }

                }

                if (piros == true) {

                    c.setBackground(Color.RED);
                } else if (narancs == true) {

                    c.setBackground(Color.ORANGE);

                } else if (cian == true) {

                    c.setBackground(Color.LIGHT_GRAY);

                }

            } //infó sorok színezése
            else if (table.getValueAt(row, 3).toString().equals("Infó") && column == 3) {

                c.setBackground(Color.YELLOW);

                //terv sorok szinezése
            } else if (table.getValueAt(row, 3).toString().equals("Terv")) {

                c.setBackground(new Color(Tc_Betervezo.slide1, Tc_Betervezo.slide2, Tc_Betervezo.slide3));

            } //teny sorok szinezese
            else if (table.getValueAt(row, 3).toString().equals("Tény")) {

                c.setBackground(new Color(Tc_Betervezo.slide4, Tc_Betervezo.slide5, Tc_Betervezo.slide6));

            } else {

                c.setBackground(null);

            }

            //calculátor zöld színe ha hagyobb vagy egyenlő a tény a tervnél és nem nulla és már a terv reszben vagyunk
            if (column > 3 && Integer.parseInt(table.getValueAt(row, table.getColumnCount() - 1).toString()) != 0 && table.getValueAt(row, 3).equals("Tény") && ((Integer.parseInt(table.getValueAt(row, table.getColumnCount() - 1).toString())) >= (Integer.parseInt(table.getValueAt(row - 1, table.getColumnCount() - 1).toString())))) {

                c.setBackground(new Color(Tc_Betervezo.slide7, Tc_Betervezo.slide8, Tc_Betervezo.slide9));

            } else if (column > 3 && Integer.parseInt(table.getValueAt(row, table.getColumnCount() - 1).toString()) != 0 && table.getValueAt(row, 3).equals("Terv") && ((Integer.parseInt(table.getValueAt(row, table.getColumnCount() - 1).toString())) <= (Integer.parseInt(table.getValueAt(row + 1, table.getColumnCount() - 1).toString())))) {

                c.setBackground(new Color(Tc_Betervezo.slide7, Tc_Betervezo.slide8, Tc_Betervezo.slide9));

            }

        } catch (Exception e) {
        }

        if (isSelected) {

            c.setBackground(Color.LIGHT_GRAY);
        }

        c.setBorder(BorderFactory.createEtchedBorder(1));
        c.setToolTipText(tooltiptext);
        c.setHorizontalAlignment(CENTER);

        return c;

    }

}
