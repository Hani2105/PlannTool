/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool;

import static PlannTool.ablak.jTable1;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author gabor_hanacsek
 */
public class snrebontos extends Thread {

    public void run() {

        //seria számra lebontása a wip nek
//ha jó helyen vagyunk , azaz a kijelölés jó ablakban történt
        if (!jTable1.getSelectionModel().isSelectionEmpty()) {

            String pn = jTable1.getValueAt(jTable1.getSelectedRow(), 0).toString();
            String loc = jTable1.getValueAt(jTable1.getSelectedRow(), 1).toString();

//lekérdezzük a wip et a PN re
            xmlfeldolg xxx = new xmlfeldolg();
            Object rowdata[][] = null;

            try {

                URL url = new URL("http://143.116.140.120/rest/request.php?page=planning_iswip&product=" + pn + "&format=xml");
                ArrayList<String> lista = new ArrayList();

                String nodelist = "planning_iswip";
                lista.add("Serial_Number");
                lista.add("Part_Number");
                lista.add("SFDC_Location_Name");
                lista.add("Days_in_Location");
                lista.add("Shop_Order");

                rowdata = (Object[][]) xxx.xmlfeldolg(url, nodelist, lista);

            } catch (MalformedURLException ex) {
                Logger.getLogger(ablak.class.getName()).log(Level.SEVERE, null, ex);
            }

            //feldolgozzuk a row datát
//peldanyositunk egy snrebontost
            Snrebontoswip s = new Snrebontoswip();
            DefaultTableModel model = new DefaultTableModel();
            model = (DefaultTableModel) s.jTable1.getModel();

            for (int i = 0; i < rowdata.length; i++) {
                if (rowdata[i][2].toString().equals(loc)) {
                    model.addRow(new Object[]{rowdata[i][0], rowdata[i][1], rowdata[i][2], rowdata[i][3], rowdata[i][4]});
                }

            }

            s.jTable1.setModel(model);
            s.setVisible(true);
            animation.rajzol = false;

        }

    }
}
