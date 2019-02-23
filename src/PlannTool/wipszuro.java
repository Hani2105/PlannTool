/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author gabor_hanacsek
 */
public class wipszuro {

    JTable j;

    public wipszuro(JTable j) {

        this.j = j;

    }

    public DefaultTableModel smtkiszed() {

        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) j.getModel();
        model.setRowCount(0);
        if (ablak.jCheckBox4.isSelected()) {

            for (int i = 0; i < ablak.wiplist.size(); i++) {

                if (!ablak.wiplist.get(i)[0].contains("SMT")) {

                    model.addRow(new Object[]{ablak.wiplist.get(i)[0], ablak.wiplist.get(i)[1], ablak.wiplist.get(i)[2]});

                }

            }

        } else {

            model.setRowCount(0);

            for (int i = 0; i < ablak.wiplist.size(); i++) {

                model.addRow(new Object[]{ablak.wiplist.get(i)[0], ablak.wiplist.get(i)[1], ablak.wiplist.get(i)[2]});

            }

        }

        return model;

    }

}
