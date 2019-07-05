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
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ToolTipManager;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author gabor_hanacsek
 */
public class Prio_Tablerenderer extends DefaultTableCellRenderer {

    public Prio_Tablerenderer() {

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
        //c.setBorder(BorderFactory.createEtchedBorder());

        if (row == 0 && column == 0) {

            c.setBackground(new Color(250, 20, 20, 90));
            //c.setBorder(BorderFactory.createLineBorder(Color.black));
            c.setFont(new Font("Sanserif", Font.BOLD, 10));
            

        } else if (row == 1&& column == 0) {

            c.setBackground(new Color(250, 20, 20, 80));
            //c.setBorder(BorderFactory.createLineBorder(Color.black));
            c.setFont(new Font("Sanserif", Font.BOLD, 10));

        } else if (row == 2&& column == 0) {

            c.setBackground(new Color(250, 20, 20, 70));
            //c.setBorder(BorderFactory.createLineBorder(Color.black));
            c.setFont(new Font("Sanserif", Font.BOLD, 10));

        } else if (row == 3&& column == 0) {

            c.setBackground(new Color(250, 20, 20, 60));
            //c.setBorder(BorderFactory.createLineBorder(Color.black));
            c.setFont(new Font("Sanserif", Font.BOLD, 10));

        } else if (row == 4&& column == 0) {

            c.setBackground(new Color(250, 20, 20, 50));
            //c.setBorder(BorderFactory.createLineBorder(Color.black));
            c.setFont(new Font("Sanserif", Font.BOLD, 10));

        } else if (row == 5&& column == 0) {

            c.setBackground(new Color(250, 20, 20, 40));
            //c.setBorder(BorderFactory.createLineBorder(Color.black));
            c.setFont(new Font("Sanserif", Font.BOLD, 10));

        } else if (row == 6&& column == 0) {

            c.setBackground(new Color(250, 20, 20, 30));
            //c.setBorder(BorderFactory.createLineBorder(Color.black));
            c.setFont(new Font("Sanserif", Font.BOLD, 10));

        } else if (row == 7&& column == 0) {

            c.setBackground(new Color(250, 20, 20, 20));
            //c.setBorder(BorderFactory.createLineBorder(Color.black));
            c.setFont(new Font("Sanserif", Font.BOLD, 10));

        } else if (row == 8&& column == 0) {

            c.setBackground(new Color(250, 20, 20, 10));
            //c.setBorder(BorderFactory.createLineBorder(Color.black));
            c.setFont(new Font("Sanserif", Font.BOLD, 10));

        } else if (column == 0)  {

            c.setBackground(new Color(250, 20, 20, 00));
            //c.setBorder(BorderFactory.createLineBorder(Color.black));
            c.setFont(new Font("Sanserif", Font.BOLD, 10));

        }
        
        else if(column> 0){
        
        c.setBackground(null);
        c.setFont(new Font("Sanserif", Font.PLAIN, 10));
        c.setBorder(BorderFactory.createEtchedBorder());
        
        }
        
        if(isSelected){
        
        c.setBackground(new Color(250, 20, 20, 90));
        
        }

        return c;

    }

}
