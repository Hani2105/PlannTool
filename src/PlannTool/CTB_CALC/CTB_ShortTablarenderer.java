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
        c.setBorder(BorderFactory.createEtchedBorder());
        if (column == 3 || column == 0 || column == 8 ) {

            c.setBackground(new Color(167, 181, 204, 60));
//ha nem üres a stockdata és egyezik a pn akkor beírjuk a tooltipet

//            if (CTB.WipStockData != null || CTB.OraStockData != null) {
//                String tooltip = "<html>";
//                for (int i = 0; i < CTB.WipStockData.length; i++) {
//
//                    tooltip += CTB.WipStockData[i][0] + " " + CTB.WipStockData[i][1] + "<br>";
//
//                }
//                try {
//                    for (int i = 0; i < CTB.OraStockData.length; i++) {
//
//                        tooltip += CTB.OraStockData[i][0] + " " + CTB.OraStockData[i][1] + " " + CTB.OraStockData[i][2] + "<br>";
//
//                    }
//                } catch (Exception e) {
//                }
//
//                tooltip += "</html>";
//                c.setToolTipText(tooltip);
//
//            } else {
//                c.setToolTipText(null);
//            }

        } else {

            c.setBackground(null);

        }

        if (isSelected) {

            c.setForeground(Color.RED);
            c.setBorder(BorderFactory.createLineBorder(new Color(55, 52, 249), 2));

        } else {

            c.setForeground(Color.BLACK);

        }

        return c;

    }

}
