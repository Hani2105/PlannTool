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
public class CTB_Tablarenderer extends DefaultTableCellRenderer {

    public CTB_Tablarenderer() {

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
        if (column == 0) {

            c.setBackground(new Color(229, 246, 33, 100));
            c.setBorder(BorderFactory.createEtchedBorder());

//összeszedjük a pn hez a darabokat
            String pn = "";

            try {
                pn = table.getValueAt(row, column).toString();
            } catch (Exception e) {

            }

            if (!pn.equals("") && CTB_Wipquery.rowdata != null) {
                String tooltip = "<html>";
                for (int i = 0; i < CTB_Wipquery.rowdata.length; i++) {

                    if (pn.equals(CTB_Wipquery.rowdata[i][0].toString())) {

                        //tooltip += CTB_Wipquery.rowdata[i][0].toString() + "<br>";
                        tooltip += CTB_Wipquery.rowdata[i][1].toString() + ": ";
                        tooltip += CTB_Wipquery.rowdata[i][2].toString() + "DB<br>";

                    }

                }

                tooltip += "</html>";
                c.setToolTipText(tooltip);
            } else {
                c.setToolTipText(null);
            }

        } else if (column == 1) {

            c.setBackground(new Color(229, 246, 33, 80));
            c.setBorder(BorderFactory.createEtchedBorder());
            c.setToolTipText(null);

        } else if (column == 2) {

            c.setBackground(new Color(229, 246, 33, 60));
            c.setBorder(BorderFactory.createEtchedBorder());
            c.setToolTipText(null);

        } else if (column == 3) {

            c.setBackground(new Color(229, 246, 33, 40));
            c.setBorder(BorderFactory.createEtchedBorder());
            c.setToolTipText(null);

        } else if (column == 4) {

            c.setBackground(new Color(229, 246, 33, 20));
            c.setBorder(BorderFactory.createEtchedBorder());
            c.setToolTipText(null);

        } else if (column == 5) {

            try {
                if (table.getValueAt(row, column).toString().equals("BOM hiba!")) {

                    c.setBackground(new Color(255, 166, 22));
                    c.setToolTipText(null);

                }
            } catch (Exception e) {
            }

            try {
                if (Integer.parseInt(table.getValueAt(row, column).toString()) <= 0) {

                    c.setBackground(Color.red);

                } else {

                    c.setBackground(new Color(38, 236, 28, 60));

                }
            } catch (Exception e) {
            }
            c.setToolTipText(null);
        } else if (column == 6) {
            try {
                if (table.getValueAt(row, column).toString().equals("BOMhiba!")) {

                    c.setBackground(new Color(255, 166, 22));

                }
            } catch (Exception e) {
            }

            try {
                if (Integer.parseInt(table.getValueAt(row, column - 1).toString()) <= 0) {

                    c.setBackground(Color.red);

                } else {

                    c.setBackground(new Color(38, 236, 28, 60));

                }
            } catch (Exception e) {
            }
            c.setToolTipText(null);
        } else {

            c.setBorder(BorderFactory.createEtchedBorder());
            c.setBackground(new Color(0, 0, 0, 10));
            c.setToolTipText(null);

        }

        if (isSelected) {

            c.setForeground(new Color(0, 0, 0));
            c.setBorder(BorderFactory.createLineBorder(new Color(55, 52, 249), 2));
        }

//        if (column == 0) {
//
//            c.setFont(new Font("Sanserif", Font.BOLD, 14));
//
//        }
        return c;

    }

}
