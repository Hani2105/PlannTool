/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool.CTB_CALC;

import PlannTool.BACKEND.Tc_Betervezo;
import PlannTool.BACKEND.Tc_Stringbolint;
import static PlannTool.CTB_CALC.CTB.SelectedBecells;
import static PlannTool.CTB_CALC.CTB.TablaOszlopSzelesseg;
import static PlannTool.CTB_CALC.CTB.jCheckBoxMenuItem1;
import static PlannTool.CTB_CALC.CTB.jCheckBoxMenuItem2;
import static PlannTool.CTB_CALC.CTB.jScrollPane1;
import static PlannTool.CTB_CALC.CTB.jScrollPane11;
import static PlannTool.CTB_CALC.CTB.jTable1;
import static PlannTool.CTB_CALC.CTB.jTable11;
import static PlannTool.CTB_CALC.CTB.jTable6;
import static PlannTool.CTB_CALC.CTB.jTable7;
import static PlannTool.CTB_CALC.CTB.jTable9;

import static PlannTool.CTB_CALC.CTB.tervido;
import PlannTool.ablak;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author gabor_hanacsek
 */
public class CTB_NEW_FullCalc extends Thread {

    private static boolean c;
    private static int osszeg = 0;
    private static String comp = "";
    private static calculations k;

    public CTB_NEW_FullCalc(boolean compare, calculations kalk) {
        CTB_NEW_FullCalc.c = compare;
        CTB_NEW_FullCalc.k = kalk;

    }

    public enum calculations {
        FULL, CHANGEOH, CHANGELOST
    };

