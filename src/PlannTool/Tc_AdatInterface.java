/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool;

import java.util.concurrent.ExecutionException;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author gabor_hanacsek
 */
public class Tc_AdatInterface {

    Tc_Besheet b;

    public Tc_AdatInterface(Tc_Besheet b) {

        this.b = b;

    }

    public void tablabatolt() {

        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) b.jTable2.getModel();
        model.setRowCount(0);

        for (int r = 0; r < b.tablaadat.length; r++) {

            model.addRow(new Object[b.tablaadat[1].length]);
            for (int o = 0; o < b.tablaadat[1].length; o++) {

                String adat = "";

                try {
                    adat = b.tablaadat[r][o].value;
                } catch (Exception e) {
                }

                model.setValueAt(adat, r, o);

            }

        }

        b.jTable2.setModel(model);

    }

    public void adatbatoltpluszsor() {

//kell egy táblamodell
        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) b.jTable2.getModel();

//megszamoljuk mennyi olyan sor van ami nem info es csinalunk egy tempet
        int infsor = 0;

        for (int i = 0; i < b.jTable2.getRowCount(); i++) {
            try {
                if (b.jTable2.getValueAt(i, 3).toString().equals("Infó")) {

                    infsor++;

                }
            } catch (Exception e) {
            }

        }
// megszamoljuk mennyi olyan sor van ami nem info es csinalunk egy tempet
        int infoszlop = 0;

        for (int i = 0; i < b.jTable2.getColumnCount(); i++) {
            try {
                if (b.jTable2.getColumnName(i).equals("Sum: PN,JOB,WS")) {

                    infoszlop++;

                }
            } catch (Exception e) {
            }

        }

        Tc_CellClass[][] temp = null;
        try {
            temp = new Tc_CellClass[b.tablaadat.length][b.tablaadat[1].length];

//teletoljuk üres classokkal és beállítjuk az eredeti értéket ha van
            for (int r = 0; r < temp.length; r++) {

                for (int o = 0; o < temp[1].length; o++) {

                    temp[r][o] = new Tc_CellClass("", 0, 0.0, 0);
//mivel minden cella mögött kell h legyen class , nem szabadna hibára futnia  

                    temp[r][o] = b.tablaadat[r][o];

                }

            }
        } catch (Exception e) {
        }
//átalakítjuk a sheethez tartozó adattárolót a jtablenak megfelelő mérethez

        b.tablaadat = new Tc_CellClass[b.jTable2.getRowCount() - infsor][b.jTable2.getColumnCount() - infoszlop];

        //teletoljuk üres peldanyokkal és visszaírjuk a tempből az adatokat
        for (int r = 0; r < b.tablaadat.length; r++) {

            for (int o = 0; o < b.tablaadat[1].length; o++) {

                b.tablaadat[r][o] = new Tc_CellClass("", 0, 0.0, 0);

                try {
                    b.tablaadat[r][o].value = model.getValueAt(r + infsor, o).toString();
                } catch (Exception e) {
                }
                try {
                    b.tablaadat[r][o].eng = temp[r][o].eng;
                    b.tablaadat[r][o].engtime = temp[r][o].engtime;
                    b.tablaadat[r][o].szin = temp[r][o].szin;

                } catch (Exception e) {
                }

            }

        }

    }

    public void mernoki() {

//megszamoljuk az info sorokat , hogy ki tudjuk vonni a selected row ból
        int infsor = 0;

        for (int i = 0; i < b.jTable2.getRowCount(); i++) {

            if (b.jTable2.getValueAt(i, 3).toString().equals("Infó")) {

                infsor++;

            }

        }

        try {
            if (b.tablaadat[b.jTable2.getSelectedRow() - infsor][b.jTable2.getSelectedColumn()].eng == 0) {
                b.tablaadat[b.jTable2.getSelectedRow() - infsor][b.jTable2.getSelectedColumn()].eng = 1;
            } else {

                b.tablaadat[b.jTable2.getSelectedRow() - infsor][b.jTable2.getSelectedColumn()].eng = 0;
                b.tablaadat[b.jTable2.getSelectedRow() - infsor][b.jTable2.getSelectedColumn()].engtime = 0.0;

            }
        } catch (Exception e) {
        }

    }

    public void adatbatoltbeszursor(int ujsorszam1, int ujsorszam2) {

        //kell egy tablamodell
        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) b.jTable2.getModel();

        //lekapjuk a faszba az info sorokat a modellről
        for (int i = 0; i < model.getRowCount(); i++) {

            if (model.getValueAt(i, 3).toString().equals("Infó")) {

                model.removeRow(i);
                i--;

            }

        }

        //lekapjuk az info oszlopot a modellről
        for (int i = 0; i < model.getColumnCount(); i++) {

            if (model.getColumnName(i).equals("Sum: PN,JOB,WS")) {

                model.setColumnCount(model.getColumnCount() - 1);

            }

        }

//letrehozunk egy temp tarolot , akkorat amekkora az uj tabla merete az info adatok nelkul + 2 ket beszurt sor
        Tc_CellClass[][] temp = new Tc_CellClass[model.getRowCount() + 2][model.getColumnCount()];

