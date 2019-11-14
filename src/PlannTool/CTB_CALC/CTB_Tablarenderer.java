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
//akkor futunk ha kész a comptoctb szal

        //a tooltip eltünés és előjövetel ideje
        // Get current delay
        int initialDelay = ToolTipManager.sharedInstance().getInitialDelay();

        // Show tool tips immediately
        ToolTipManager.sharedInstance().setInitialDelay(0);
        ToolTipManager.sharedInstance().setDismissDelay(9999999);

        JLabel c = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        c.setHorizontalAlignment(CENTER);
       // c.setBorder(BorderFactory.createLoweredSoftBevelBorder()    );
        c.setIcon(null);
        if (column == 0) {

            c.setBackground(new Color(0, 122, 138, 100));
           // c.setBorder(BorderFactory.createLoweredSoftBevelBorder()    );

        } else if (column == 1) {

            try {
                if (Integer.parseInt(table.getValueAt(row, column).toString()) <= 0) {

                    c.setBackground(new Color(237, 2, 45, 60));

                } else {

                    c.setBackground(new Color(0, 140, 158, 80));

                }
            } catch (Exception e) {
                c.setBackground(new Color(0, 140, 158, 80));
            }

        } else if (column == 2) {

            c.setBackground(new Color(0, 156, 176, 60));
            //c.setBorder(BorderFactory.createLoweredSoftBevelBorder()    );
            c.setToolTipText(null);
            c.setIcon(null);

        } else if (column == 3) {

            c.setBackground(new Color(0, 170, 191, 40));
           // c.setBorder(BorderFactory.createLoweredSoftBevelBorder()    );
            c.setToolTipText(null);
            c.setIcon(null);

        } else if (column == 4) {

            try {
                if (Integer.parseInt(table.getValueAt(row, column).toString()) <= 0) {

                    c.setBackground(new Color(237, 2, 45, 60));

                } else {

                    c.setBackground(new Color(0, 186, 209, 20));

                }
            } catch (Exception e) {
                c.setBackground(new Color(0, 186, 209, 20));
            }

        } else if (column == 5) {

            try {
                if (table.getValueAt(row, column).toString().equals("BOM hiba!")) {

                    c.setBackground(new Color(197, 198, 201));
                    c.setToolTipText(null);
                    c.setIcon(null);

                }
            } catch (Exception e) {
                c.setBackground(new Color(197, 198, 201));
            }

            try {
                if (Integer.parseInt(table.getValueAt(row, column).toString()) <= 0) {

                    c.setBackground(new Color(237, 2, 45, 60));

                } else {

                    c.setBackground(new Color(38, 236, 28, 60));

                }
            } catch (Exception e) {
            }
            c.setToolTipText(null);
        } else if (column == 6) {
            try {
                if (table.getValueAt(row, column - 1).toString().equals("BOM hiba!")) {

                    c.setBackground(new Color(197, 198, 201));

                }
            } catch (Exception e) {
                c.setBackground(new Color(197, 198, 201));
            }

            try {
                if (Integer.parseInt(table.getValueAt(row, column - 1).toString()) <= 0) {

                    c.setBackground(new Color(237, 2, 45, 60));

                } else {

                    c.setBackground(new Color(38, 236, 28, 60));

                }
            } catch (Exception e) {
            }
            c.setToolTipText(null);

        } else if (column == 7) {
               c.setHorizontalAlignment(LEFT);
               c.setBackground(new Color(197, 198, 201));
               c.setFont(new java.awt.Font("Segoe Script", 1, 12));

        } else {

         //   c.setBorder(BorderFactory.createLoweredSoftBevelBorder()    );
            c.setBackground(new Color(0, 0, 0, 10));
            c.setToolTipText(null);
            c.setIcon(null);

        }

        if (isSelected) {

            c.setForeground(new Color(0, 0, 0));
            c.setBorder(BorderFactory.createLineBorder(new Color(55, 52, 249), 2));
        }

        return c;

    }

}
