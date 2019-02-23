/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author gabor_hanacsek
 */
class Tooltiprenderer extends DefaultTableCellRenderer {

    public Component getTableCellRendererComponent(
            JTable table, Object value,
            boolean isSelected, boolean hasFocus,
            int row, int column) {
        JLabel c = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        String tooltiptext = "";
        try {
            for (int i = 0; i < ablak.lista.get(0).length; i++) {

                if (ablak.lista.get(0)[i][0] != null) {

                    try {

                        if (ablak.lista.get(0)[i][0].equals(c.getText())) {

                            tooltiptext += "<html>" + ablak.lista.get(0)[i][0] + "  " + ablak.lista.get(0)[i][1] + "  " + ablak.lista.get(0)[i][2] + "<br>";

                        }
                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                }

            }
        } catch (Exception e) {
        }

        c.setToolTipText(tooltiptext);
        return c;
    }
}
