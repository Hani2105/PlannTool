/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author gabor_hanacsek
 */
public class Anyageladosrenderer extends DefaultTableCellRenderer {

    public Component getTableCellRendererComponent(
            JTable table, Object value,
            boolean isSelected, boolean hasFocus,
            int row, int column) {
        JLabel c = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        try {
            if (table.getValueAt(row, 16).equals("Leadtime") && column > 16 && table.getValueAt(row, column).toString().equals("1")) {

                c.setBackground(Color.BLUE);

            } else if (table.getValueAt(row, 16).equals("Balance") && column > 16 && Double.parseDouble(table.getValueAt(row, column).toString()) < 0) {

                c.setBackground(Color.red);

            } else {

                c.setBackground(null);
            }

        } catch (Exception e) {
        }

        return c;
    }

}
