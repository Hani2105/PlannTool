/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool;

import static PlannTool.ablak.jTable1;
import static PlannTool.ablak.jTable2;
import static PlannTool.ablak.jTextField6;
import static PlannTool.ablak.model;
import static PlannTool.ablak.model1;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author gabor_hanacsek
 */
public class universalfilter {

    public universalfilter(String keresendo, JTable tabla) {

        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) tabla.getModel();
        TableRowSorter<DefaultTableModel> tr = new TableRowSorter<DefaultTableModel>(model);

        tabla.setRowSorter(tr);

        tr.setRowFilter(RowFilter.regexFilter(keresendo));

//        int total = 0;
//
//        for (int i = 0; i < jTable1.getRowCount(); i++) {
//            String value = (String) jTable1.getValueAt(i, 2);
//            total += Integer.parseInt(value);
//        }
//
//        jTextField6.setText(Integer.toString(total));
    }

}
