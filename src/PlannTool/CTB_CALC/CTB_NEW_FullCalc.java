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
import java.awt.Color;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author gabor_hanacsek
 */
public class CTB_NEW_FullCalc {

    private boolean c;
    CTB_NEW_Variables v;
    int x;
    int y;
    int osszeg = 0;
    String comp = "";

    public CTB_NEW_FullCalc(boolean compare, CTB_NEW_Variables v) {
        this.c = compare;
        this.v = v;
        //hogy vissza tudjuk állítani a kijelölést
        this.y = CTB.jTable11.getSelectedRow();
        this.x = CTB.jTable11.getSelectedColumn();

    }

    public void calc() {

        if (jTable1.isEditing()) {

            jTable1.getCellEditor().stopCellEditing();

        }
        if (jTable11.isEditing()) {

            jTable11.getCellEditor().stopCellEditing();

        }
//beállítjuk a táblákat

        v.lostmodel = (DefaultTableModel) CTB.jTable12.getModel();
        v.allocmodel = (DefaultTableModel) CTB.jTable3.getModel();
        v.horizontalmodel = (DefaultTableModel) CTB.jTable13.getModel();
        v.tervtablemodel = (DefaultTableModel) CTB.jTable11.getModel();
        v.demandmodel = (DefaultTableModel) CTB.jTable4.getModel();
        v.ohmodel = (DefaultTableModel) CTB.jTable2.getModel();
        v.womodel = (DefaultTableModel) CTB.jTable5.getModel();

//itt elválunk, ha változtak az adatok akkor az összeset ujra össze kell szedni
        if (!c) {
            v.calcbommodel = (DefaultTableModel) jTable7.getModel();
//felépítjük a ctb táblát a terv tábla alapján

            build();
//lekérjük a wip adatokat ezt egy külön szálban

            CTB_Wipquery wip = new CTB_Wipquery(jTable1);
            wip.start();
//kiszámoljuk bele a nyitott po-kat

            openpocalc();
//kiszámoljuk a raktárkészletet a késztermékre!!!!

            stockcalc();
//kiszámoljuk mennyi nyitott job van a termékre

            openordercalc();
//összeállítjuk a kalkulált bom táblát amiben amugy az összes alapanyag info is benne van

            bomCalc();

            //pn adatok beírása
            CTB_PnDatas p = new CTB_PnDatas();
            try {
                p.adatleker();
            } catch (SQLException ex) {
                Logger.getLogger(CTB_NEW_FullCalc.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(CTB_NEW_FullCalc.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
//a betervezett mennyiség a backend sheetekről

        tervszamol();
//ki kell számolni ,hogy mennyit kell még betervezni 

        tervmennyisegszamol();
//ez adja meg , hogy mennyit kell még betervezni

        needtobuild();
//hozzáadjuk a horizontalt
        AddHoriFull();
//ez számolja ki a plann mennyiség anyagigényét a calcbom tablaba

        plannqty();
//ki kell szamolni a total oh-t

        totalohcalc();
//ez megadja , hogy mennyira vagyunk ctb-k az adott pn ből
        ctbszamol();

//összekapcsolódás
        new CTB_NEW_LinkTables(jScrollPane1, jTable1, jScrollPane11, jTable11, 1);
        ablak.stat.beir(System.getProperty("user.name"), "CTB kapcsolodas!", "", "gabor.hanacsek@sanmina.com");

//a táblák szélességének beállítása
        try {
            CTB.jTable11.setRowSelectionInterval(y - 1, y - 1);
            CTB.jTable11.setColumnSelectionInterval(x, x);
        } catch (Exception e) {
        }
        TablaOszlopSzelesseg(jTable1);
        TablaOszlopSzelesseg(jTable11);

    }

    public void totalohcalc() {
        for (int i = 0; i < v.calcbommodel.getRowCount(); i++) {
//négy különböző eset lehet
            int osszeg = 0;
//ha mindennel számolunk , lost és horizontallal is
            if (CTB.jCheckBoxMenuItem7.isSelected() && CTB.jCheckBoxMenuItem8.isSelected()) {
                osszeg = Integer.parseInt(v.calcbommodel.getValueAt(i, 2).toString().trim()) - Integer.parseInt(v.calcbommodel.getValueAt(i, 3).toString().trim()) - Integer.parseInt(v.calcbommodel.getValueAt(i, 4).toString().trim()) - Integer.parseInt(v.calcbommodel.getValueAt(i, 5).toString().trim()) + Integer.parseInt(v.calcbommodel.getValueAt(i, 6).toString().trim());
            }
//ha csak a lostalszamolunk
            if (CTB.jCheckBoxMenuItem7.isSelected() && !CTB.jCheckBoxMenuItem8.isSelected()) {
                osszeg = Integer.parseInt(v.calcbommodel.getValueAt(i, 2).toString().trim()) - Integer.parseInt(v.calcbommodel.getValueAt(i, 3).toString().trim()) - Integer.parseInt(v.calcbommodel.getValueAt(i, 4).toString().trim()) - Integer.parseInt(v.calcbommodel.getValueAt(i, 5).toString().trim());
            }
//ha csaka horizontallal szamolunk
            if (!CTB.jCheckBoxMenuItem7.isSelected() && CTB.jCheckBoxMenuItem8.isSelected()) {
                osszeg = Integer.parseInt(v.calcbommodel.getValueAt(i, 2).toString().trim()) - Integer.parseInt(v.calcbommodel.getValueAt(i, 3).toString().trim()) - Integer.parseInt(v.calcbommodel.getValueAt(i, 4).toString().trim()) + Integer.parseInt(v.calcbommodel.getValueAt(i, 6).toString().trim());
            }
//ha semmivel semszamolunk
            if (!CTB.jCheckBoxMenuItem7.isSelected() && !CTB.jCheckBoxMenuItem8.isSelected()) {
                osszeg = Integer.parseInt(v.calcbommodel.getValueAt(i, 2).toString().trim()) - Integer.parseInt(v.calcbommodel.getValueAt(i, 3).toString().trim()) - Integer.parseInt(v.calcbommodel.getValueAt(i, 4).toString().trim());
            }
//végül beállítjuk a calohba
            v.calcbommodel.setValueAt(osszeg, i, 8);
        }
    }

    public void plannqty() {
//meg kell nézni , hogy az ujpn hez tartozo kesztermekszamhoz van e terv mennyiség beírva
//tekerjük az összerakott bom táblát és ha megtaláljuk benne a pn-t akkor végigmegyünk az oszlopokon és kiszedjük az oszlopneveket majd összehasonlítjuk a ctbmodel terv részével h van e oda írva

        for (int b = 0; b < v.calcbommodel.getRowCount(); b++) {
            int osszeg = 0;

            for (int c = 0; c < v.calcbommodel.getColumnCount(); c++) {

                String termek = v.calcbommodel.getColumnName(c).toString();
//megnezzuk , hogy van e terv mennyiseg
                for (int ctb = 0; ctb < v.ctbmodel.getRowCount(); ctb++) {
                    try {
                        if (termek.equals(v.ctbmodel.getValueAt(ctb, 0)) && Integer.parseInt(v.ctbmodel.getValueAt(ctb, 6).toString()) > 0) {
//ha van akkor kiszedjük mint szorzót 
                            int darabszam = Integer.parseInt(v.ctbmodel.getValueAt(ctb, 6).toString());
                            try {
                                osszeg += darabszam * (Math.round(Float.valueOf(v.calcbommodel.getValueAt(b, c).toString())));
                            } catch (Exception e) {
                            }
                        }

                    } catch (Exception e) {
                    }
                }

            }
            //beírjuk az ohmodellbe az összeget
            v.calcbommodel.setValueAt(osszeg, b, 4);

        }

    }

    public void ctbszamol() {
        String pn = "";
//meg kell keresnia legkisebb szamot ami fogja a termeket
//végigmegyünk a ctb modellen es kivesszük a kesztermekpn-t
        outerloop:
        for (int i = 0; i < v.ctbmodel.getRowCount(); i++) {
            pn = v.ctbmodel.getValueAt(i, 0).toString();
//elkezdjuk bejarni a bommatrixot ez utan a pn utan kutatva
            for (int bo = 9; bo < v.calcbommodel.getColumnCount(); bo++) {
//a legkisebb gyártható szám
                int less = 999999999;
//ha megtaláljuk a pn-t mert egyezik a column name vele akkor bejárjuk asorokat és matekozunk
                if (v.calcbommodel.getColumnName(bo).equals(pn)) {

                    for (int bs = 0; bs < v.calcbommodel.getRowCount(); bs++) {
//ha van valami írva az adott orba akkor matekozunk
                        try {

                            int possible = Integer.parseInt(v.calcbommodel.getValueAt(bs, 8).toString()) / (int) (Double.parseDouble(v.calcbommodel.getValueAt(bs, bo).toString()));

                            if (less > possible) {
                                less = possible;
                            }

                        } catch (Exception e) {

                        }

                    }
//ha végigértünk a soron akkor beírjuk a ctb modellbe a legkisebb eredmenyt es ugrunk a következő pn re

//ha a less 9999999 akkor beírjuk , hogy bomhiba
                    if (less == 999999999) {
                        v.ctbmodel.setValueAt("BOM hiba!", i, 5);
                    } else {
                        v.ctbmodel.setValueAt(less, i, 5);
                    }
                    continue outerloop;
                }

            }

        }

    }

    public void tervszamol() {

//vegigfutunk a listan és megprobálunk keresni ilyen tabot a backendtervezőben
        for (int i = 0; i < SelectedBecells.size(); i++) {
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

    public void tervmennyisegszamol() {

        //ki kell számolni ,hogy mennyit kell még betervezni , ezt úgy tesszük , hogy kivonjuk a betervezett hetek összegéből a stockot , az open job-ot és a tervet
        for (int i = 0; i < CTB_NEW_Variables.tervtablemodel.getRowCount(); i++) {

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

    public void needtobuild() {

        for (int i = 0; i < v.ctbmodel.getRowCount(); i++) {
            //openorder
            int openorder = 0;
            int stock = 0;
            int openjob = 0;
            int terv = 0;

            try {
                openorder = Integer.parseInt(v.ctbmodel.getValueAt(i, 1).toString());
            } catch (Exception e) {
            }
            try {
                stock = Integer.parseInt(v.ctbmodel.getValueAt(i, 2).toString());
            } catch (Exception e) {
            }
            try {
                openjob = Integer.parseInt(v.ctbmodel.getValueAt(i, 3).toString());
            } catch (Exception e) {
            }
            try {
                terv = Integer.parseInt(v.ctbmodel.getValueAt(i, 6).toString());
            } catch (Exception e) {
            }

            v.ctbmodel.setValueAt(openorder - (terv + openjob + stock), i, 4);

        }

    }

    public void openordercalc() {

        for (int i = 0; i < v.ctbmodel.getRowCount(); i++) {
            int osszeg = 0;
//és végigszaladunk az OH táblán
            for (int d = 0; d < v.womodel.getRowCount(); d++) {
//ha egyezik a ctb tábla pn e az OH tábla pn-el 
                try {
                    if ((v.ctbmodel.getValueAt(i, 0).toString().equals(v.womodel.getValueAt(d, 1).toString()) && (v.womodel.getValueAt(d, 8).toString().contains("Released")))) {

                        osszeg += Integer.parseInt(v.womodel.getValueAt(d, 17).toString());

                    }
                } catch (Exception e) {
                }

            }

            v.ctbmodel.setValueAt(osszeg, i, 3);

        }

        jTable1.setModel(v.ctbmodel);

    }

    public void build() {

        CTB_NEW_Variables.adatok = new String[jTable11.getRowCount()][2];
        //bejárjuk a táblát és feltöltjük a tömböt
        for (int i = 0; i < CTB_NEW_Variables.tervtablemodel.getRowCount(); i++) {

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

        CTB_NEW_Variables.ctbmodel = (DefaultTableModel) jTable1.getModel();
        //CTB_NEW_Variables.ctbmodel.setColumnCount(8);
        CTB_NEW_Variables.ctbmodel.setRowCount(0);

        //akkor most hozzáadjuk az uj adatokat a ctb modellhez
        for (int i = 0; i < CTB_NEW_Variables.adatok.length; i++) {
            if (CTB_NEW_Variables.adatok[i][0] != null) {
                CTB_NEW_Variables.ctbmodel.addRow(new Object[]{CTB_NEW_Variables.adatok[i][0], "", "", "", "", "", "", "" /*adatok[i][1]*/});
            }

        }
        jTable1.setModel(CTB_NEW_Variables.ctbmodel);

    }

    public void openpocalc() {

//beállítjuk a változókba a táblákat
        v.ctbmodel = (DefaultTableModel) CTB.jTable1.getModel();

        for (int i = 0; i < v.ctbmodel.getRowCount(); i++) {
            int osszeg = 0;
//és végigszaladunk a demand táblán
            for (int d = 0; d < v.demandmodel.getRowCount(); d++) {
//ha egyezik a ctb tábla pn e a demand tábla pn-el és booked a státusz hozzáadjuk a darabot a darabhoz
                try {
                    if ((v.ctbmodel.getValueAt(i, 0).toString().equals(v.demandmodel.getValueAt(d, 7).toString()) && (v.demandmodel.getValueAt(d, 12).toString().equals("Booked")))) {

                        osszeg += Integer.parseInt(v.demandmodel.getValueAt(d, 17).toString());

                    }
                } catch (Exception e) {
                }

            }

            v.ctbmodel.setValueAt(osszeg, i, 1);

        }

    }

    public void stockcalc() {
//beállítjuk a változót , hogy fut a szál

        for (int i = 0; i < v.ctbmodel.getRowCount(); i++) {
            int osszeg = 0;
//és végigszaladunk az OH táblán
            for (int d = 0; d < v.ohmodel.getRowCount(); d++) {
//ha egyezik a ctb tábla pn e az OH tábla pn-el 
                try {
                    if ((v.ctbmodel.getValueAt(i, 0).toString().equals(v.ohmodel.getValueAt(d, 2).toString()) && (v.ohmodel.getValueAt(d, 4).toString().equals("Net-Asset")))) {

                        osszeg += Integer.parseInt(v.ohmodel.getValueAt(d, 6).toString());

                    }
                } catch (Exception e) {

                }

            }

            v.ctbmodel.setValueAt(osszeg, i, 2);

        }

        jTable1.setModel(v.ctbmodel);

    }
    //a bom calc bol jön át 
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------

    public void bomCalc() {

//a késztermék számok lesznek
        String pn;
//kinullazzuk a sorokat es oszlopokat
        v.calcbommodel.setRowCount(0);
        v.calcbommodel.setColumnCount(9);

//hozzáadjuk columnként a pn eket
        for (int i = 0; i < v.ctbmodel.getRowCount(); i++) {

            v.calcbommodel.addColumn(v.ctbmodel.getValueAt(i, 0).toString());

        }
//felvesszük az indentedbom modellt
        v.indentedbommodel = (DefaultTableModel) jTable6.getModel();
//beallitjuk a valtozot , hogy csak az aktualis szintre nezzunk e ctb-t vagy vilagitsuk e at az egeszet
//ha az egeszet nezzük , ki kell venni az sa-kat!!! , ha nem , akkor maradhatnak de csak level 1 kell!!
        int lvl = 100;
        String sa = "SA";
        if (CTB.jCheckBoxMenuItem6.isSelected()) {

            lvl = 1;
            sa = "kiskutyafüle";

        }

//miután ez megvan végigmegyünk rajta oszlopszor és megpróbáljuk megkereseni az adott oszlop pn-ét az intended bom ban,
        for (int o = 7; o < v.calcbommodel.getColumnCount(); o++) {

//felvesszük pn-nek a pn-t :p
            pn = v.calcbommodel.getColumnName(o).trim();

            for (int r = 0; r < v.indentedbommodel.getRowCount(); r++) {

//ha egyezik a pn az intended bom pn -ével és van ora type
                if (pn.equals(v.indentedbommodel.getValueAt(r, 0).toString().trim()) && !v.indentedbommodel.getValueAt(r, 8).toString().equals(sa) && !v.indentedbommodel.getValueAt(r, 5).toString().equals("0") && Integer.parseInt(v.indentedbommodel.getValueAt(r, 5).toString()) <= lvl) {

//itt megvizsgáljuk , hogy kell a a phantom item vagy nem
//ha nincs kipipálva az azt jelenti , hogy nem kell beletenni , ergo ha az akkor ugrunk a ciklusban
                    if (!jCheckBoxMenuItem1.isSelected() && v.indentedbommodel.getValueAt(r, 8).toString().equals("PH")) {

                        continue;

                    }

//itt megvizsgaljuk , hogy kell e a bulk
//ha nincs kipipálva az azt jelenti , hogy nem kell vele számolni , ergo ha az akkor ugrunk a ciklusban
                    if (!jCheckBoxMenuItem2.isSelected() && v.indentedbommodel.getValueAt(r, 11).toString().equals("Bulk")) {

                        continue;

                    }
//ha eddig eljutunk
//felvesszük a componentet stringnek
                    comp = v.indentedbommodel.getValueAt(r, 7).toString().trim();
//akkor megvizsgáljuk , hogy a componentet felvettük e már sorba a modellbe
//kell egy boolean , hogy kell e uj sort létrehozni
                    boolean ujsor = true;
                    for (int i = 0; i < v.calcbommodel.getRowCount(); i++) {
//ha találunk olyan sort akkor oda írunk
                        if (v.calcbommodel.getValueAt(i, 0).toString().equals(comp)) {

                            v.calcbommodel.setValueAt(v.indentedbommodel.getValueAt(r, 13).toString(), i, o);
                            v.calcbommodel.setValueAt(v.indentedbommodel.getValueAt(r, 9).toString(), i, 1);
                            ujsor = false;

                            AddOH(i);

                            AddAlloc(i);

                            AddLost(i);

                            // AddHori(i);
                            break;
                        }
//ha nem találunk akkor hozzá kell adni egy sort

                    }
//ha nem írtunk , ezért kell egy uj sort hozzáadni akkor ezt tesszük
                    if (ujsor) {

                        v.calcbommodel.addRow(new Object[]{"", "", "", "", "", "", "", "", ""});
                        v.calcbommodel.setValueAt(comp, v.calcbommodel.getRowCount() - 1, 0);
                        v.calcbommodel.setValueAt(v.indentedbommodel.getValueAt(r, 13).toString(), v.calcbommodel.getRowCount() - 1, o);
                        v.calcbommodel.setValueAt(v.indentedbommodel.getValueAt(r, 9).toString(), v.calcbommodel.getRowCount() - 1, 1);
                        int sor = v.calcbommodel.getRowCount() - 1;
                        //a beépülők lesznek

                        AddOH(sor);

                        AddAlloc(sor);

                        AddLost(sor);

                        //  AddHori(sor);
                    }

                }

            }
        }

        CTB.jTable7.setModel(v.calcbommodel);
        TablaOszlopSzelesseg(CTB.jTable7);

    }

//ezt használjuk amikor ki-be kapcsolgatjuk a kalkulációs beállításokat
    public void AddOH() {
        for (int i = 0; i < v.calcbommodel.getRowCount(); i++) {
            String comp = v.calcbommodel.getValueAt(i, 0).toString().trim();
            //a második az oh táblában szereplő mennyiségek összesítése, itt 3 lehetőség van , net asset , minden lokáció és stock
// ha a net asset ceckbox van kipipálva
            if (CTB.jCheckBoxMenuItem3.isSelected()) {
//bejárjuk az ohtablat es összeszedjuk azokat az ertekeket amik net asset beallitassal rendelkeznek es egyezik a pn
                osszeg = 0;
                for (int o = 0; o < v.ohmodel.getRowCount(); o++) {

                    if (v.ohmodel.getValueAt(o, 2).toString().trim().equals(comp.trim()) && v.ohmodel.getValueAt(o, 4).toString().trim().equals("Net-Asset")) {
//kiszedjuk a vesszot a szambol majd integerre alakitjuk es hozzaadjuk az osszeghez
                        osszeg += Integer.parseInt(v.ohmodel.getValueAt(o, 6).toString().replace(",", ""));

                    }
                }
//ha vegigcsavartunk az oh modellen akkor beirhatjuk az erteket a calculalt oh tablaba
                v.calcbommodel.setValueAt(osszeg, i, 2);

            }
//ha a stock van kipipálva
            if (CTB.jCheckBoxMenuItem4.isSelected()) {
//bejárjuk az ohtablat es összeszedjuk azokat az ertekeket amik net asset beallitassal rendelkeznek es egyezik a pn
                osszeg = 0;
                for (int o = 0; o < v.ohmodel.getRowCount(); o++) {

                    if (v.ohmodel.getValueAt(o, 2).toString().trim().equals(comp.trim()) && (v.ohmodel.getValueAt(o, 0).toString().contains("STOCK") || v.ohmodel.getValueAt(o, 0).toString().trim().contains("HIGH"))) {
//kiszedjuk a vesszot a szambol majd integerre alakitjuk es hozzaadjuk az osszeghez
                        osszeg += Integer.parseInt(v.ohmodel.getValueAt(o, 6).toString().trim().replace(",", ""));

                    }
                }
//ha vegigcsavartunk az oh modellen akkor beirhatjuk az erteket a calculalt oh tablaba
                v.calcbommodel.setValueAt(osszeg, i, 2);

            }
//ha mindere kiváncsiak vagyunk
            if (CTB.jCheckBoxMenuItem5.isSelected()) {
//bejárjuk az ohtablat es összeszedjuk azokat az ertekeket amik net asset beallitassal rendelkeznek es egyezik a pn
                osszeg = 0;
                for (int o = 0; o < v.ohmodel.getRowCount(); o++) {

                    if (v.ohmodel.getValueAt(o, 2).toString().trim().equals(comp.trim())) {
//kiszedjuk a vesszot a szambol majd integerre alakitjuk es hozzaadjuk az osszeghez
                        osszeg += Integer.parseInt(v.ohmodel.getValueAt(o, 6).toString().trim().replace(",", ""));

                    }
                }
//ha vegigcsavartunk az oh modellen akkor beirhatjuk az erteket a calculalt oh tablaba
                v.calcbommodel.setValueAt(osszeg, i, 2);

            }

        }
    }

    public void AddOH(int i) {
        //a második az oh táblában szereplő mennyiségek összesítése, itt 3 lehetőség van , net asset , minden lokáció és stock
// ha a net asset ceckbox van kipipálva
        if (CTB.jCheckBoxMenuItem3.isSelected()) {
//bejárjuk az ohtablat es összeszedjuk azokat az ertekeket amik net asset beallitassal rendelkeznek es egyezik a pn
            osszeg = 0;
            for (int o = 0; o < v.ohmodel.getRowCount(); o++) {

                if (v.ohmodel.getValueAt(o, 2).toString().trim().equals(comp.trim()) && v.ohmodel.getValueAt(o, 4).toString().trim().equals("Net-Asset")) {
//kiszedjuk a vesszot a szambol majd integerre alakitjuk es hozzaadjuk az osszeghez
                    osszeg += Integer.parseInt(v.ohmodel.getValueAt(o, 6).toString().replace(",", ""));

                }
            }
//ha vegigcsavartunk az oh modellen akkor beirhatjuk az erteket a calculalt oh tablaba
            v.calcbommodel.setValueAt(osszeg, i, 2);

        }
//ha a stock van kipipálva
        if (CTB.jCheckBoxMenuItem4.isSelected()) {
//bejárjuk az ohtablat es összeszedjuk azokat az ertekeket amik net asset beallitassal rendelkeznek es egyezik a pn
            osszeg = 0;
            for (int o = 0; o < v.ohmodel.getRowCount(); o++) {

                if (v.ohmodel.getValueAt(o, 2).toString().trim().equals(comp.trim()) && (v.ohmodel.getValueAt(o, 0).toString().contains("STOCK") || v.ohmodel.getValueAt(o, 0).toString().trim().contains("HIGH"))) {
//kiszedjuk a vesszot a szambol majd integerre alakitjuk es hozzaadjuk az osszeghez
                    osszeg += Integer.parseInt(v.ohmodel.getValueAt(o, 6).toString().trim().replace(",", ""));

                }
            }
//ha vegigcsavartunk az oh modellen akkor beirhatjuk az erteket a calculalt oh tablaba
            v.calcbommodel.setValueAt(osszeg, i, 2);

        }
//ha mindere kiváncsiak vagyunk
        if (CTB.jCheckBoxMenuItem5.isSelected()) {
//bejárjuk az ohtablat es összeszedjuk azokat az ertekeket amik net asset beallitassal rendelkeznek es egyezik a pn
            osszeg = 0;
            for (int o = 0; o < v.ohmodel.getRowCount(); o++) {

                if (v.ohmodel.getValueAt(o, 2).toString().trim().equals(comp.trim())) {
//kiszedjuk a vesszot a szambol majd integerre alakitjuk es hozzaadjuk az osszeghez
                    osszeg += Integer.parseInt(v.ohmodel.getValueAt(o, 6).toString().trim().replace(",", ""));

                }
            }
//ha vegigcsavartunk az oh modellen akkor beirhatjuk az erteket a calculalt oh tablaba
            v.calcbommodel.setValueAt(osszeg, i, 2);

        }

    }
//ezt a metódust használjuk a rawoh összeadásnál ha módosítottuk kézzel

    public void AddOH(int i, String comp) {
        //a második az oh táblában szereplő mennyiségek összesítése, itt 3 lehetőség van , net asset , minden lokáció és stock
// ha a net asset ceckbox van kipipálva
        if (CTB.jCheckBoxMenuItem3.isSelected()) {
//bejárjuk az ohtablat es összeszedjuk azokat az ertekeket amik net asset beallitassal rendelkeznek es egyezik a pn
            osszeg = 0;
            for (int o = 0; o < v.ohmodel.getRowCount(); o++) {

                if (v.ohmodel.getValueAt(o, 2).toString().trim().equals(comp.trim()) && v.ohmodel.getValueAt(o, 4).toString().trim().equals("Net-Asset")) {
//kiszedjuk a vesszot a szambol majd integerre alakitjuk es hozzaadjuk az osszeghez
                    osszeg += Integer.parseInt(v.ohmodel.getValueAt(o, 6).toString().replace(",", ""));

                }
            }
//ha vegigcsavartunk az oh modellen akkor beirhatjuk az erteket a calculalt oh tablaba
            v.calcbommodel.setValueAt(osszeg, i, 2);

        }
//ha a stock van kipipálva
        if (CTB.jCheckBoxMenuItem4.isSelected()) {
//bejárjuk az ohtablat es összeszedjuk azokat az ertekeket amik net asset beallitassal rendelkeznek es egyezik a pn
            osszeg = 0;
            for (int o = 0; o < v.ohmodel.getRowCount(); o++) {

                if (v.ohmodel.getValueAt(o, 2).toString().trim().equals(comp.trim()) && (v.ohmodel.getValueAt(o, 0).toString().contains("STOCK") || v.ohmodel.getValueAt(o, 0).toString().trim().contains("HIGH"))) {
//kiszedjuk a vesszot a szambol majd integerre alakitjuk es hozzaadjuk az osszeghez
                    osszeg += Integer.parseInt(v.ohmodel.getValueAt(o, 6).toString().trim().replace(",", ""));

                }
            }
//ha vegigcsavartunk az oh modellen akkor beirhatjuk az erteket a calculalt oh tablaba
            v.calcbommodel.setValueAt(osszeg, i, 2);

        }
//ha mindere kiváncsiak vagyunk
        if (CTB.jCheckBoxMenuItem5.isSelected()) {
//bejárjuk az ohtablat es összeszedjuk azokat az ertekeket amik net asset beallitassal rendelkeznek es egyezik a pn
            osszeg = 0;
            for (int o = 0; o < v.ohmodel.getRowCount(); o++) {

                if (v.ohmodel.getValueAt(o, 2).toString().trim().equals(comp.trim())) {
//kiszedjuk a vesszot a szambol majd integerre alakitjuk es hozzaadjuk az osszeghez
                    osszeg += Integer.parseInt(v.ohmodel.getValueAt(o, 6).toString().trim().replace(",", ""));

                }
            }
//ha vegigcsavartunk az oh modellen akkor beirhatjuk az erteket a calculalt oh tablaba
            v.calcbommodel.setValueAt(osszeg, i, 2);

        }

    }

    public void AddAlloc(int i) {
        osszeg = 0;
        for (int a = 0; a < v.allocmodel.getRowCount(); a++) {
//ha egyezik a pn akkor összeadjuk
            if (v.allocmodel.getValueAt(a, 4).toString().equals(comp)) {

                osszeg += Integer.parseInt(v.allocmodel.getValueAt(a, 11).toString().trim().replace(",", ""));

            }

        }

//beállítjuk a kalkulált oh táblába
        v.calcbommodel.setValueAt(osszeg, i, 3);

    }

    public void AddLost(int i) {
//bejarjuk es bejarjuk az oh tablat is , ha egyezik a pn akkor beírjuk a lost oszlopba

        for (int l = 0; l < v.lostmodel.getRowCount(); l++) {

            if (v.lostmodel.getValueAt(l, 0).toString().trim().equals(comp.trim())) {

                v.calcbommodel.setValueAt(v.lostmodel.getValueAt(l, 1).toString(), i, 5);
                return;
            }

        }

//ha idáig eljutunk , írjunk be egy nullát
        v.calcbommodel.setValueAt("0", i, 5);

    }

    public void AddLost(int i, String comp) {
//bejarjuk es bejarjuk az oh tablat is , ha egyezik a pn akkor beírjuk a lost oszlopba

        for (int l = 0; l < v.lostmodel.getRowCount(); l++) {

            if (v.lostmodel.getValueAt(l, 0).toString().trim().equals(comp.trim())) {

                v.calcbommodel.setValueAt(v.lostmodel.getValueAt(l, 1).toString(), i, 5);
                return;
            }

        }

//ha idáig eljutunk , írjunk be egy nullát
        v.calcbommodel.setValueAt("0", i, 5);

    }

    public void AddHori(int i) {
        osszeg = 0;
//csak akkor futunk ha hét részen állunk
        try {
            if (CTB.jTable11.getSelectedColumn() > 3) {
//kell egy változó , hogy melyik hétig adjuk össze a darabokat
                int hetig = Integer.parseInt(jTable11.getColumnModel().getColumn(jTable11.getSelectedColumn()).getHeaderValue().toString());
//beirjuk a shorttabla nevenek is ezt a hetet h tudjuk h mizu
                jTable9.getColumnModel().getColumn(9).setHeaderValue("Horizontals " + hetig);
//bejárjuk a hrizontal modellt a pn után kutatva
                outerloop:
                for (int h = 0; h < v.horizontalmodel.getRowCount(); h++) {
//ha egyezik a pn és a 19. oszlopban a supply sorban vagyunk akkor bemegyünk és elkezdjük tekerni az oszlopokat is
                    if (comp.equals(v.horizontalmodel.getValueAt(h, 1)) && v.horizontalmodel.getValueAt(h, 18).toString().contains("Supply")) {
// elkezdjük bejárni az oszlopokat
                        for (int oszlop = 19; oszlop < v.horizontalmodel.getColumnCount(); oszlop++) {
//meg kell keresni , hogy melyik hét oszlopában vagyunk
                            int horihet = 0;
                            for (int horih = 0; horih < v.horizontalmodel.getRowCount(); horih++) {
                                try {
                                    if (v.horizontalmodel.getValueAt(horih, oszlop).toString().contains("Week")) {

                                        horihet = Integer.parseInt(v.horizontalmodel.getValueAt(horih + 1, oszlop).toString());
                                        break;

                                    }
                                } catch (Exception e) {
                                }

                            }
//ha megvan a hét és az nagyobb nulla akkor hozzáadjuk az összeghez
                            if (horihet <= hetig && horihet > 0) {

                                osszeg += Integer.parseInt(v.horizontalmodel.getValueAt(h, oszlop).toString().replace(",", ""));

                            } else if (horihet > hetig) {

                                break outerloop;

                            }

                        }
                    }

                }

            }
//beírjuk az összeget az ohmodellbe
            v.calcbommodel.setValueAt(osszeg, i, 6);
        } catch (Exception e) {
        }
    }

    public void AddHoriFull() {

//csak akkor futunk ha hét részen állunk
        try {
            if (CTB.jTable11.getSelectedColumn() > 3) {
//a teljes bomtáblát bejárjuk
                for (int bs = 0; bs < v.calcbommodel.getRowCount(); bs++) {
                    osszeg = 0;
//kell egy változó , hogy melyik hétig adjuk össze a darabokat
                    int hetig = Integer.parseInt(jTable11.getColumnModel().getColumn(jTable11.getSelectedColumn()).getHeaderValue().toString());
//beirjuk a shorttabla nevenek is ezt a hetet h tudjuk h mizu
                    jTable9.getColumnModel().getColumn(9).setHeaderValue("Horizontals " + hetig);
//bejárjuk a hrizontal modellt a pn után kutatva
                    outerloop:
                    for (int h = 0; h < v.horizontalmodel.getRowCount(); h++) {
//ha egyezik a pn és a 19. oszlopban a supply sorban vagyunk akkor bemegyünk és elkezdjük tekerni az oszlopokat is
                        if (v.calcbommodel.getValueAt(bs, 0).equals(v.horizontalmodel.getValueAt(h, 1)) && v.horizontalmodel.getValueAt(h, 18).toString().contains("Supply")) {
// elkezdjük bejárni az oszlopokat
                            for (int oszlop = 19; oszlop < v.horizontalmodel.getColumnCount(); oszlop++) {
//meg kell keresni , hogy melyik hét oszlopában vagyunk
                                int horihet = 0;
                                for (int horih = 0; horih < v.horizontalmodel.getRowCount(); horih++) {
                                    try {
                                        if (v.horizontalmodel.getValueAt(horih, oszlop).toString().contains("Week")) {

                                            horihet = Integer.parseInt(v.horizontalmodel.getValueAt(horih + 1, oszlop).toString());
                                            break;

                                        }
                                    } catch (Exception e) {
                                    }

                                }
//ha megvan a hét és az nagyobb nulla akkor hozzáadjuk az összeghez
                                if (horihet <= hetig && horihet > 0) {

                                    osszeg += Integer.parseInt(v.horizontalmodel.getValueAt(h, oszlop).toString().replace(",", ""));

                                } else if (horihet > hetig) {

                                    break outerloop;

                                }

                            }
                        }

                    }
                    //beírjuk az összeget az ohmodellbe
                    v.calcbommodel.setValueAt(osszeg, bs, 6);

                }

            }
        } catch (Exception e) {
        }
    }

}
