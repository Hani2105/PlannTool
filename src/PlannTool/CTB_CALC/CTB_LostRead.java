/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool.CTB_CALC;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author gabor_hanacsek
 */
//megprobáljuk beolvasni a lostfilet és betölteni a táblába az adatait
public class CTB_LostRead {

    public void olvas() throws IOException {

        //beolvassuk az ini file-t
        String filepath = "C:\\Users\\" + System.getProperty("user.name") + "\\";
        File file = new File(filepath + "CTB_Lost.ini");
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (Exception e) {

            return;
        }
        String line = null;
        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) CTB.jTable12.getModel();
        model.setRowCount(0);

        while ((line = br.readLine()) != null) {

            String adatok[] = line.split(";");
            model.addRow(adatok);

        }

        CTB.jTable12.setModel(model);

    }

}
