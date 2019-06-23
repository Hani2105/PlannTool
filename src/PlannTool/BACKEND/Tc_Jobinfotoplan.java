/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool.BACKEND;

import PlannTool.infobox;
import PlannTool.xmlfeldolg;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import static org.apache.poi.hemf.hemfplus.record.HemfPlusRecordType.object;

/**
 *
 * @author gabor_hanacsek
 */
public class Tc_Jobinfotoplan extends Thread {

    Tc_Besheet b;

    public Tc_Jobinfotoplan(Tc_Besheet b) {

        this.b = b;

    }

    public void run() {
        b.jobadat.clear();
//összeszedjük a jobszámokat a sheetről
        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) b.jTable2.getModel();

        //kiszedjuk a tablabol az adatokat es lekerdezzuk
        String jobszamok = "";
        for (int i = 0; i < model.getRowCount(); i++) {

            //ha terv sorban vagyunk és nem üres a job
            try {
                if (model.getValueAt(i, 1) != null && !model.getValueAt(i, 1).equals("")) {
                    jobszamok += model.getValueAt(i, 1).toString().trim() + ";";
                }
            } catch (Exception e) {
            }

        }

        try {

            jobszamok = jobszamok.substring(0, jobszamok.length() - 1);
            jobszamok = jobszamok.replaceAll("[+.^:,/ ]", "");
        } catch (Exception e) {
        }

        xmlfeldolg xxx = new xmlfeldolg();
        Object rowdata[][] = null;
        URL url = null;
        try {
            url = new URL("http://143.116.140.120/rest/request.php?page=planning_shop_order&shoporder=" + jobszamok + "&format=xml");
            ArrayList<String> lista = new ArrayList();

            String nodelist = "planning_shop_order";
            lista.add("Shop_Order_Number");
            lista.add("Part_Number");
            lista.add("Workstation");
            lista.add("Qty");
            lista.add("Unit_Status");
            lista.add("Order_Status");
            try {
                rowdata = (Object[][]) xxx.xmlfeldolg(url, nodelist, lista);
            } catch (Exception e) {
                infobox inf = new infobox();
                inf.infoBox("Nem lehetséges a JOB infók összegyűjtése , túl sok vagy hibás JOB számok miatt!", "Hiba!");
            }

        } catch (Exception e) {
        }

        try {
            String jobadat[][] = new String[rowdata.length][5];

            for (int i = 0; i < rowdata.length; i++) {

                jobadat[i][0] = rowdata[i][0].toString();
                jobadat[i][1] = rowdata[i][2].toString();
                jobadat[i][2] = rowdata[i][3].toString();
                jobadat[i][3] = rowdata[i][5].toString();
                jobadat[i][4] = rowdata[i][4].toString();

            }

            b.jobadat.add(jobadat);

        } catch (Exception e) {
        }

        b.jTable2.repaint();
    }

}
