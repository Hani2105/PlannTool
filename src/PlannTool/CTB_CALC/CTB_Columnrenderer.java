/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool.CTB_CALC;

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
public class CTB_Columnrenderer extends DefaultTableCellRenderer {

    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        JLabel c = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (column == 0) {

            c.setBackground(new Color(0, 122, 138, 100));
            //c.setBorder(BorderFactory.createLineBorder(Color.black));
            c.setFont(new Font("Sanserif", Font.BOLD, 10));

        } else if (column == 1) {

            c.setBackground(new Color(0, 140, 158, 80));
            //c.setBorder(BorderFactory.createLineBorder(Color.black));
            c.setFont(new Font("Sanserif", Font.BOLD, 10));

        } else if (column == 2) {

            c.setBackground(new Color(0, 156, 176, 60));
            //c.setBorder(BorderFactory.createLineBorder(Color.black));
            c.setFont(new Font("Sanserif", Font.BOLD, 10));

        } else if (column == 3) {

            c.setBackground(new Color(0, 170, 191, 40));
            //c.setBorder(BorderFactory.createLineBorder(Color.black));
            c.setFont(new Font("Sanserif", Font.BOLD, 10));

        } else if (column == 4) {

            c.setBackground(new Color(0, 186, 209, 20));
            //c.setBorder(BorderFactory.createLineBorder(Color.black));
            c.setFont(new Font("Sanserif", Font.BOLD, 10));

        } else if (column == 5) {

            c.setBackground(null);
            //c.setBorder(BorderFactory.createLineBorder(Color.black));
            c.setFont(new Font("Sanserif", Font.BOLD, 10));

        } else if (column == 6) {

            c.setBackground(null);
            //c.setBorder(BorderFactory.createLineBorder(Color.black));
            c.setFont(new Font("Sanserif", Font.BOLD, 10));

        } else if (column == 7) {

            c.setBackground(null);
            //c.setBorder(BorderFactory.createLineBorder(Color.black));
            c.setFont(new Font("Sanserif", Font.BOLD, 10));

        } else if (column == 8) {

            c.setBackground(null);
            //c.setBorder(BorderFactory.createLineBorder(Color.black));
            c.setFont(new Font("Sanserif", Font.BOLD, 10));

        } else if (column == 9) {

            c.setBackground(null);
            //c.setBorder(BorderFactory.createLineBorder(Color.black));
            c.setFont(new Font("Sanserif", Font.BOLD, 10));

        } else {

            c.setBackground(null);
        }

        c.setHorizontalAlignment(CENTER);

        return c;

    }

}
