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
public class CTB_PartsToPlanTableRenderer extends DefaultTableCellRenderer {

    public CTB_PartsToPlanTableRenderer() {

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
        //c.setBorder(BorderFactory.createLoweredSoftBevelBorder());
        c.setBackground(new Color(0, 0, 0, 10));

        if (column == 3) {

            try {
                if (Integer.parseInt(table.getValueAt(row, column).toString()) > 0) {

                    c.setBackground(new Color(237, 2, 45, 60));

                } else {

                    c.setBackground(new Color(38, 236, 28, 60));
                }
            } catch (Exception e) {

            }
        } else if (column == 0) {

            c.setBackground(new Color(0, 122, 138, 100));

        } else if (column == 1) {

            c.setBackground(new Color(0, 140, 158, 80));

        } else if (column == 2) {

            c.setBackground(new Color(0, 156, 176, 60));

        } else {

           // c.setBorder(BorderFactory.createLoweredSoftBevelBorder());
            c.setBackground(new Color(0, 0, 0, 30));

        }

        if (isSelected) {

            //ha nem egyezik a kijelölt oszlop fejléce azzal az adattal amivel kalkuláltunk legyen piros a kijelölés
            if (CTB.jTable9.getColumnModel().getColumn(9).getHeaderValue().toString().contains(table.getColumnModel().getColumn(table.getSelectedColumn()).getHeaderValue().toString())) {

                c.setForeground(new Color(0, 0, 0));
                c.setBorder(BorderFactory.createLineBorder(new Color(55, 52, 249), 2));

            } else {

                c.setForeground(new Color(0, 0, 0));
                c.setBorder(BorderFactory.createLineBorder(new Color(252, 3, 3), 2));

            }

        }

        return c;

    }

}
