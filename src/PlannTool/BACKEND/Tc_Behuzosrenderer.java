/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool.BACKEND;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author gabor_hanacsek
 */
public class Tc_Behuzosrenderer extends DefaultTableCellRenderer {

    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        JLabel c = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        String tooltip = "";

        if (column == 6) {

            String[] tooltiptext = table.getValueAt(row, column).toString().split(" ");

            for (int i = 0; i < tooltiptext.length; i++) {

                tooltip += "<html>" + tooltiptext[i] + "<br>";

            }

        } else {
            //tooltiptext = null;
        }

        c.setToolTipText(tooltip);

        return c;

    }

}
