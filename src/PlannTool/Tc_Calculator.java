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
import javax.swing.table.TableColumn;

/**
 *
 * @author gabor_hanacsek
 */
public class Tc_Calculator {

    Tc_Besheet b;

    public Tc_Calculator(Tc_Besheet b) {

        this.b = b;

        String sheetname = "";
        try {
            sheetname = Tc_Betervezo.Tervezotabbed.getTitleAt(Tc_Betervezo.Tervezotabbed.getSelectedIndex());
        } catch (Exception e) {
        }

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
                this.b.jTable2.setColumnSelectionInterval(this.b.jTable2.getSelectedColumn(), this.b.jTable2.getSelectedColumn());
                this.b.jTable2.setRowSelectionInterval(this.b.jTable2.getSelectedRow() + 1, this.b.jTable2.getSelectedRow() + 1);

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
                        this.b.jTable2.setColumnSelectionInterval(this.b.jTable2.getSelectedColumn(), this.b.jTable2.getSelectedColumn());
                        this.b.jTable2.setRowSelectionInterval(this.b.jTable2.getSelectedRow() - 1, this.b.jTable2.getSelectedRow() - 1);

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

                            try {
                                String pn = model.getValueAt(i, 0).toString();

                                //hibaüzit kiiratjuk ha nincs a pn hez ws a cellaban
                                boolean hiba = true;

                                //megkeressuk a darab/orat
                                for (int n = 0; n < Tc_Betervezo.ciklusidok.get(0).length; n++) {

                                    if (Tc_Betervezo.ciklusidok.get(0)[n][0].equals(sheetname) && Tc_Betervezo.ciklusidok.get(0)[n][1].equals(pn) && Tc_Betervezo.ciklusidok.get(0)[n][2].equals(ws)) {

                                        dbo = Double.parseDouble(Tc_Betervezo.ciklusidok.get(0)[n][3]);
                                        hiba = false;

                                    }

                                }
                            } catch (Exception e) {

                            }
                            //ha nem volt a pn hez ws hibát írunk
//                            if (hiba == true && model.getValueAt(i, 3).toString().equals("Terv")) {
//
//                                infobox info = new infobox();
//                                info.infoBox("Nincs mgeadva ciklusidő a \n " + pn + " -hez a " + ws + " - állomáson! \n Kérlek vidd fel az adatbázisba!", "Hiba!");
//
//                            }

                            //terv vagy teny es a dbo nagyobb nulla
                            if (model.getValueAt(i, 3).toString().equals("Terv") && dbo > 0) {

                                tervido += db / dbo;

                            }
                            if (model.getValueAt(i, 3).toString().equals("Tény") && dbo > 0) {

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

//egy utolso oszlopot hozunk letre amiben a summakat kiiratjuk ha még nem az
        if (!model.getColumnName(model.getColumnCount() - 1).equals("Sum: PN,JOB,WS")) {
            model.addColumn("Sum: PN,JOB,WS");
        }

        //ha soronkénti összeadás van
        if (Tc_Betervezo.calc == 1) {

            //összeadjuk a darabot a tt oszloptól a summa oszlopig ha nem infó a sor
            try {
                for (int r = 0; r < model.getRowCount(); r++) {

                    //ebbe számolunk
                    int qty = 0;

                    if (!model.getValueAt(r, 3).toString().equals("Infó")) {

                        //bejárjuk az oszlopokat 4- utolsó előttig
                        for (int i = 4; i < model.getColumnCount(); i++) {

                            //ha nagyobb mint nulla a stringől int és nem summa az osulop
                            try {
                                if (new Tc_Stringbolint(model.getValueAt(r, i).toString()).db > 0 && !model.getColumnName(i).equals("Sum: PN,JOB,WS")) {
                                    //kiszedjuk a stringű az intete

                                    qty += new Tc_Stringbolint(model.getValueAt(r, i).toString()).db;

                                }
                            } catch (Exception e) {

                            }
                        }

                        model.setValueAt(qty, r, model.getColumnCount() - 1);

                    }

                }
            } catch (Exception e) {
            }

        } //ha össz összeadás van!
        else if (Tc_Betervezo.calc
                == 2) {
            //bejarjuk a sorokat , felvesszuk a ws-t , a job -ot es a pn-t , terv/tenyt , darabszamot
            String pn = "";
            String job = "";
            String ws = "";
            String tt = "";
            int qty = 0;

            for (int i = 0; i < model.getRowCount(); i++) {

                //felvesszuk az ertekeket (ha nem info a sor) 
                try {
                    if (!model.getValueAt(i, 3).toString().equals("Infó")) {
                        pn = model.getValueAt(i, 0).toString();
                        job = model.getValueAt(i, 1).toString();
                        ws = model.getValueAt(i, 2).toString();
                        tt = model.getValueAt(i, 3).toString();
                        qty = 0;

                        //elinditjuk a ket kisebb ciklust , hogy osszeszamoljuk a darabokat
                        for (int r = 0; r < model.getRowCount(); r++) {

                            for (int o = 4; o < model.getColumnCount(); o++) {

                                //kinullazzuk a darabot
                                //ha egyezik az aktuális sorban a job ws pn és tt és nem üres és a szamolo nagyobb nulla és nem a summa oszlop 
                                try {
                                    if (model.getValueAt(r, 0).toString().equals(pn) && model.getValueAt(r, 1).toString().equals(job) && model.getValueAt(r, 2).toString().equals(ws)
                                            && model.getValueAt(r, 3).toString().equals(tt) && model.getValueAt(r, o) != null && new Tc_Stringbolint(model.getValueAt(r, o).toString()).db > 0 && !model.getColumnName(o).equals("Sum: PN,JOB,WS")) {

                                        //itt jon a stringbol kiszedjuk a qty-t resz
                                        //Tc_Stringbolint tc = new Tc_Stringbolint();
                                        qty += new Tc_Stringbolint(model.getValueAt(r, o).toString()).db;

                                    }
                                } catch (Exception e) {

                                    qty = 0;
                                }

                            }

                        }

                    }
                } catch (Exception e) {
                }

                //ha lefutott a ciklus beírjuk az utolsó oszlopba
                model.setValueAt(qty, i, model.getColumnCount() - 1);

            }

        } //Ha az adott időpontig vagyunk kiváncsiak az eredményre!
        else if (Tc_Betervezo.calc
                == 3) {
            //bejarjuk a sorokat , felvesszuk a ws-t , a job -ot es a pn-t , terv/tenyt , darabszamot
            String pn = "";
            String job = "";
            String ws = "";
            String tt = "";
            int qty = 0;

            for (int i = 0; i <= b.jTable2.getSelectedRow(); i++) {

                //felvesszuk az ertekeket (ha nem info a sor) 
                try {
                    if (!model.getValueAt(i, 3).toString().equals("Infó")) {
                        pn = model.getValueAt(i, 0).toString();
                        job = model.getValueAt(i, 1).toString();
                        ws = model.getValueAt(i, 2).toString();
                        tt = model.getValueAt(i, 3).toString();
                        qty = 0;

                        //elinditjuk a ket kisebb ciklust , hogy osszeszamoljuk a darabokat
                        for (int r = 0; r <= b.jTable2.getSelectedRow() + 1; r++) {
                            //csak a kijelölt oszlopig megyunk
                            for (int o = 4; o <= b.jTable2.getSelectedColumn(); o++) {

                                //kinullazzuk a darabot
                                //ha egyezik az aktuális sorban a job ws pn és tt és nem üres és a szamolo nagyobb nulla és nem a summa oszlop 
                                try {
                                    if (model.getValueAt(r, 0).toString().equals(pn) && model.getValueAt(r, 1).toString().equals(job) && model.getValueAt(r, 2).toString().equals(ws)
                                            && model.getValueAt(r, 3).toString().equals(tt) && model.getValueAt(r, o) != null && new Tc_Stringbolint(model.getValueAt(r, o).toString()).db > 0 && !model.getColumnName(o).equals("Sum: PN,JOB,WS")) {

                                        //itt jon a stringbol kiszedjuk a qty-t resz
                                        //Tc_Stringbolint tc = new Tc_Stringbolint();
                                        qty += new Tc_Stringbolint(model.getValueAt(r, o).toString()).db;

                                    }
                                } catch (Exception e) {

                                }

                            }

                        }

                        //ha lefutott a ciklus beírjuk az utolsó oszlopba
                        model.setValueAt(qty, i, model.getColumnCount() - 1);

                    }
                } catch (Exception e) {
                }

            }
            // a megadott oszlopon túli értékek legyenek nullásak
            try {
                for (int n = (b.jTable2.getSelectedRow() + 1); n < b.jTable2.getRowCount(); n++) {

                    model.setValueAt(0, n, model.getColumnCount() - 1);

                }
            } catch (Exception e) {
            }

        }

        this.b.jTable2.setModel(model);

        //szelesseg allitas ha engedelyezett
        TableColumn column = null;
        if (Tc_Betervezo.allitsuke
                == 1) {

            for (int i = 0; i < model.getColumnCount(); i++) {

                if (i != 3) {
                    column = b.jTable2.getColumnModel().getColumn(i);

                    column.setPreferredWidth(130);
                }

            }

        } //ha nem engedelyezett akkor a sliderek ertekei
        else if (Tc_Betervezo.allitsuke
                == 2) {

            for (int i = 0; i < model.getColumnCount(); i++) {

                if (i < 3) {
                    column = b.jTable2.getColumnModel().getColumn(i);
                    column.setPreferredWidth(Tc_Betervezo.slider2);
                } else if (i > 3) {

                    column = b.jTable2.getColumnModel().getColumn(i);
                    column.setPreferredWidth(Tc_Betervezo.slider1);

                }

            }
//ha manuális
        } else if (Tc_Betervezo.allitsuke
                == 0) {

            try {
                for (int i = 0; i < model.getColumnCount(); i++) {

                    column = b.jTable2.getColumnModel().getColumn(i);
                    column.setPreferredWidth(Tc_Betervezo.szelessegek.get(i));

                }
            } catch (Exception e) {
            }

        }
    }

}
