/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool;

import static com.sun.glass.ui.Application.run;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class szal extends Thread {

    ablak a;
    selejt s;
   

    public szal(ablak a, selejt s) throws MalformedURLException {
        this.a = a;
        this.s = s;
      

    }

   
    public DefaultTableModel mukodj() {


        int count = 0;

        for (int i = 0; i < 1000; i++) {

            if (ablak.modelstatus.getValueAt(i, 0) != null) {
                count = count + 1;
            }

        }

        s.selejtmodel.setRowCount(count);

        for (int i = 0; i < count; i++) {

            s.selejtmodel.setValueAt(ablak.modelstatus.getValueAt(i, 1), i, 0);
            s.selejtmodel.setValueAt(ablak.modelstatus.getValueAt(i, 2), i, 2);
            s.selejtmodel.setValueAt(ablak.modelstatus.getValueAt(i, 5), i, 5);
            s.selejtmodel.setValueAt(ablak.modelstatus.getValueAt(i, 3), i, 1);
            s.selejtmodel.setValueAt(ablak.modelstatus.getValueAt(i, 4), i, 6);

        }

        String joblist = "";
        String job = "";
        boolean irunke = true;

        for (int i = 0; i < s.selejtmodel.getRowCount(); i++) {

            irunke = true;

            for (int n = 0; n < i; n++) {
                if (s.selejtmodel.getValueAt(i, 2).toString().equals(job)) {
                    irunke = false;
                    continue;
                }
            }

            if (irunke == true) {
                joblist = joblist + s.selejtmodel.getValueAt(i, 2) + ";";
                job = s.selejtmodel.getValueAt(i, 2).toString();
            }
        }

        xmlfeldolg xxx = new xmlfeldolg();

        Object rowdata[][] = null;
        URL url = null;
        try {
            url = new URL("http://143.116.140.120/rest/request.php?page=planning_shop_order&shoporder=" + joblist + "&format=xml");
        } catch (MalformedURLException ex) {
            Logger.getLogger(szal.class.getName()).log(Level.SEVERE, null, ex);
        }
        String nodelist = "planning_shop_order";
        ArrayList<String> lista = new ArrayList();
        lista.add("Shop_Order_Number");
        lista.add("Part_Number");
        lista.add("Workstation");
        lista.add("Qty");
        lista.add("Unit_Status");
        int killkounter = 0;

        rowdata = (Object[][]) xxx.xmlfeldolg(url, nodelist, lista);

        for (int i = 0; i < s.selejtmodel.getRowCount(); i++) {
            killkounter = 0;

            for (int n = 0; n < rowdata.length; n++) {

                if (s.selejtmodel.getValueAt(i, 2).toString().equals(rowdata[n][0].toString()) && rowdata[n][4].toString().equals("Killed")) {
                    killkounter = killkounter + Integer.parseInt(rowdata[n][3].toString());
                    s.selejtmodel.setValueAt(killkounter, i, 3);
                }
                if (s.selejtmodel.getValueAt(i, 2).toString().equals(rowdata[n][0].toString()) && rowdata[n][4].toString().equals("Finished")) {
                    s.selejtmodel.setValueAt(rowdata[n][3], i, 4);
                }
            }
        }

        try {
            url = new URL("http://143.116.140.120/rest/request.php?page=planning_killonhand&job_number=" + joblist + "&format=xml");
        } catch (MalformedURLException ex) {
            Logger.getLogger(szal.class.getName()).log(Level.SEVERE, null, ex);
        }
        nodelist = "planning_killonhand";
        lista.clear();
        lista.add("job_number");
        lista.add("qty");
        rowdata = (Object[][]) xxx.xmlfeldolg(url, nodelist, lista);

        for (int i = 0; i < s.selejtmodel.getRowCount(); i++) {

            for (int n = 0; n < rowdata.length; n++) {

                if (s.selejtmodel.getValueAt(i, 2).toString().equals(rowdata[n][0].toString())) {

                    s.selejtmodel.setValueAt(rowdata[n][1], i, 7);
                    try {
                        s.selejtmodel.setValueAt(Integer.parseInt(s.selejtmodel.getValueAt(i, 3).toString()) - Integer.parseInt(s.selejtmodel.getValueAt(i, 7).toString()), i, 8);
                    } catch (Exception ex) {

                        s.selejtmodel.setValueAt(0, i, 8);

                    }

                }
            }
        }

        return s.selejtmodel;

    }
}
