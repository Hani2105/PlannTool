/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author gabor_hanacsek
 */
public class Tc_TervTablaRenderer extends DefaultTableCellRenderer {

    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        JLabel c = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        try {

            if (table.getColumnName(column).toLowerCase().contains("Sun".toLowerCase())) {

                c.setBackground(Color.RED);
                c.setBorder(BorderFactory.createEtchedBorder(1));
                c.setForeground(Color.BLACK);

            } else if (table.getColumnName(column).toLowerCase().contains("Sat".toLowerCase())) {

                c.setBackground(Color.ORANGE);
                c.setBorder(BorderFactory.createEtchedBorder(1));
                c.setForeground(Color.BLACK);

            } else {
                setBackground(null);
                c.setBorder(BorderFactory.createEtchedBorder(1));
                c.setForeground(Color.BLACK);

            }
        } catch (Exception e) {
        }

        try {
            if (table.getColumnName(column).toLowerCase().contains("PartNumber".toLowerCase()) || table.getColumnName(column).toLowerCase().contains("workstation".toLowerCase()) || table.getColumnName(column).toLowerCase().contains("job".toLowerCase())) {

                c.setBackground(Color.LIGHT_GRAY);
                c.setForeground(Color.BLACK);
                c.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            }
        } catch (Exception e) {
        }

        c.setHorizontalAlignment(CENTER);
        return c;

    }

}
