/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool.CTB_CALC;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ToolTipManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author gabor_hanacsek
 */
public class CTB_ShortTablarenderer extends DefaultTableCellRenderer {

    public CTB_ShortTablarenderer() {

    }

    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        //a tooltip eltünés és előjövetel ideje
        // Get current delay
        int initialDelay = ToolTipManager.sharedInstance().getInitialDelay();

        // Show tool tips immediately
        ToolTipManager.sharedInstance().setInitialDelay(0);
        ToolTipManager.sharedInstance().setDismissDelay(9999999);

        JLabel c = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        c.setHorizontalAlignment(CENTER);
        c.setBorder(BorderFactory.createLoweredSoftBevelBorder());
        if (column == 3 || column == 1 || column == 8) {

            c.setBackground(new Color(167, 181, 204, 60));

        } else if (column == 9 && isSelected) {
//kiszedjük , hogy melyik héten mennyi anyag jön 
//felvesszük a horizontal modellt
            String tooltiptext = "<html><body>";
            DefaultTableModel model = new DefaultTableModel();
            model = (DefaultTableModel) CTB.jTable13.getModel();

//megnezzuk melyik pn re vagyunk kiváncsiak
            String pn = table.getValueAt(row, 0).toString();

//bepörgetjük a modellt a pn után kutatva és supply sor után kutatva
            for (int i = 0; i < model.getRowCount(); i++) {
                if (model.getValueAt(i, 1).toString().equals(pn) && model.getValueAt(i, 18).toString().contains("Supply")) {
//bent vagyunk a megfelelő sorban
//bejárjuk az oszlopokat a 18. tól elkezdve
                    for (int o = 0; o < model.getColumnCount(); o++) {
//minden oszlopban meg kell keresni a hetet
                        for (int sor = i; sor >= 0; sor--) {
//ha week cellába ütközünk akkor a hét száma egyel alatta lesz
                            try {
                                if (model.getValueAt(sor, o).toString().equals("Week")) {

                                    tooltiptext += "Hét " + model.getValueAt(sor + 1, o).toString() + ": " + model.getValueAt(i, o).toString() + "DB <br>";
                                    break;

                                }
                            } catch (Exception e) {
                            }

                        }

                    }
                    break;
                }

            }

            c.setToolTipText(tooltiptext);

        } else {

            c.setBackground(null);

        }

        if (isSelected) {

            c.setForeground(Color.RED);
            c.setBorder(BorderFactory.createLineBorder(new Color(55, 52, 249), 2));

        } else {

            c.setForeground(Color.BLACK);
            c.setToolTipText(null);

        }

        return c;

    }

}
