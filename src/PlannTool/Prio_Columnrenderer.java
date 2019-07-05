/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import static javax.swing.GroupLayout.Alignment.CENTER;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.LookAndFeel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author gabor_hanacsek
 */
public class Prio_Columnrenderer extends DefaultTableCellRenderer {

    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        JLabel c = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

//        if (column > 6) {
//            //c.setFont(new Font("Sanserif", Font.BOLD, 10));
//            c.setForeground(Color.BLACK);
//        }
        if (column == 0) {

            c.setBackground(new Color(250, 20, 20, 100));
            //c.setBorder(BorderFactory.createLineBorder(Color.black));
            c.setFont(new Font("Sanserif", Font.BOLD, 10));

        } else if (column == 1) {

            c.setBackground(new Color(250, 20, 20, 90));
            //c.setBorder(BorderFactory.createLineBorder(Color.black));
            c.setFont(new Font("Sanserif", Font.BOLD, 10));

        } else if (column == 2) {

            c.setBackground(new Color(250, 20, 20, 80));
            //c.setBorder(BorderFactory.createLineBorder(Color.black));
            c.setFont(new Font("Sanserif", Font.BOLD, 10));

        } else if (column == 3) {

            c.setBackground(new Color(250, 20, 20, 70));
            //c.setBorder(BorderFactory.createLineBorder(Color.black));
            c.setFont(new Font("Sanserif", Font.BOLD, 10));

        } else if (column == 4) {

            c.setBackground(new Color(250, 20, 20, 60));
            //c.setBorder(BorderFactory.createLineBorder(Color.black));
            c.setFont(new Font("Sanserif", Font.BOLD, 10));

        } else if (column == 5) {

            c.setBackground(new Color(250, 20, 20, 50));
            //c.setBorder(BorderFactory.createLineBorder(Color.black));
            c.setFont(new Font("Sanserif", Font.BOLD, 10));

        } else if (column == 6) {

            c.setBackground(new Color(250, 20, 20, 40));
            //c.setBorder(BorderFactory.createLineBorder(Color.black));
            c.setFont(new Font("Sanserif", Font.BOLD, 10));

        } else if (column == 7) {

            c.setBackground(new Color(250, 20, 20, 30));
            //c.setBorder(BorderFactory.createLineBorder(Color.black));
            c.setFont(new Font("Sanserif", Font.BOLD, 10));

        } else if (column == 8) {

            c.setBackground(new Color(250, 20, 20, 20));
            //c.setBorder(BorderFactory.createLineBorder(Color.black));
            c.setFont(new Font("Sanserif", Font.BOLD, 10));

        } else if (column == 9) {

            c.setBackground(new Color(250, 20, 20, 10));
            //c.setBorder(BorderFactory.createLineBorder(Color.black));
            c.setFont(new Font("Sanserif", Font.BOLD, 10));

        } else if (column == 10) {

            c.setBackground(new Color(250, 20, 20, 0));
            //c.setBorder(BorderFactory.createLineBorder(Color.black));
            c.setFont(new Font("Sanserif", Font.BOLD, 10));

        } 
        
        else if (column == 11) {

            c.setBackground(new Color(250, 20, 20, 0));
            //c.setBorder(BorderFactory.createLineBorder(Color.black));
            c.setFont(new Font("Sanserif", Font.BOLD, 10));

        } 
        else if (column == 12) {

            c.setBackground(new Color(250, 20, 20, 0));
            //c.setBorder(BorderFactory.createLineBorder(Color.black));
            c.setFont(new Font("Sanserif", Font.BOLD, 10));

        } 
        
        else if (column == 13) {

            c.setBackground(new Color(250, 20, 20, 0));
            //c.setBorder(BorderFactory.createLineBorder(Color.black));
            c.setFont(new Font("Sanserif", Font.BOLD, 10));

        } 
        else if (column == 14) {

            c.setBackground(new Color(250, 20, 20, 0));
            //c.setBorder(BorderFactory.createLineBorder(Color.black));
            c.setFont(new Font("Sanserif", Font.BOLD, 10));

        } 
        else if (column == 15) {

            c.setBackground(new Color(250, 20, 20, 0));
            //c.setBorder(BorderFactory.createLineBorder(Color.black));
            c.setFont(new Font("Sanserif", Font.BOLD, 10));

        } 
        
        
        else {

            c.setBackground(null);
        }
        
        
        

        c.setHorizontalAlignment(CENTER);

        return c;

    }

}
