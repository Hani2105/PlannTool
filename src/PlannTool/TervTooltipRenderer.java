/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool;

import java.awt.Color;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author gabor_hanacsek
 */
public class TervTooltipRenderer extends DefaultTableCellRenderer{
    
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        JLabel c = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        String tooltiptext = "";

         if(column > 3 && (table.getValueAt(row, 0) != null && table.getValueAt(row, 1) != null && table.getValueAt(row, 2) != null ) && table.getValueAt(row, column) != null){
         
         tooltiptext =(table.getValueAt(row, 3).toString() + " " + table.getValueAt(row, 0).toString() + " " + table.getValueAt(row, 1).toString() + " " + table.getValueAt(row, 2).toString());
             
         }
         
         else{
         
             tooltiptext = null;
             
         }

        c.setBorder(BorderFactory.createEtchedBorder(1));
        c.setToolTipText(tooltiptext);
        c.setHorizontalAlignment(CENTER);
        return c;

    }
    
    
}
