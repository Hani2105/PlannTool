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
        c.setBorder(BorderFactory.createEtchedBorder());
        c.setBackground(new Color(0, 0, 0, 10));

        if (column == 2) {

            try {
                if (Integer.parseInt(table.getValueAt(row, column).toString()) > 0) {

                    c.setBackground(Color.red);

                } else {

                    c.setBackground(new Color(38, 236, 28, 60));
                }
            } catch (Exception e) {
                // c.setBackground(new Color(38, 236, 28, 60));
            }
        } else if (column == 0) {

            c.setBackground(new Color(229, 246, 33, 100));

        } else if (column == 1) {

            c.setBackground(new Color(229, 246, 33, 80));

        } else {

            c.setBorder(BorderFactory.createEtchedBorder());
            c.setBackground(new Color(0, 0, 0, 10));

        }

        if (isSelected) {

            c.setForeground(new Color(0, 0, 0));
            c.setBorder(BorderFactory.createLineBorder(new Color(55, 52, 249), 2));
        }

        return c;

    }

}