    @Override
    public void run() {

//ne szeresztodjun a jtable1
        stopEdit();
//felvesszük a táblákat 
        getTables();
        switch (k) {

            case FULL:

                if (!c) {

                    //felépítjük a ctb táblát a terv tábla alapján
                    build();
                    //kiszámoljuk bele a nyitott po-kat
                    openPoCalc();
                    //kiszámoljuk a raktárkészletet a késztermékre!!!!
                    stockCalc();
                    //kiszámoljuk mennyi nyitott job van a termékre
                    openOrderCalc();
                    //összeállítjuk a bom matrixot
                    bomCalc();
                    //az oh beetöltése
                    addOh();
                    //az allokációk összeszedése
                    addAlloc();
                    //a lost mennyiség belekalkulálása ha kell
                    addLost(CTB.jCheckBoxMenuItem10.isSelected());
                    //lekérjük a pn adatokat
                    CTB_PnDatas p = new CTB_PnDatas();
                    try {
                        p.adatleker();
                    } catch (SQLException ex) {
                        Logger.getLogger(CTB_NEW_FullCalc.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(CTB_NEW_FullCalc.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    //statisztika küldése
                    ablak.stat.beir(System.getProperty("user.name"), "CTB kapcsolodas!", "", "gabor.hanacsek@sanmina.com");
                }
                //a betervezett mennyiség a backend sheetekről
                getPlannQty();
                //ennyit kell még tervezni a visszaigazolásig
                needToPlan();
                //ez adja meg , hogy mennyit kell még betervezni
                needToBuild();
                //hozzáadjuk a horizontalt, ha jcheck9 ki van pipálva
                addHori(CTB.jCheckBoxMenuItem9.isSelected());

                //ez számolja ki a plann mennyiség anyagigényét a calcbom tablaba
                calcPlannCompQty();
                //ki kell szamolni a total oh-t
                totalOhCalc();
                //ez megadja , hogy mennyira vagyunk ctb-k az adott pn ből
                ctbCalc();

                break;

            case CHANGEOH:

                addOh();
                totalOhCalc();
                ctbCalc();
                break;
            case CHANGELOST:

                addLost(CTB.jCheckBoxMenuItem10.isSelected());
                totalOhCalc();
                ctbCalc();
                break;

        }

        //a táblák szélességének beállítása
        TablaOszlopSzelesseg(jTable1);
        TablaOszlopSzelesseg(jTable11);

        //update progressbar
        CTB.updateBar("Finish!", 0, 0);
        //topshort megmutatása
        CTB_NEW_TopShortThread t = new CTB_NEW_TopShortThread();
        t.start();

    }

    public void getTables() {

        CTB_NEW_Variables.lostmodel = (DefaultTableModel) CTB.jTable12.getModel();
        CTB_NEW_Variables.allocmodel = (DefaultTableModel) CTB.jTable3.getModel();
        CTB_NEW_Variables.horizontalmodel = (DefaultTableModel) CTB.jTable13.getModel();
        CTB_NEW_Variables.tervtablemodel = (DefaultTableModel) CTB.jTable11.getModel();
        CTB_NEW_Variables.demandmodel = (DefaultTableModel) CTB.jTable4.getModel();
        CTB_NEW_Variables.ohmodel = (DefaultTableModel) CTB.jTable2.getModel();
        CTB_NEW_Variables.womodel = (DefaultTableModel) CTB.jTable5.getModel();
        CTB_NEW_Variables.ctbmodel = (DefaultTableModel) CTB.jTable1.getModel();
        CTB_NEW_Variables.calcbommodel = (DefaultTableModel) jTable7.getModel();
        CTB_NEW_Variables.indentedbommodel = (DefaultTableModel) jTable6.getModel();

    }

    public void stopEdit() {

        if (jTable11.isEditing()) {

            jTable11.getCellEditor().stopCellEditing();

        }

    }

    public void bomCalc() {

//a késztermék számok lesznek
        String pn;
//kinullazzuk a sorokat es oszlopokat
        CTB_NEW_Variables.calcbommodel.setRowCount(0);
        CTB_NEW_Variables.calcbommodel.setColumnCount(9);

//hozzáadjuk columnként a pn eket
        for (int i = 0; i < CTB_NEW_Variables.ctbmodel.getRowCount(); i++) {

            CTB_NEW_Variables.calcbommodel.addColumn(CTB_NEW_Variables.ctbmodel.getValueAt(i, 0).toString());

        }

//beallitjuk a valtozot , hogy csak az aktualis szintre nezzunk e ctb-t vagy vilagitsuk e at az egeszet, de ha ki van pipálva a +sa akkor mehet bele az is
//ha az egeszet nezzük , ki kell venni az sa-kat!!! , ha nem , akkor maradhatnak de csak level 1 kell!!
        int lvl = 100;
        String sa = "SA";
        if (CTB.jCheckBoxMenuItem6.isSelected()) {

            lvl = 1;
            sa = "kiskutyafüle";

        }

//miután ez megvan végigmegyünk rajta oszlopszor és megpróbáljuk megkereseni az adott oszlop pn-ét az intended bom ban,
        for (int o = 7; o < CTB_NEW_Variables.calcbommodel.getColumnCount(); o++) {
//a progressbar frissítése
//CTB.jProgressBar1.setValue(o);
            CTB.updateBar("BOMCALC", CTB_NEW_Variables.calcbommodel.getColumnCount() - 1, o);
//felvesszük pn-nek a pn-t :p
            pn = CTB_NEW_Variables.calcbommodel.getColumnName(o).trim();

            for (int r = 0; r < CTB_NEW_Variables.indentedbommodel.getRowCount(); r++) {

//ha egyezik a pn az intended bom pn -ével és van ora type
                if ((pn.equals(CTB_NEW_Variables.indentedbommodel.getValueAt(r, 0).toString().trim()) && !CTB_NEW_Variables.indentedbommodel.getValueAt(r, 8).toString().equals(sa) && !CTB_NEW_Variables.indentedbommodel.getValueAt(r, 5).toString().equals("0") && Integer.parseInt(CTB_NEW_Variables.indentedbommodel.getValueAt(r, 5).toString()) <= lvl) || (CTB.jCheckBoxMenuItem13.isSelected() && pn.equals(CTB_NEW_Variables.indentedbommodel.getValueAt(r, 0).toString().trim())) && !CTB_NEW_Variables.indentedbommodel.getValueAt(r, 5).toString().equals("0") && Integer.parseInt(CTB_NEW_Variables.indentedbommodel.getValueAt(r, 5).toString()) <= lvl) {

//itt megvizsgáljuk , hogy kell a a phantom item vagy nem
//ha nincs kipipálva az azt jelenti , hogy nem kell beletenni , ergo ha az akkor ugrunk a ciklusban
                    if (!jCheckBoxMenuItem1.isSelected() && CTB_NEW_Variables.indentedbommodel.getValueAt(r, 8).toString().equals("PH")) {

                        continue;

                    }

//itt megvizsgaljuk , hogy kell e a bulk
//ha nincs kipipálva az azt jelenti , hogy nem kell vele számolni , ergo ha az akkor ugrunk a ciklusban
                    if (!jCheckBoxMenuItem2.isSelected() && CTB_NEW_Variables.indentedbommodel.getValueAt(r, 11).toString().equals("Bulk")) {

                        continue;

                    }

//ha eddig eljutunk
//felvesszük a componentet stringnek
                    comp = CTB_NEW_Variables.indentedbommodel.getValueAt(r, 7).toString().trim();
//akkor megvizsgáljuk , hogy a componentet felvettük e már sorba a modellbe
//kell egy boolean , hogy kell e uj sort létrehozni
                    boolean ujsor = true;
                    for (int i = 0; i < CTB_NEW_Variables.calcbommodel.getRowCount(); i++) {
//ha találunk olyan sort akkor oda írunk, de úgy, hogy hozzáadjuk a mennyiséget a már meglévőhöz!!!
                        if (CTB_NEW_Variables.calcbommodel.getValueAt(i, 0).toString().equals(comp)) {
//ez a változója az eddig felvett emnnyiségeknek

                            double osszeg = 0;
                            try {
                                osszeg = Double.parseDouble(CTB_NEW_Variables.calcbommodel.getValueAt(i, o).toString());
                            } catch (Exception e) {
                            }

                            CTB_NEW_Variables.calcbommodel.setValueAt(Double.parseDouble(CTB_NEW_Variables.indentedbommodel.getValueAt(r, 13).toString()) + osszeg, i, o);
                            CTB_NEW_Variables.calcbommodel.setValueAt(CTB_NEW_Variables.indentedbommodel.getValueAt(r, 9).toString(), i, 1);
                            ujsor = false;

                            break;
                        }
//ha nem találunk akkor hozzá kell adni egy sort

                    }
//ha nem írtunk , ezért kell egy uj sort hozzáadni akkor ezt tesszük
                    if (ujsor) {

                        CTB_NEW_Variables.calcbommodel.addRow(new Object[]{"0", "0", "0", "0", "0", "0", "0", "", "0"});
                        CTB_NEW_Variables.calcbommodel.setValueAt(comp, CTB_NEW_Variables.calcbommodel.getRowCount() - 1, 0);
                        CTB_NEW_Variables.calcbommodel.setValueAt(CTB_NEW_Variables.indentedbommodel.getValueAt(r, 13).toString(), CTB_NEW_Variables.calcbommodel.getRowCount() - 1, o);
                        CTB_NEW_Variables.calcbommodel.setValueAt(CTB_NEW_Variables.indentedbommodel.getValueAt(r, 9).toString(), CTB_NEW_Variables.calcbommodel.getRowCount() - 1, 1);
                        int sor = CTB_NEW_Variables.calcbommodel.getRowCount() - 1;
                        //a beépülők lesznek

                    }

                }

            }
        }

        CTB.jTable7.setModel(CTB_NEW_Variables.calcbommodel);
        TablaOszlopSzelesseg(CTB.jTable7);

    }

    public void totalOhCalc() {
        for (int i = 0; i < CTB_NEW_Variables.calcbommodel.getRowCount(); i++) {
            //update progressbar
            CTB.updateBar("TOTALOHCALC", CTB_NEW_Variables.calcbommodel.getRowCount() - 1, i);
//négy különböző eset lehet
            int osszeg = 0;
//ha mindennel számolunk , lost és horizontallal is
            if (CTB.jCheckBoxMenuItem7.isSelected() && CTB.jCheckBoxMenuItem8.isSelected()) {
                osszeg = Integer.parseInt(CTB_NEW_Variables.calcbommodel.getValueAt(i, 2).toString().trim()) - Integer.parseInt(CTB_NEW_Variables.calcbommodel.getValueAt(i, 3).toString().trim()) - Integer.parseInt(CTB_NEW_Variables.calcbommodel.getValueAt(i, 4).toString().trim()) - Integer.parseInt(CTB_NEW_Variables.calcbommodel.getValueAt(i, 5).toString().trim()) + Integer.parseInt(CTB_NEW_Variables.calcbommodel.getValueAt(i, 6).toString().trim());
            }
//ha csak a lostalszamolunk
            if (CTB.jCheckBoxMenuItem7.isSelected() && !CTB.jCheckBoxMenuItem8.isSelected()) {
                osszeg = Integer.parseInt(CTB_NEW_Variables.calcbommodel.getValueAt(i, 2).toString().trim()) - Integer.parseInt(CTB_NEW_Variables.calcbommodel.getValueAt(i, 3).toString().trim()) - Integer.parseInt(CTB_NEW_Variables.calcbommodel.getValueAt(i, 4).toString().trim()) - Integer.parseInt(CTB_NEW_Variables.calcbommodel.getValueAt(i, 5).toString().trim());
            }
//ha csaka horizontallal szamolunk
            if (!CTB.jCheckBoxMenuItem7.isSelected() && CTB.jCheckBoxMenuItem8.isSelected()) {
                osszeg = Integer.parseInt(CTB_NEW_Variables.calcbommodel.getValueAt(i, 2).toString().trim()) - Integer.parseInt(CTB_NEW_Variables.calcbommodel.getValueAt(i, 3).toString().trim()) - Integer.parseInt(CTB_NEW_Variables.calcbommodel.getValueAt(i, 4).toString().trim()) + Integer.parseInt(CTB_NEW_Variables.calcbommodel.getValueAt(i, 6).toString().trim());
            }
//ha semmivel semszamolunk
            if (!CTB.jCheckBoxMenuItem7.isSelected() && !CTB.jCheckBoxMenuItem8.isSelected()) {
                osszeg = Integer.parseInt(CTB_NEW_Variables.calcbommodel.getValueAt(i, 2).toString().trim()) - Integer.parseInt(CTB_NEW_Variables.calcbommodel.getValueAt(i, 3).toString().trim()) - Integer.parseInt(CTB_NEW_Variables.calcbommodel.getValueAt(i, 4).toString().trim());
            }
//végül beállítjuk a calohba
            CTB_NEW_Variables.calcbommodel.setValueAt(osszeg, i, 8);
        }
    }

    public void calcPlannCompQty() {
//meg kell nézni , hogy az ujpn hez tartozo kesztermekszamhoz van e terv mennyiség beírva
//tekerjük az összerakott bom táblát és ha megtaláljuk benne a pn-t akkor végigmegyünk az oszlopokon és kiszedjük az oszlopneveket majd összehasonlítjuk a ctbmodel terv részével h van e oda írva

        for (int b = 0; b < CTB_NEW_Variables.calcbommodel.getRowCount(); b++) {
            //update progressbar
            CTB.updateBar("CALCPLANNCOMPQTY", CTB_NEW_Variables.calcbommodel.getRowCount() - 1, b);
            int osszeg = 0;

            for (int c = 0; c < CTB_NEW_Variables.calcbommodel.getColumnCount(); c++) {

                String termek = CTB_NEW_Variables.calcbommodel.getColumnName(c).toString();
//megnezzuk , hogy van e terv mennyiseg
                for (int ctb = 0; ctb < CTB_NEW_Variables.ctbmodel.getRowCount(); ctb++) {
                    try {
                        if (termek.equals(CTB_NEW_Variables.ctbmodel.getValueAt(ctb, 0)) && Integer.parseInt(CTB_NEW_Variables.ctbmodel.getValueAt(ctb, 6).toString()) > 0) {
//ha van akkor kiszedjük mint szorzót 
                            int darabszam = Integer.parseInt(CTB_NEW_Variables.ctbmodel.getValueAt(ctb, 6).toString());
                            try {
                                osszeg += darabszam * (Math.round(Float.valueOf(CTB_NEW_Variables.calcbommodel.getValueAt(b, c).toString())));
                            } catch (Exception e) {
                            }
                        }

                    } catch (Exception e) {
                    }
                }

            }
            //beírjuk az ohmodellbe az összeget
            CTB_NEW_Variables.calcbommodel.setValueAt(osszeg, b, 4);

        }

    }

    public void ctbCalc() {
        String pn = "";
//meg kell keresnia legkisebb szamot ami fogja a termeket
//végigmegyünk a ctb modellen es kivesszük a kesztermekpn-t
        outerloop:
        for (int i = 0; i < CTB_NEW_Variables.ctbmodel.getRowCount(); i++) {
            //update progressbar
            CTB.updateBar("CTBCALC", CTB_NEW_Variables.ctbmodel.getRowCount() - 1, i);
            pn = CTB_NEW_Variables.ctbmodel.getValueAt(i, 0).toString();
//elkezdjuk bejarni a bommatrixot ez utan a pn utan kutatva
            for (int bo = 9; bo < CTB_NEW_Variables.calcbommodel.getColumnCount(); bo++) {
//a legkisebb gyártható szám
                int less = 999999999;
//ha megtaláljuk a pn-t mert egyezik a column name vele akkor bejárjuk asorokat és matekozunk
                if (CTB_NEW_Variables.calcbommodel.getColumnName(bo).equals(pn)) {

                    for (int bs = 0; bs < CTB_NEW_Variables.calcbommodel.getRowCount(); bs++) {
//ha van valami írva az adott orba akkor matekozunk
                        try {

                            int possible = Integer.parseInt(CTB_NEW_Variables.calcbommodel.getValueAt(bs, 8).toString()) / (int) (Double.parseDouble(CTB_NEW_Variables.calcbommodel.getValueAt(bs, bo).toString()));

                            if (less > possible) {
                                less = possible;
                            }

                        } catch (Exception e) {

                        }

                    }
//ha végigértünk a soron akkor beírjuk a ctb modellbe a legkisebb eredmenyt es ugrunk a következő pn re

//ha a less 9999999 akkor beírjuk , hogy bomhiba
                    if (less == 999999999) {
                        CTB_NEW_Variables.ctbmodel.setValueAt("BOM hiba!", i, 5);
                    } else {
                        CTB_NEW_Variables.ctbmodel.setValueAt(less, i, 5);
                    }
                    continue outerloop;
                }

            }

        }

    }

    public void getPlannQty() {

//vegigfutunk a listan és megprobálunk keresni ilyen tabot a backendtervezőben
        for (int i = 0; i < SelectedBecells.size(); i++) {
            //update progress bar
            CTB.updateBar("GETPLANN", SelectedBecells.size() - 1, i);
            try {
//áttesszük egy modelbe az adatokat
                DefaultTableModel model = new DefaultTableModel();
                model = (DefaultTableModel) Tc_Betervezo.Besheets.get(SelectedBecells.get(i)).jTable2.getModel();
                int elsooszlop = 0;
//megkeressük az első oszlopot ahonnan el kell indulni számolni a tervet
                for (int c = 0; c < model.getColumnCount(); c++) {

                    if (model.getColumnName(c).contains(tervido) && !tervido.equals("YYYY-MM-DD hh:mm:ss")) {

                        elsooszlop = c;
                        break;

                    }

                }

//innen bejárjuk a táblát jobbra és lefelé (figyelni a kalkulált oszlopra!!) és megpróbáljuk megkeresni a plan táblában szereplő pn eket ha nagyobb mint 0 az első oszlop!!
                if (elsooszlop > 0) {
                    for (int p = 0; p < CTB.jTable11.getRowCount(); p++) {
                        String partnumber = CTB.jTable11.getValueAt(p, 0).toString();
                        int osszeg = 0;
                        for (int r = 0; r < model.getRowCount(); r++) {
//ha olyan sorba érünk ahol egyezik a pn (model 0) és nem tény a sor akkor bejárjuk az oszlopokat és megprobáljuk kiszedni az összeget
                            try {
                                if (partnumber.equals(model.getValueAt(r, 0).toString()) && !model.getValueAt(r, 3).toString().equals("Tény")) {

                                    for (int c = elsooszlop; c < model.getColumnCount() - 1; c++) {

                                        osszeg += new Tc_Stringbolint(model.getValueAt(r, c).toString()).db;

                                    }

                                }

                            } catch (Exception e) {

                            }

                        }
//beállítjuk a táblába

                        if (osszeg > 0) {

                            CTB.jTable11.setValueAt(osszeg, p, 1);

                        } else {
                            CTB.jTable11.setValueAt("", p, 1);
                        }

                    }
                }
            } catch (Exception e) {
            }
        }

    }

    public void needToPlan() {

        //ki kell számolni ,hogy mennyit kell még betervezni , ezt úgy tesszük , hogy kivonjuk a betervezett hetek összegéből a stockot , az open job-ot és a tervet
        for (int i = 0; i < CTB_NEW_Variables.tervtablemodel.getRowCount(); i++) {
            //update progressbar
            CTB.updateBar("NEEDTOPLAN", CTB_NEW_Variables.tervtablemodel.getRowCount() - 1, i);

            //ha van valami írva a nulladik oszlopba
            if (CTB_NEW_Variables.tervtablemodel.getValueAt(i, 0) != null) {
                int osszeg = 0;
                //bejárjuk oszlopszor
                for (int o = 4; o < CTB_NEW_Variables.tervtablemodel.getColumnCount(); o++) {
                    try {
                        osszeg += Integer.parseInt(CTB_NEW_Variables.tervtablemodel.getValueAt(i, o).toString());
                    } catch (Exception e) {
                    }

                }

                //megvan az összeg , akkor kivonjuk a cuccokat
                try {
                    osszeg = osszeg - Integer.parseInt(CTB_NEW_Variables.ctbmodel.getValueAt(i, 2).toString());
                } catch (Exception e) {
                }
                try {
                    osszeg = osszeg - Integer.parseInt(CTB_NEW_Variables.ctbmodel.getValueAt(i, 3).toString());
                } catch (Exception e) {
                }
                try {
                    osszeg = osszeg - Integer.parseInt(CTB_NEW_Variables.tervtablemodel.getValueAt(i, 1).toString());
                } catch (Exception e) {

                }

//kiszállítás kivonása
                try {
                    osszeg = osszeg - Integer.parseInt(CTB_NEW_Variables.tervtablemodel.getValueAt(i, 2).toString());
                } catch (Exception e) {

                }

                //beszetteljük az eredményt a táblába a need to planhez 
                CTB_NEW_Variables.tervtablemodel.setValueAt(osszeg, i, 3);

                int plan = 0;

                try {

                    plan = Integer.parseInt(CTB_NEW_Variables.tervtablemodel.getValueAt(i, 1).toString());

                } catch (Exception e) {

                }

//ha az összeg (needtoplan negatív , akkor csak a plannel számolunk)
                if (osszeg <= 0) {
                    try {
                        CTB_NEW_Variables.ctbmodel.setValueAt(plan, i, 6);
                    } catch (Exception e) {
                    }

                } //ellenkező esetben mindkettővel               
                else {
                    try {
                        CTB_NEW_Variables.ctbmodel.setValueAt(osszeg + plan, i, 6);
                    } catch (Exception e) {
                    }

                }

            }

        }

    }

    public void needToBuild() {

        for (int i = 0; i < CTB_NEW_Variables.ctbmodel.getRowCount(); i++) {
            //update progressbar
            CTB.updateBar("NEEDTOBUILD", CTB_NEW_Variables.ctbmodel.getRowCount() - 1, i);
            //openorder
            int openorder = 0;
            int stock = 0;
            int openjob = 0;
            int terv = 0;

            try {
                openorder = Integer.parseInt(CTB_NEW_Variables.ctbmodel.getValueAt(i, 1).toString());
            } catch (Exception e) {
            }
            try {
                stock = Integer.parseInt(CTB_NEW_Variables.ctbmodel.getValueAt(i, 2).toString());
            } catch (Exception e) {
            }
            try {
                openjob = Integer.parseInt(CTB_NEW_Variables.ctbmodel.getValueAt(i, 3).toString());
            } catch (Exception e) {
            }
            try {
                terv = Integer.parseInt(CTB_NEW_Variables.ctbmodel.getValueAt(i, 6).toString());
            } catch (Exception e) {
            }

            CTB_NEW_Variables.ctbmodel.setValueAt(openorder - (terv + openjob + stock), i, 4);

        }

    }

    public void openOrderCalc() {

        for (int i = 0; i < CTB_NEW_Variables.ctbmodel.getRowCount(); i++) {

            //update progress bar
            CTB.updateBar("JOBCALC", CTB_NEW_Variables.ctbmodel.getRowCount() - 1, i);
            int osszeg = 0;
//és végigszaladunk az OH táblán
            for (int d = 0; d < CTB_NEW_Variables.womodel.getRowCount(); d++) {
//ha egyezik a ctb tábla pn e az OH tábla pn-el 
                try {
                    if ((CTB_NEW_Variables.ctbmodel.getValueAt(i, 0).toString().equals(CTB_NEW_Variables.womodel.getValueAt(d, 1).toString()) && (CTB_NEW_Variables.womodel.getValueAt(d, 8).toString().contains("Released")))) {

                        osszeg += Integer.parseInt(CTB_NEW_Variables.womodel.getValueAt(d, 17).toString());

                    }
                } catch (Exception e) {
                }

            }

            CTB_NEW_Variables.ctbmodel.setValueAt(osszeg, i, 3);

        }

        CTB.jTable1.setModel(CTB_NEW_Variables.ctbmodel);

    }

    public void build() {

        CTB_NEW_Variables.adatok = new String[jTable11.getRowCount()][2];
        //bejárjuk a táblát és feltöltjük a tömböt
        for (int i = 0; i < CTB_NEW_Variables.tervtablemodel.getRowCount(); i++) {
            //update progress bar
            CTB.updateBar("BUILD", CTB_NEW_Variables.tervtablemodel.getRowCount() - 1, i);
            if (CTB_NEW_Variables.tervtablemodel.getValueAt(i, 0) != null && !CTB_NEW_Variables.tervtablemodel.getValueAt(i, 0).toString().equals("")) {

                try {
                    CTB_NEW_Variables.adatok[i][0] = CTB_NEW_Variables.tervtablemodel.getValueAt(i, 0).toString().trim();
                } catch (Exception e) {
                }

                //végigjárjuk az oszlopokat és összeadjuk a mennyiséget
                int osszeg = 0;
                for (int o = 3; o < CTB_NEW_Variables.tervtablemodel.getColumnCount(); o++) {
                    try {
                        osszeg += Integer.parseInt(CTB_NEW_Variables.tervtablemodel.getValueAt(i, o).toString());
                    } catch (Exception e) {
                    }
                }

                CTB_NEW_Variables.adatok[i][1] = String.valueOf(osszeg);

            } else {
                CTB_NEW_Variables.tervtablemodel.removeRow(i);
                i--;
            }
        }
        jTable11.setModel(CTB_NEW_Variables.tervtablemodel);
        TablaOszlopSzelesseg(jTable11);
        //kinullázzuk a ctb táblát

        //CTB_NEW_Variables.ctbmodel.setColumnCount(8);
        CTB_NEW_Variables.ctbmodel.setRowCount(0);

        //akkor most hozzáadjuk az uj adatokat a ctb modellhez
        for (int i = 0; i < CTB_NEW_Variables.adatok.length; i++) {
            if (CTB_NEW_Variables.adatok[i][0] != null) {
                CTB_NEW_Variables.ctbmodel.addRow(new Object[]{CTB_NEW_Variables.adatok[i][0], "", "", "", "", "", "", "" /*adatok[i][1]*/});
            }

        }
        CTB.jTable1.setModel(CTB_NEW_Variables.ctbmodel);

    }

    public void openPoCalc() {

//beállítjuk a változókba a táblákat
        CTB_NEW_Variables.ctbmodel = (DefaultTableModel) CTB.jTable1.getModel();

        for (int i = 0; i < CTB_NEW_Variables.ctbmodel.getRowCount(); i++) {
            //update progress bar
            CTB.updateBar("POCALC", CTB_NEW_Variables.ctbmodel.getRowCount() - 1, i);
            int osszeg = 0;
//és végigszaladunk a demand táblán
            for (int d = 0; d < CTB_NEW_Variables.demandmodel.getRowCount(); d++) {
//ha egyezik a ctb tábla pn e a demand tábla pn-el és booked a státusz hozzáadjuk a darabot a darabhoz, ha nincs kipipálva az entered po-s cucc

                if (!CTB.jCheckBoxMenuItem11.isSelected()) {
                    try {
                        if ((CTB_NEW_Variables.ctbmodel.getValueAt(i, 0).toString().equals(CTB_NEW_Variables.demandmodel.getValueAt(d, 7).toString()) && (CTB_NEW_Variables.demandmodel.getValueAt(d, 12).toString().equals("Booked")))) {

                            osszeg += Integer.parseInt(CTB_NEW_Variables.demandmodel.getValueAt(d, 17).toString());

                        }
                    } catch (Exception e) {
                    }
                } //ha kell az entered po is        
                else {

                    try {
                        if ((CTB_NEW_Variables.ctbmodel.getValueAt(i, 0).toString().equals(CTB_NEW_Variables.demandmodel.getValueAt(d, 7).toString())) && (CTB_NEW_Variables.demandmodel.getValueAt(d, 12).toString().equals("Booked") || CTB_NEW_Variables.demandmodel.getValueAt(d, 12).toString().equals("Entered"))) {

                            osszeg += Integer.parseInt(CTB_NEW_Variables.demandmodel.getValueAt(d, 17).toString());

                        }
                    } catch (Exception e) {
                    }

                }

            }

            CTB_NEW_Variables.ctbmodel.setValueAt(osszeg, i, 1);

        }

    }

    public void stockCalc() {
//beállítjuk a változót , hogy fut a szál

        for (int i = 0; i < CTB_NEW_Variables.ctbmodel.getRowCount(); i++) {
            //update progress bar
            CTB.updateBar("STOCK", CTB_NEW_Variables.ctbmodel.getRowCount() - 1, i);
            int osszeg = 0;
//és végigszaladunk az OH táblán
            for (int d = 0; d < CTB_NEW_Variables.ohmodel.getRowCount(); d++) {
//ha egyezik a ctb tábla pn e az OH tábla pn-el és ki kell venni az fgoodsot
                if (!CTB.jCheckBoxMenuItem12.isSelected()) {
                    try {
                        if ((CTB_NEW_Variables.ctbmodel.getValueAt(i, 0).toString().equals(CTB_NEW_Variables.ohmodel.getValueAt(d, 2).toString()) && (CTB_NEW_Variables.ohmodel.getValueAt(d, 4).toString().equals("Net-Asset"))) && !CTB_NEW_Variables.ohmodel.getValueAt(d, 0).toString().equals("FGOODS")) {

                            osszeg += Integer.parseInt(CTB_NEW_Variables.ohmodel.getValueAt(d, 6).toString());

                        }
                    } catch (Exception e) {

                    }

                } //ha kell az fgoods is bele
                else {
                    try {
                        if ((CTB_NEW_Variables.ctbmodel.getValueAt(i, 0).toString().equals(CTB_NEW_Variables.ohmodel.getValueAt(d, 2).toString()) && (CTB_NEW_Variables.ohmodel.getValueAt(d, 4).toString().equals("Net-Asset")))) {

                            osszeg += Integer.parseInt(CTB_NEW_Variables.ohmodel.getValueAt(d, 6).toString());

                        }
                    } catch (Exception e) {

                    }

                }
            }

            CTB_NEW_Variables.ctbmodel.setValueAt(osszeg, i, 2);

        }

        CTB.jTable1.setModel(CTB_NEW_Variables.ctbmodel);

    }

    public void addOh() {
        for (int i = 0; i < CTB_NEW_Variables.calcbommodel.getRowCount(); i++) {
            //update progress bar
            CTB.updateBar("OHCALC", CTB_NEW_Variables.calcbommodel.getRowCount() - 1, i);
            String comp = CTB_NEW_Variables.calcbommodel.getValueAt(i, 0).toString().trim();
            //a második az oh táblában szereplő mennyiségek összesítése, itt 3 lehetőség van , net asset , minden lokáció és stock
// ha a net asset ceckbox van kipipálva
            if (CTB.jCheckBoxMenuItem3.isSelected()) {
//bejárjuk az ohtablat es összeszedjuk azokat az ertekeket amik net asset beallitassal rendelkeznek es egyezik a pn
                osszeg = 0;
                String comment = "";

                for (int o = 0; o < CTB_NEW_Variables.ohmodel.getRowCount(); o++) {

                    if (CTB_NEW_Variables.ohmodel.getValueAt(o, 2).toString().trim().equals(comp.trim()) && CTB_NEW_Variables.ohmodel.getValueAt(o, 4).toString().trim().equals("Net-Asset")) {
//kiszedjuk a vesszot a szambol majd integerre alakitjuk es hozzaadjuk az osszeghez
                        osszeg += Integer.parseInt(CTB_NEW_Variables.ohmodel.getValueAt(o, 6).toString().replaceAll("[^0-9]",""));
                        try {
                            comment += CTB_NEW_Variables.ohmodel.getValueAt(o, 10).toString() + ", ";
                        } catch (Exception e) {
                        }

                    }
                }
//ha vegigcsavartunk az oh modellen akkor beirhatjuk az erteket a calculalt oh tablaba és a kommenteket
                CTB_NEW_Variables.calcbommodel.setValueAt(osszeg, i, 2);
                try {
                    CTB_NEW_Variables.calcbommodel.setValueAt(comment.substring(0, comment.length() - 2), i, 7);
                } catch (Exception e) {
                }

            }
//ha a stock van kipipálva
            if (CTB.jCheckBoxMenuItem4.isSelected()) {
//bejárjuk az ohtablat es összeszedjuk azokat az ertekeket amik net asset beallitassal rendelkeznek es egyezik a pn
                osszeg = 0;
                for (int o = 0; o < CTB_NEW_Variables.ohmodel.getRowCount(); o++) {

                    if (CTB_NEW_Variables.ohmodel.getValueAt(o, 2).toString().trim().equals(comp.trim()) && (CTB_NEW_Variables.ohmodel.getValueAt(o, 0).toString().contains("STOCK") || CTB_NEW_Variables.ohmodel.getValueAt(o, 0).toString().trim().contains("HIGH"))) {
//kiszedjuk a vesszot a szambol majd integerre alakitjuk es hozzaadjuk az osszeghez
                        osszeg += Integer.parseInt(CTB_NEW_Variables.ohmodel.getValueAt(o, 6).toString().trim().replaceAll("[^0-9]",""));

                    }
                }
//ha vegigcsavartunk az oh modellen akkor beirhatjuk az erteket a calculalt oh tablaba
                CTB_NEW_Variables.calcbommodel.setValueAt(osszeg, i, 2);

            }
//ha mindere kiváncsiak vagyunk
            if (CTB.jCheckBoxMenuItem5.isSelected()) {
//bejárjuk az ohtablat es összeszedjuk azokat az ertekeket amik net asset beallitassal rendelkeznek es egyezik a pn
                osszeg = 0;
                for (int o = 0; o < CTB_NEW_Variables.ohmodel.getRowCount(); o++) {

                    if (CTB_NEW_Variables.ohmodel.getValueAt(o, 2).toString().trim().equals(comp.trim())) {
//kiszedjuk a vesszot a szambol majd integerre alakitjuk es hozzaadjuk az osszeghez
                        osszeg += Integer.parseInt(CTB_NEW_Variables.ohmodel.getValueAt(o, 6).toString().trim().replaceAll("[^0-9]",""));

                    }
                }
//ha vegigcsavartunk az oh modellen akkor beirhatjuk az erteket a calculalt oh tablaba
                CTB_NEW_Variables.calcbommodel.setValueAt(osszeg, i, 2);

            }

        }
    }

    public void addAlloc() {
//végigmegyünk a teljes calbom táblán
        for (int i = 0; i < CTB_NEW_Variables.calcbommodel.getRowCount(); i++) {
            //update progress bar
            CTB.updateBar("ALLOCATIONCALC", CTB_NEW_Variables.calcbommodel.getRowCount() - 1, i);
            String comp = CTB_NEW_Variables.calcbommodel.getValueAt(i, 0).toString().trim();
            osszeg = 0;
            for (int a = 0; a < CTB_NEW_Variables.allocmodel.getRowCount(); a++) {
//ha egyezik a pn akkor összeadjuk
                if (CTB_NEW_Variables.allocmodel.getValueAt(a, 4).toString().equals(comp)) {

                    osszeg += Integer.parseInt(CTB_NEW_Variables.allocmodel.getValueAt(a, 11).toString().trim().replaceAll("[^0-9]",""));

                }

            }

//beállítjuk a kalkulált oh táblába
            CTB_NEW_Variables.calcbommodel.setValueAt(osszeg, i, 3);

        }
    }

    public void addLost(Boolean b) {

//ha mutatni kell a lost mennyiséget
        if (b) {
            //a teljes calbommodelt bejárjuk
            outerloop:
            for (int i = 0; i < CTB_NEW_Variables.calcbommodel.getRowCount(); i++) {
                //update progress bar
                CTB.updateBar("LOSTCALC", CTB_NEW_Variables.calcbommodel.getRowCount() - 1, i);
                String comp = CTB_NEW_Variables.calcbommodel.getValueAt(i, 0).toString().trim();
//es bejarjuk a lost tablat is , ha egyezik a pn akkor beírjuk a lost oszlopba
                for (int l = 0; l < CTB_NEW_Variables.lostmodel.getRowCount(); l++) {

                    if (CTB_NEW_Variables.lostmodel.getValueAt(l, 0).toString().trim().equals(comp.trim())) {

                        CTB_NEW_Variables.calcbommodel.setValueAt(CTB_NEW_Variables.lostmodel.getValueAt(l, 1).toString(), i, 5);
                        continue outerloop;
                    }

                }

//ha idáig eljutunk , írjunk be egy nullát
                CTB_NEW_Variables.calcbommodel.setValueAt("0", i, 5);

            }
        } //ha nem kell mutatni kinullázunk mindent
        else {
            for (int i = 0; i < CTB_NEW_Variables.calcbommodel.getRowCount(); i++) {

                CTB_NEW_Variables.calcbommodel.setValueAt("0", i, 5);

            }

        }
    }

    public void addHori(boolean b) {
//ha ki van jelölve
        if (b) {
//csak akkor futunk ha hét részen állunk
            try {
                if (CTB.jTable11.getSelectedColumn() > 3) {
//a teljes bomtáblát bejárjuk
                    for (int bs = 0; bs < CTB_NEW_Variables.calcbommodel.getRowCount(); bs++) {
                        //update progressbar
                        CTB.updateBar("ADDHORIZONTAL", CTB_NEW_Variables.calcbommodel.getRowCount() - 1, bs);
                        osszeg = 0;
//kell egy változó , hogy melyik hétig adjuk össze a darabokat
                        int hetig = Integer.parseInt(jTable11.getColumnModel().getColumn(jTable11.getSelectedColumn()).getHeaderValue().toString());
//beirjuk a shorttabla nevenek is ezt a hetet h tudjuk h mizu
                        jTable9.getColumnModel().getColumn(9).setHeaderValue("Horizontals " + hetig);
//bejárjuk a hrizontal modellt a pn után kutatva
                        outerloop:
                        for (int h = 0; h < CTB_NEW_Variables.horizontalmodel.getRowCount(); h++) {
//ha egyezik a pn és a 19. oszlopban a supply sorban vagyunk akkor bemegyünk és elkezdjük tekerni az oszlopokat is
                            if (CTB_NEW_Variables.calcbommodel.getValueAt(bs, 0).equals(CTB_NEW_Variables.horizontalmodel.getValueAt(h, 1)) && CTB_NEW_Variables.horizontalmodel.getValueAt(h, 18).toString().contains("Supply")) {
// elkezdjük bejárni az oszlopokat
                                for (int oszlop = 19; oszlop < CTB_NEW_Variables.horizontalmodel.getColumnCount(); oszlop++) {
//meg kell keresni , hogy melyik hét oszlopában vagyunk
                                    int horihet = 0;
                                    for (int horih = 0; horih < CTB_NEW_Variables.horizontalmodel.getRowCount(); horih++) {
                                        try {
                                            if (CTB_NEW_Variables.horizontalmodel.getValueAt(horih, oszlop).toString().contains("Week")) {

                                                horihet = Integer.parseInt(CTB_NEW_Variables.horizontalmodel.getValueAt(horih + 1, oszlop).toString());
                                                break;

                                            }
                                        } catch (Exception e) {
                                        }

                                    }
//ha megvan a hét és az nagyobb nulla akkor hozzáadjuk az összeghez
                                    if (horihet <= hetig && horihet > 0) {

                                        osszeg += Integer.parseInt(CTB_NEW_Variables.horizontalmodel.getValueAt(h, oszlop).toString().replaceAll("[^0-9]",""));

                                    } else if (horihet > hetig) {

                                        break outerloop;

                                    }

                                }
                            }

                        }
                        //beírjuk az összeget az ohmodellbe
                        CTB_NEW_Variables.calcbommodel.setValueAt(osszeg, bs, 6);

                    }

                }
            } catch (Exception e) {
            }
        } else {

            for (int i = 0; i < CTB_NEW_Variables.calcbommodel.getRowCount(); i++) {

                CTB_NEW_Variables.calcbommodel.setValueAt("0", i, 6);
            }
        }
    }

}
