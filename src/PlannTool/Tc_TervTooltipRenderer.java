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

    }

    ;
    
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        JLabel c = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        String tooltiptext = "";

        //tooltip beállítása
        try {
            if (column > 3 && (table.getValueAt(row, 0) != null && table.getValueAt(row, 2) != null) && table.getValueAt(row, column) != null && !table.getValueAt(row, column).toString().equals("")) {

                String komment = "";
                try {
                    tooltiptext = ("<html>" + "Terv/Tény: " + table.getValueAt(row, 3).toString() + "<br>" + "PN: " + table.getValueAt(row, 0).toString() + "<br>" + "JOB: " + table.getValueAt(row, 1).toString() + "<br>" + "WS: " + table.getValueAt(row, 2).toString());

                    
                        komment = new Tc_Stringbolint(table.getValueAt(row, column).toString()).komment;
                    

                    tooltiptext += "<br> Komment: " + komment + "</html>";

                } catch (Exception e) {
                };
            } else {

                tooltiptext = null;

            }
        } catch (Exception e) {
        }

        //pn szinezes , piros ha nincs az adatbazisban+
        try {
            if (column == 0 && (table.getValueAt(row, 0) != null && !table.getValueAt(row, 0).toString().equals(""))) {

                boolean piros = true;

                for (int i = 0; i < b.partnumbers.size(); i++) {

                    if (table.getValueAt(row, 0).toString().equals(b.partnumbers.get(i))) {

                        piros = false;

                    }

                }

                if (piros == true) {

                    c.setBackground(Color.red);
                } else {

                    //ha terv a sor
                    if (table.getValueAt(row, 3).toString().equals("Terv")) {

                        c.setBackground(new Color(Tc_Betervezo.slide1, Tc_Betervezo.slide2, Tc_Betervezo.slide3));
                        //ha tény a sor
                    } else if (table.getValueAt(row, 3).toString().equals("Tény")) {

                        c.setBackground(new Color(Tc_Betervezo.slide4, Tc_Betervezo.slide5, Tc_Betervezo.slide6));

                    }

                }

            } //ws szinezes , piros ha nincs az adatbazisban
            else if (column == 2 && (table.getValueAt(row, 2) != null && !table.getValueAt(row, 2).toString().equals(""))) {

                boolean piros = true;

                for (int i = 0; i < b.workstations.size(); i++) {

                    if (table.getValueAt(row, 2).toString().equals(b.workstations.get(i))) {

                        piros = false;

                    }

                }

                if (piros == true) {

                    c.setBackground(Color.red);
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

            if (table.getValueAt(row, 3).equals("Tény") && ((Integer.parseInt(table.getValueAt(row, table.getColumnCount() - 1).toString())) >= (Integer.parseInt(table.getValueAt(row - 1, table.getColumnCount() - 1).toString())))) {

                c.setBackground(Color.GREEN);

            } else if (table.getValueAt(row, 3).equals("Terv") && ((Integer.parseInt(table.getValueAt(row, table.getColumnCount() - 1).toString())) <= (Integer.parseInt(table.getValueAt(row + 1, table.getColumnCount() - 1).toString())))) {

                c.setBackground(Color.GREEN);

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
