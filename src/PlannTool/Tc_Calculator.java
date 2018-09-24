/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author gabor_hanacsek
 */
public class Tc_Calculator {

    Tc_Besheet b;
    Tc_Betervezo bt;

    public Tc_Calculator(Tc_Besheet b, Tc_Betervezo bt) {
        this.b = b;
        this.bt = bt;
        String sheetname = Tc_Betervezo.jTabbedPane1.getTitleAt(Tc_Betervezo.jTabbedPane1.getSelectedIndex());

        List<String> wsek = new ArrayList<String>();
        boolean irtunke = false;

        //felvesszuk a tabla adatait
        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) b.jTable2.getModel();

        //kiszedjuk az info sorokat
        for (int i = 0; i < model.getRowCount(); i++) {

            if (model.getValueAt(i, 3).toString().equals("Infó")) {

                model.removeRow(i);
                i = i - 1;

            }

        }

        //felvesszuk a ws eket elore uj sorkent
        for (int i = 0; i < model.getRowCount(); i++) {

            try {
                
                //akkor ha nem infó , nem üres és nem null az értéke
                if (!model.getValueAt(i, 3).toString().equals("Infó") && !model.getValueAt(i, 2).toString().equals("") && model.getValueAt(i, 2) != null) {

                    String ws = model.getValueAt(i, 2).toString();

                    for (int n = 0; n < wsek.size(); n++) {

                        if (ws.equals(wsek.get(n))) {

                            irtunke = true;

                        }

                    }

                    if (irtunke == false) {

                        wsek.add(ws);
                        model.insertRow(0, new Object[]{null, null, ws, "Infó"});

                    }

                    irtunke = false;
                }

            } catch (Exception e) {
            };

        }

        //szamoljuk az idoket
        //oszlopszor
        for (int o = 4; o < model.getColumnCount(); o++) {

            //sorszor
            for (int s = 0; s < model.getRowCount(); s++) {

                // ha infó a sor    
                if (model.getValueAt(s, 3).toString().equals("Infó")) {
                    //felvesszük a workstation
                    String ws = model.getValueAt(s, 2).toString();
                    Double tervido = 0.0;
                    Double tenyido = 0.0;

                    //bejarjuk a sorokat
                    for (int i = 0; i < model.getRowCount(); i++) {

                        //ha nem null az ertek és egyezik a ws felvesszuk darabszamnak
                        if (model.getValueAt(i, o) != null && ws.equals(model.getValueAt(i, 2)) && !model.getValueAt(i, 3).toString().equals("Infó")) {

                            double dbo = 0;
                            double db = 0;
                            boolean inte = true;
                            int eddigmenni = 1;

                            //kiszedjuk a stringbol az integert
                            while (inte) {
                                try {
                                    db = Double.parseDouble(model.getValueAt(i, o).toString().substring(0, eddigmenni));
                                    eddigmenni++;
                                } catch (Exception e) {

                                    inte = false;

                                }
                            }

                            String pn = model.getValueAt(i, 0).toString();

                            //hibaüzit kiiratjuk ha nincs a pn hez ws a cellaban
                            boolean hiba = true;

                            //megkeressuk a darab/orat
                            for (int n = 0; n < Tc_Besheet.ciklusidok.get(0).length; n++) {

                                if (Tc_Besheet.ciklusidok.get(0)[n][0].equals(sheetname) && Tc_Besheet.ciklusidok.get(0)[n][1].equals(pn) && Tc_Besheet.ciklusidok.get(0)[n][2].equals(ws)) {

                                    dbo = Double.parseDouble(Tc_Besheet.ciklusidok.get(0)[n][3]);
                                    hiba = false;

                                }

                            }
                            //ha nem volt a pn hez ws hibát írunk
                            if (hiba == true && model.getValueAt(i, 3).toString().equals("Terv")) {

                                infobox info = new infobox();
                                info.infoBox("Nincs mgeadva ciklusidő a \n " + pn + " -hez a " + ws + " - állomáson! \n Kérlek vidd fel az adatbázisba!", "Hiba!");

                            }

                            //terv vagy teny
                            if (model.getValueAt(i, 3).toString().equals("Terv")) {

                                tervido += db / dbo;

                            }
                            if (model.getValueAt(i, 3).toString().equals("Tény")) {

                                tenyido += db / dbo;

                            }

                        }

                    }

                    //kiirjuk a cellaba
                    //csak 2 tizedesig írunk
                    DecimalFormat df2 = new DecimalFormat("#.##");
                    model.setValueAt("Terv: " + df2.format(tervido) + " " + "Tény: " + df2.format(tenyido), s, o);

                } else {
                    continue;
                };

            }

        }

        this.b.jTable2.setModel(model);

    }
}
