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
        if (column > 2) {

            c.setBackground(new Color(167, 181, 204, 60));
            //c.setForeground(Color.BLACK);
            //c.setBorder(BorderFactory.createLineBorder(Color.black));

        } else {

            c.setBackground(null);

        }

        if (isSelected) {
 
            c.setForeground(Color.RED);
            
        }
        
        else{
        
            c.setForeground(Color.BLACK);
        
        }
        return c;

    }

}
