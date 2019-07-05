/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool.CTB_CALC;

import static PlannTool.CTB_CALC.CTB_Wipquery.rowdata;
import PlannTool.ablak;
import PlannTool.xmlfeldolg;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;

/**
 *
 * @author gabor_hanacsek
 */
public class CTB_Wipquery extends Thread {

    JTable j;
    public static Object rowdata[][] = null;

    public CTB_Wipquery(JTable j) {

        this.j = j;

    }

    @Override

    public void run() {

//lekerdezzuk a wip et mindenre is
        xmlfeldolg xxx = new xmlfeldolg();
        URL url = null;
        try {

            url = new URL("http://143.116.140.120/rest/request.php?page=planning_shipment_plan_process_all&product=&format=xml");
            ArrayList<String> lista = new ArrayList();

            String nodelist = "planning_shipment_plan_process_all";
            lista.add("Part_Number");
            lista.add("SFDC_Location_Name");
            lista.add("Serial_Number");

            rowdata = (Object[][]) xxx.xmlfeldolg(url, nodelist, lista);

        } catch (MalformedURLException ex) {
            Logger.getLogger(ablak.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        

    }

}
