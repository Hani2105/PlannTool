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

/**
 *
 * @author gabor_hanacsek
 */
public class CTB_Tablarenderer extends DefaultTableCellRenderer {

    public CTB_Tablarenderer() {

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
        c.setBorder(BorderFactory.createEtchedBorder());
        if (column < 4) {

            c.setBackground(new Color(244, 149, 71, 30));
            c.setBorder(BorderFactory.createLineBorder(Color.black));

        } else if (column == 5) {

            try {
                if (table.getValueAt(row, column).toString().equals("BOM hiba!")) {

                    c.setBackground(new Color(255, 166, 22));

                }
            } catch (Exception e) {
            }

            try {
                if (Integer.parseInt(table.getValueAt(row, column).toString()) <= 0) {

                    c.setBackground(Color.red);

                } else {

                    c.setBackground(new Color(38, 236, 28, 60));

                }
            } catch (Exception e) {
            }

        } else if (column == 4) {

            c.setBackground(new Color(66, 134, 244));

        } else if (column == 6) {
            try {
                if (table.getValueAt(row, column).toString().equals("BOMhiba!")) {

                    c.setBackground(new Color(255, 166, 22));

                }
            } catch (Exception e) {
            }

            try {
                if (Integer.parseInt(table.getValueAt(row, column - 1).toString()) <= 0) {

                    c.setBackground(Color.red);

                } else {

                    c.setBackground(new Color(38, 236, 28, 60));

                }
            } catch (Exception e) {
            }

        } else {

            c.setBorder(BorderFactory.createEtchedBorder());
            c.setBackground(new Color(0, 0, 0, 10));

        }

        if (isSelected) {

            c.setForeground(new Color(0, 0, 0));
            c.setBorder(BorderFactory.createLineBorder(new Color(55, 52, 249), 2));
        }

//        if (column == 0) {
//
//            c.setFont(new Font("Sanserif", Font.BOLD, 14));
//
//        }
        return c;

    }

}
