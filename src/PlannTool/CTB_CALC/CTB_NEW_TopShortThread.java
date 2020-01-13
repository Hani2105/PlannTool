/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool.CTB_CALC;

import static PlannTool.CTB_CALC.CTB.TablaOszlopSzelesseg;
import static PlannTool.CTB_CALC.CTB.jTable1;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author gabor_hanacsek
 */
public class CTB_NEW_TopShortThread extends Thread {
    
    int i = -1;
    String pn = "";
    
    public CTB_NEW_TopShortThread() {
        
    }
    
    public CTB_NEW_TopShortThread(int i) {
        
        this.i = i;
        
    }
    
    public CTB_NEW_TopShortThread(String pn) {
        
        this.pn = pn;
    }
    
    @Override
    public void run() {

//addig csináljuk míg a kijelölés nem egyezik azzal a pn-el amit felvettünk, do while mert egyszer le kő futnyi
        //felvesszük a pn-t
        //ha a keresőből indítottunk es onnan van a pn akkor mindekepp azt hasznaljuk       
        if (i > -1 && pn.equals("")) {
            
            try {
                pn = CTB.jTable11.getValueAt(i, 0).toString();
            } catch (Exception e) {
            }
        } else if (pn.equals("")) {
            try {
                pn = CTB.jTable11.getValueAt(CTB.jTable11.getSelectedRow(), 0).toString();
            } catch (Exception e) {
            }
        }

        //csinálunk egy arrayt a sorbarendezett partnumbereknek
        ArrayList<String[]> pnadatokarray = new ArrayList<>();
//meg kell keresni a pn-t a bommatrixban
        outerloop:
        for (int o = 0; o < CTB_NEW_Variables.calcbommodel.getColumnCount(); o++) {

//megkeressük a pn-t és ha egyezik akkor bejárjuk a sorokat
            if (CTB_NEW_Variables.calcbommodel.getColumnName(o).equals(pn)) {
                
                for (int s = 0; s < CTB_NEW_Variables.calcbommodel.getRowCount(); s++) {
                    //frissítjük a progressbart
                    CTB.jProgressBar2.setMaximum(CTB_NEW_Variables.calcbommodel.getRowCount() - 1);
                    CTB.jProgressBar2.setValue(s);
                    CTB.jProgressBar2.setString("Calculating " + CTB.jProgressBar2.getValue() + " / " + CTB.jProgressBar2.getMaximum());

//ha van valami írva a sorba akkor kiszámoljuk ,hogy mizu és betesszük az adatokat a tömbbe
                    try {
                        if (!CTB_NEW_Variables.calcbommodel.getValueAt(s, o).toString().equals("")) {
                            //az alapanyag
                            String comp = CTB_NEW_Variables.calcbommodel.getValueAt(s, 0).toString();
//létrehozunk egy String tömböt 10 elemeset
                            String[] pnadatok = new String[11];
//kiszámoljuk a ctb-t erre az anyagra
                            int possible = Integer.parseInt(CTB_NEW_Variables.calcbommodel.getValueAt(s, 8).toString()) / (int) (Double.parseDouble(CTB_NEW_Variables.calcbommodel.getValueAt(s, o).toString()));
//bepakoljuk az adatokat a tömbbe
                            //partnumber
                            pnadatok[0] = CTB_NEW_Variables.calcbommodel.getValueAt(s, 0).toString();
                            //qty
                            pnadatok[1] = CTB_NEW_Variables.calcbommodel.getValueAt(s, 8).toString();
                            //description
                            pnadatok[2] = CTB_NEW_Variables.calcbommodel.getValueAt(s, 1).toString();
                            //raw oh
                            pnadatok[3] = CTB_NEW_Variables.calcbommodel.getValueAt(s, 2).toString();
                            //master comment
                            pnadatok[4] = CTB_NEW_Variables.calcbommodel.getValueAt(s, 7).toString();
                            //opseq na ezt meg kell keresni az indented bom ban
                            //ez lesz az opseq string ha több oldalon épül
                            String opseq = "";
                            //ez lesz a suppl type
                            String supply = "";
                            for (int i = 0; i < CTB_NEW_Variables.indentedbommodel.getRowCount(); i++) {
//ha egyezik a comp és a pn az indented bom adataival akkor hozzáadjuk a stringhez az opseqet
                                if (pn.equals(CTB_NEW_Variables.indentedbommodel.getValueAt(i, 0).toString()) && comp.equals(CTB_NEW_Variables.indentedbommodel.getValueAt(i, 7).toString())) {
                                    
                                    opseq += CTB_NEW_Variables.indentedbommodel.getValueAt(i, 6).toString() + ",";
                                    supply = CTB_NEW_Variables.indentedbommodel.getValueAt(i, 11).toString();
                                }
                            }
                            //levágjuk az utolsó vesszőt
                            opseq = opseq.substring(0, opseq.length() - 1);
                            //végül hozzáadjuk a tömbhöz
                            pnadatok[5] = opseq;
                            //a supplyt is
                            pnadatok[6] = supply;
                            //beépülés
                            pnadatok[7] = CTB_NEW_Variables.calcbommodel.getValueAt(s, o).toString();
                            //lost
                            pnadatok[8] = CTB_NEW_Variables.calcbommodel.getValueAt(s, 5).toString();
                            //horizontal
                            pnadatok[9] = CTB_NEW_Variables.calcbommodel.getValueAt(s, 6).toString();
                            //a végére , hogy mennyi gyártható az anyag miatt , ez fogja megadni a sorrendet
                            pnadatok[10] = String.valueOf(possible);

                            //betesszük a listbe
                            pnadatokarray.add(pnadatok);
                            
                        }
                    } catch (Exception e) {
                    }
                    
                }
                break outerloop;
            }
        }
//be kell rendezni sorba az adatokat

        for (int i = pnadatokarray.size() - 1; 0 < i; --i) {
            for (int j = 0; j < i; ++j) {
                if (Integer.parseInt(pnadatokarray.get(j)[10].toString()) > Integer.parseInt(pnadatokarray.get(j + 1)[10].toString())) {
                    // csere
                    String[] tempstring = new String[11];
                    tempstring[0] = pnadatokarray.get(j)[0];
                    tempstring[1] = pnadatokarray.get(j)[1];
                    tempstring[2] = pnadatokarray.get(j)[2];
                    tempstring[3] = pnadatokarray.get(j)[3];
                    tempstring[4] = pnadatokarray.get(j)[4];
                    tempstring[5] = pnadatokarray.get(j)[5];
                    tempstring[6] = pnadatokarray.get(j)[6];
                    tempstring[7] = pnadatokarray.get(j)[7];
                    tempstring[8] = pnadatokarray.get(j)[8];
                    tempstring[9] = pnadatokarray.get(j)[9];
                    tempstring[10] = pnadatokarray.get(j)[10];
                    
                    pnadatokarray.set(j, pnadatokarray.get(j + 1));
                    pnadatokarray.set(j + 1, tempstring);
                    
                }
            }
        }

//bellítjuk a shortmodellbe ezt az adathalmazt
        CTB_NEW_Variables.topshortmodel = (DefaultTableModel) CTB.jTable9.getModel();
        CTB_NEW_Variables.topshortmodel.setRowCount(0);
        
        for (int i = 0; i < pnadatokarray.size(); i++) {
            
            CTB_NEW_Variables.topshortmodel.addRow(new Object[]{pnadatokarray.get(i)[0], pnadatokarray.get(i)[1], pnadatokarray.get(i)[2], pnadatokarray.get(i)[3], pnadatokarray.get(i)[4], pnadatokarray.get(i)[5], pnadatokarray.get(i)[6], pnadatokarray.get(i)[7], pnadatokarray.get(i)[8], pnadatokarray.get(i)[9]});
            
        }

        //false ra allitjuk a globalis valtozot , hogy most epp nem fut szal
        CTB.jTable9.setModel(CTB_NEW_Variables.topshortmodel);
        TablaOszlopSzelesseg(CTB.jTable9);
        CTB.jProgressBar2.setMaximum(0);
        CTB.jProgressBar2.setValue(0);
        CTB.jProgressBar2.setString("Finish!");
        
    }
    
}
