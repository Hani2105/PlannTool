/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool;

import static PlannTool.ablak.jCheckBox2;
import static PlannTool.ablak.jTable3;
import static PlannTool.ablak.model;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author gabor_hanacsek
 */
public class activityszuro {

    public void listabolszurok() {

//az állomás jtable adatait kiszedjük egy listbe
        List<String> allomasok = new ArrayList<String>();

        for (int i = 0; i < ablak.jTable18.getRowCount(); i++) {

            if (ablak.jTable18.getValueAt(i, 0) != null && !ablak.jTable18.getValueAt(i, 0).toString().equals("")) {

                allomasok.add(ablak.jTable18.getValueAt(i, 0).toString());

            }

        }

//a prefixek jtable adatait kiszedjük egy listbe
        List<String> prefixek = new ArrayList<String>();

        for (int i = 0; i < ablak.jTable19.getRowCount(); i++) {

            if (ablak.jTable19.getValueAt(i, 0) != null && !ablak.jTable19.getValueAt(i, 0).toString().equals("")) {

                prefixek.add(ablak.jTable19.getValueAt(i, 0).toString());

            }

        }

        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) jTable3.getModel();

//kiszedjuk a modellbol ami nem szerepel az allomaslista és a prefixlista adatokban
        int sorszam = model.getRowCount();
        for (int i = 0; i < sorszam; i++) {
//ezzel figyeljuk hogy vettünk e el sort a modellbol
            boolean minuszprefix = true;
            boolean minuszallomas = true;

// ha van az állomás listában valami
            if (allomasok.size() > 0) {

                for (int a = 0; a < allomasok.size(); a++) {

//ha tartalmazza az eredeti modellben levo adat a listaban szereplo stringet nem bántjuk , ha nem , kiszedjük 
                    if (model.getValueAt(i, 0).toString().contains(allomasok.get(a))) {

                        minuszallomas = false;
                    }

                }

            } //ha üres az állomás lista átállítjuk falsera false -ra

            if (allomasok.size() == 0) {

                minuszallomas = false;

            }

//kiszedjuk a modelbol a prefix lista adatait is          
            if (prefixek.size() > 0) {

                for (int a = 0; a < prefixek.size(); a++) {

//ha tartalmazza az eredeti modellben levo adat a listaban szereplo stringet nem bántjuk , ha nem , kiszedjük 
                    if (model.getValueAt(i, 1).toString().contains(prefixek.get(a))) {

                        minuszprefix = false;
                    }

                }

            } //ha üres az állomás lista átállítjuk falsera false -ra

            if (prefixek.size() == 0) {

                minuszprefix = false;

            }

// ha minusz true akkor levonunk egy i-t , hogy jó legyen nekünk és elvesszük a sort
            if (minuszprefix == true || minuszallomas == true) {

                model.removeRow(i);
                i--;
                sorszam--;
            }

        }

//beallitjuk a tablanak
        jTable3.setModel(model);

    }

    public void visszaallit() {

        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) jTable3.getModel();

        model.setRowCount(0);

//visszatesszuk a modelbe az eredeti adatokat
        try {
            for (int i = 0; i < ablak.modelactilist.get(0).length; i++) {

                model.addRow(new Object[]{ablak.modelactilist.get(0)[i][0], ablak.modelactilist.get(0)[i][1], ablak.modelactilist.get(0)[i][2], ablak.modelactilist.get(0)[i][3], ablak.modelactilist.get(0)[i][4], ablak.modelactilist.get(0)[i][5], ablak.modelactilist.get(0)[i][6]});

            }
        } catch (Exception e) {
        }

//beallitjuk a tablanak
        jTable3.setModel(model);

    }

    public void activitygroup() {

        if (jCheckBox2.isSelected()) {

            //lefuttatjuk a szűrőket
            //kiszedjuk a modelbol az adatokat
            DefaultTableModel model = new DefaultTableModel();
            model = (DefaultTableModel) jTable3.getModel();

            //bedolgozzuk az adatokat egy tömbbe
            List<String[]> grouplist = new ArrayList<>();
//vegigporgetjuk a modelt
            for (int i = 0; i < model.getRowCount(); i++) {
                boolean pluszsor = true;

//megprobalun k egyezoseget keresni az osszerakott adatokben már
                for (int k = 0; k < grouplist.size(); k++) {

//ha egyezik a pn és az állomás összeadjuk a darabot
                    if (grouplist.get(k)[0].equals(model.getValueAt(i, 0).toString()) && grouplist.get(k)[1].equals(model.getValueAt(i, 1).toString())) {

                        int passz = Integer.parseInt(grouplist.get(k)[2]) + Integer.parseInt(model.getValueAt(i, 2).toString());
                        int sumpassz = Integer.parseInt(grouplist.get(k)[4]) + Integer.parseInt(model.getValueAt(i, 4).toString());
                        int moveqty = Integer.parseInt(grouplist.get(k)[5]) + Integer.parseInt(model.getValueAt(i, 5).toString());
                        int mmanualmove = Integer.parseInt(grouplist.get(k)[6]) + Integer.parseInt(model.getValueAt(i, 6).toString());

                        grouplist.get(k)[2] = String.valueOf(passz);
                        grouplist.get(k)[4] = String.valueOf(sumpassz);
                        grouplist.get(k)[5] = String.valueOf(moveqty);
                        grouplist.get(k)[6] = String.valueOf(moveqty);
//ebben az esetben nem kell plusz sort hozzaadni a grouplisthez
                        pluszsor = false;

                    }

                }

//ha pluszsor true akkor adunk hozzá egy uj sort
                if (pluszsor == true) {

                    String[] adatok = new String[7];

                    adatok[0] = model.getValueAt(i, 0).toString();
                    adatok[1] = model.getValueAt(i, 1).toString();
                    adatok[2] = model.getValueAt(i, 2).toString();
                    adatok[3] = model.getValueAt(i, 3).toString();
                    adatok[4] = model.getValueAt(i, 4).toString();
                    adatok[5] = model.getValueAt(i, 5).toString();
                    adatok[6] = model.getValueAt(i, 6).toString();

                    grouplist.add(adatok);

                }

            }

//itt vegeztunk a modell vegigporgetesevel
//a modellt atalakitjuk az uj adatoknak megfeleloen
            model.setRowCount(0);

            for (int i = 0; i < grouplist.size(); i++) {

                model.addRow(new Object[]{grouplist.get(i)[0],grouplist.get(i)[1],grouplist.get(i)[2],"Nem értelmezhető",grouplist.get(i)[4],grouplist.get(i)[5],grouplist.get(i)[6]});
                
            }
            
//beállítjuk a táblának a modellt
         jTable3.setModel(model);

        }

    }

}
