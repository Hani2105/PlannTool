/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool.CompleteQty;

import PlannTool.ablak;
import com.toedter.calendar.JDateChooser;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author gabor_hanacsek
 */
public class Methods {

    ablak a;

    public Methods(ablak a) {

        this.a = a;
    }

    public String PNosszefuz(JTable j) {

        String pnlist = "";
        for (int i = 0; i < j.getRowCount(); i++) {
            try {
                if (!j.getValueAt(i, 0).toString().equals("")) {

                    pnlist += j.getValueAt(i, 0).toString().trim() + ";";

                }
            } catch (Exception e) {
            }

        }
        try {
            pnlist = pnlist.substring(0, pnlist.length() - 1);
        } catch (Exception e) {

            JOptionPane.showMessageDialog(a,
                    "Nem adtál meg termékeket!",
                    "Hiba!",
                    JOptionPane.ERROR_MESSAGE);

        }

        return pnlist;

    }

    public String CreateApiUrl(JDateChooser j, JDateChooser j2, String api, String PNList) {
        String fullurl = "";
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date StartDate = j.getDate();
        Date EndDate = j2.getDate();

        fullurl = api.replace("termekek", PNList);
        try {
            fullurl = fullurl.replace("startdate", dateFormat.format(StartDate));
            fullurl = fullurl.replace("enddate", dateFormat.format(EndDate));

        } catch (Exception e) {

            JOptionPane.showMessageDialog(a,
                    "Nem adtál meg dátumot!",
                    "Hiba!",
                    JOptionPane.ERROR_MESSAGE);

        }

        return fullurl;

    }

    public void GroupByPn(Object[][] obi) {

        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) a.jTable28.getModel();
        model.setRowCount(0);

        outerloop:
        for (int i = 0; i < obi.length; i++) {

//bejárjuk a modellt és megnézzük , hogy van e már ilyen pn
            for (int m = 0; m < model.getRowCount() + 1; m++) {

//ha találunk ilyen pn-t és complete a státusz
                try {
                    if (model.getValueAt(m, 0).toString().equals(obi[i][1].toString()) && obi[i][2].toString().equals("COMP Complete")) {
                        int osszeg = 0;
                        try {
                            osszeg = Integer.parseInt(model.getValueAt(m, 2).toString()) + Integer.parseInt(obi[i][3].toString());
                        } catch (Exception e) {

                            osszeg = 0;
                        }
                        model.setValueAt(osszeg, m, 2);
                        continue outerloop;
                    }

                } catch (Exception e) {
                    //continue outerloop;
                }

            }

            if (obi[i][2].toString().equals("COMP Complete")) {
                model.addRow(new Object[]{obi[i][1].toString(), "", obi[i][3]});
            }

        }

        a.jTable28.setModel(model);
    }

    public void GroupByJOB(Object[][] obi) {

        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) a.jTable28.getModel();
        model.setRowCount(0);

        outerloop:
        for (int i = 0; i < obi.length; i++) {

//bejárjuk a modellt és megnézzük , hogy van e már ilyen pn
            for (int m = 0; m < model.getRowCount() + 1; m++) {

//ha találunk ilyen job-ot és pn-t
                try {
                    if ((model.getValueAt(m, 0).toString().equals(obi[i][1].toString())) && model.getValueAt(m, 1).toString().equals(obi[i][0].toString()) && obi[i][2].toString().equals("COMP Complete")) {
                        int osszeg = 0;
                        try {
                            osszeg = Integer.parseInt(model.getValueAt(m, 2).toString()) + Integer.parseInt(obi[i][3].toString());
                        } catch (Exception e) {

                            osszeg = 0;
                        }
                        model.setValueAt(osszeg, m, 2);
                        continue outerloop;
                    }
                } catch (Exception e) {
                    //continue outerloop;
                }

            }

            //ha nem találunk , hozzáadunk egy sort és beállítjuk az adatokat
            if (obi[i][2].toString().equals("COMP Complete")) {
                model.addRow(new Object[]{obi[i][1].toString(), obi[i][0].toString(), obi[i][3]});
            }

        }

        a.jTable28.setModel(model);
    }

}