//beletoltjuk az adatokat , a vizualist a modelbol , a mernokit a tablaadatbol
//kell egy valtozo az eredeti adat kinyeresehez
        int eredetisor = 0;
        for (int r = 0; r < temp.length; r++) {

            for (int o = 0; o < temp[1].length; o++) {

                temp[r][o] = new Tc_CellClass("", 0, 0.0, 0);

                if (r != ujsorszam1 && r != ujsorszam2) {
                    try {
                        temp[r][o].value = model.getValueAt(eredetisor, o).toString();
                    } catch (Exception e) {
                    }
                    try {
                        temp[r][o].eng = b.tablaadat[eredetisor][o].eng;
                        temp[r][o].engtime = b.tablaadat[eredetisor][o].engtime;
                        temp[r][o].szin = b.tablaadat[eredetisor][o].szin;
                    } catch (Exception e) {
                    }
                }

            }

            if (r != ujsorszam1 && r != ujsorszam2) {
                eredetisor++;
            } else if (r != ujsorszam1) {
                //ha olyan sorban vagyunk amit most adunk hozzá azzal , hogy nem növeljük az eredeti számlálót akkor beírjuk a terv és tény szöveget a megfelelő helyre

                temp[r][3].value = "Tény";

            } else if (r != ujsorszam2) {

                temp[r][3].value = "Terv";

            }

        }

//kell egy uj tablaadat
        b.tablaadat = new Tc_CellClass[temp.length][temp[1].length];
        b.tablaadat = temp.clone();

    }

    public void adatbatoltsortorol(int torol1, int torol2) {

        //kell egy tablamodell
        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) b.jTable2.getModel();

        //lekapjuk a faszba az info sorokat a modellről
        for (int i = 0; i < model.getRowCount(); i++) {

            if (model.getValueAt(i, 3).toString().equals("Infó")) {

                model.removeRow(i);
                i--;

            }

        }

        //lekapjuk az info oszlopot a modellről
        for (int i = 0; i < model.getColumnCount(); i++) {

            if (model.getColumnName(i).equals("Sum: PN,JOB,WS")) {

                model.setColumnCount(model.getColumnCount() - 1);

            }

        }

        //letrehozunk egy temp tarolot , akkorat amekkora az uj tabla merete az info adatok nelkul es levonjuk a ket sor meretet is
        Tc_CellClass[][] temp = new Tc_CellClass[model.getRowCount() - 2][model.getColumnCount()];

        //bejarjuk a temp tablat es beletesszuk a regi adatokat , kiveve ahol torolni akarunk
        //kell egy plussz szamolo hogy meg tudjuk ugrasztani amikor kihagyjuk a torolni kivant sorokat
        int eredetisor = 0;
        for (int r = 0; r < b.tablaadat.length; r++) {
//ha nem azokban a sorokban vagyunk amit törölni szeretnénk
            if (r != torol1 && r != torol2) {
                for (int o = 0; o < b.tablaadat[1].length; o++) {

                    temp[eredetisor][o] = b.tablaadat[r][o];

                }

                eredetisor++;
            }
        }

        b.tablaadat = new Tc_CellClass[temp.length][temp[1].length];
        b.tablaadat = temp.clone();

    }

    public void adatbatoltnezetvaltas() {

        //leterehozunk egy akkora tömböt mint az eredeti és feltöltjük üres classokkal
        Tc_CellClass[][] temp = new Tc_CellClass[b.tablaadat.length][b.tablaadat[1].length];

        for (int r = 0; r < temp.length; r++) {

            for (int o = 0; o < temp[1].length; o++) {

                temp[r][o] = new Tc_CellClass("", 0, 0.0, 0);

            }

        }

        //elkezdjük forgatni az eredeti tömböt és belepakoljuk amit kell a temp-be
        //kell egy változó ami azt mondja meg h kell e új sor vagy nem
        boolean epit = true;

        //kell egy sor amit akkor növelünk ha új sort építünk
        int ujsor = 0;
        String azonosito = "";

        //összerakunk az eredeti tömbböl egy azonositot
        for (int r = 0;
                r < b.tablaadat.length;
                r++) {
            epit = true;
            //kell egy integer , hogy tudjuk melyik sorba kell tenni
            int idetedd = 0;
            azonosito = b.tablaadat[r][0].value + b.tablaadat[r][1].value + b.tablaadat[r][2].value + b.tablaadat[r][3].value;
            //vegigtekerjuk  temp tombot es megnezzuk van e már ilyen sor benne
            for (int i = 0; i < temp.length; i++) {
                String tempadat = temp[i][0].value + temp[i][1].value + temp[i][2].value + temp[i][3].value;
                if (tempadat.equals(azonosito)) {

                    epit = false;

                    idetedd = i;

                }

            }

            //ha nem talatunk meg ilyet akkor be kell rakni a következő üres sorba
            if (epit) {

                temp[ujsor][0] = b.tablaadat[r][0];
                temp[ujsor][1] = b.tablaadat[r][1];
                temp[ujsor][2] = b.tablaadat[r][2];
                temp[ujsor][3] = b.tablaadat[r][3];

                //vegig kell menni az eredeti adat oszlopain es ahol a value nem nulla azt fel kell rakni a temp megfelelő sorába ugyan abba az oszlopba
                for (int n = 4; n < b.tablaadat[1].length; n++) {

                    if (!b.tablaadat[r][n].value.equals("")) {

                        temp[ujsor][n] = b.tablaadat[r][n];

                    }

                }

                ujsor++;

            } else {
                //tehát már van ilyen sorunk , akkor ide be kell tenni az adatokat
                //vegig kell menni az eredeti adat oszlopain es ahol a value nem nulla azt fel kell rakni a temp megfelelő sorába ugyan abba az oszlopba
                for (int n = 4; n < b.tablaadat[1].length; n++) {

                    if (!b.tablaadat[r][n].value.equals("")) {

                        temp[idetedd][n] = b.tablaadat[r][n];

                    }

                }

            }

        }

        //letrehozzuk az uj adattablat
        b.tablaadat = new Tc_CellClass[ujsor][temp[1].length];

        //beletesszük az adatokat
        for (int r = 0;
                r < ujsor;
                r++) {

            for (int o = 0; o < temp[1].length; o++) {

                b.tablaadat[r][o] = temp[r][o];

            }

        }

    }

}
