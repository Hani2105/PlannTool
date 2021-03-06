/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool.BACKEND;

import PlannTool.ablak;
import java.awt.Color;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ToolTipManager;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author gabor_hanacsek
 */
public class Tc_TervTooltipRenderer extends DefaultTableCellRenderer {

    Tc_Besheet b;

    public Tc_TervTooltipRenderer(Tc_Besheet b) {

        this.b = b;

        //eltesszuk az oszlop szelessegeket
        Tc_Betervezo.szelessegek.clear();
        for (int i = 0; i < b.jTable2.getColumnCount(); i++) {

            Tc_Betervezo.szelessegek.add(b.jTable2.getColumnModel().getColumn(i).getWidth());

        }

    }

    ;
    
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        //a tooltip eltünés és előjövetel ideje
        // Get current delay
        int initialDelay = ToolTipManager.sharedInstance().getInitialDelay();

        // Show tool tips immediately
        ToolTipManager.sharedInstance().setInitialDelay(0);
        ToolTipManager.sharedInstance().setDismissDelay(9999999);

        JLabel c = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        String tooltiptext = "";

        //megszamoljuk az info sorokat , hogy ki tudjuk vonni a selected row ból
        int infsor = 0;

        for (int i = 0; i < b.jTable2.getRowCount(); i++) {

            if (b.jTable2.getValueAt(i, 3).toString().equals("Infó")) {

                infsor++;

            }

        }
//a border beállítása
//ta terv sorban vagyunk akkor felül

        if (table.getValueAt(row, 3).toString().equals("Terv")) {

            //c.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.orange), BorderFactory.createEtchedBorder(1)));
            //c.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.black));
            c.setHorizontalAlignment(CENTER);
            c.setIcon(null);
            c.setToolTipText(null);
        }

//ha tény sorban vagyunk
        if (table.getValueAt(row, 3).toString().equals("Tény")) {

            //c.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.orange), BorderFactory.createEtchedBorder(1)));
            //c.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black));
            c.setHorizontalAlignment(CENTER);
            c.setIcon(null);
            c.setToolTipText(null);
        }

//kezdjük azzal , hogy kiszinezünk minden sort annak megfelelően , hogy terv vagy tény vagy info
        //infó sorok színezése
        if (table.getValueAt(row, 3).toString().equals("Infó") && column == 3) {

            c.setBackground(Color.YELLOW);
            c.setIcon(null);
            c.setToolTipText(null);

        } //ha info sorban vagyunk de nem a 3. oszlopban
        else if (table.getValueAt(row, 3).toString().equals("Infó") && column != 3) {

            c.setBackground(Color.WHITE);

        } //terv sorok szinezése
        else if (table.getValueAt(row, 3).toString().equals("Terv")) {

            c.setBackground(new Color(Tc_Betervezo.slide1, Tc_Betervezo.slide2, Tc_Betervezo.slide3));
            c.setIcon(null);
            c.setToolTipText(null);

        } //teny sorok szinezese
        else if (table.getValueAt(row, 3).toString().equals("Tény")) {

            c.setBackground(new Color(Tc_Betervezo.slide4, Tc_Betervezo.slide5, Tc_Betervezo.slide6));
            c.setIcon(null);
            c.setToolTipText(null);

        }

//job szinezes , ha találunk még ilyen cella értéket szinezünk
        try {
            if ((column == 1 || column == 0 || column == 2) && table.getValueAt(row, 1).toString().equals(b.jobaszinezeshez) && !b.jobaszinezeshez.equals("")) {

                c.setBackground(new Color(241, 252, 35));
                c.setForeground(new Color(0, 0, 0));
                //c.setBorder(BorderFactory.createLineBorder(new Color(232, 11, 44), 2));

            }
        } catch (Exception e) {
        }

//jöhet a kalkulátor színezés
        //calculátor zöld színe ha hagyobb vagy egyenlő a tény a tervnél és nem nulla és már a terv reszben vagyunk
        try {
            if (column > 3 && Integer.parseInt(table.getValueAt(row, table.getColumnCount() - 1).toString()) != 0 && table.getValueAt(row, 3).equals("Tény") && ((Integer.parseInt(table.getValueAt(row, table.getColumnCount() - 1).toString())) >= (Integer.parseInt(table.getValueAt(row - 1, table.getColumnCount() - 1).toString())))) {

                c.setBackground(new Color(Tc_Betervezo.slide7, Tc_Betervezo.slide8, Tc_Betervezo.slide9));
                c.setIcon(null);
                c.setToolTipText(null);

            } else if (column > 3 && (Integer.parseInt(table.getValueAt(row, table.getColumnCount() - 1).toString()) > 0 || Integer.parseInt(table.getValueAt(row + 1, table.getColumnCount() - 1).toString()) > 0) && table.getValueAt(row, 3).equals("Terv") && ((Integer.parseInt(table.getValueAt(row, table.getColumnCount() - 1).toString())) <= (Integer.parseInt(table.getValueAt(row + 1, table.getColumnCount() - 1).toString())))) {

                c.setBackground(new Color(Tc_Betervezo.slide7, Tc_Betervezo.slide8, Tc_Betervezo.slide9));
                c.setIcon(null);
                c.setToolTipText(null);

            }
        } catch (Exception e) {
        }

//jöhetnek a pn színek
////pn szinezes , narancs ha nincs az adott cellahoz
        if (column == 0 && (table.getValueAt(row, 0) != null && !table.getValueAt(row, 0).toString().equals(""))) {

            boolean narancs = true;

            for (int i = 0; i < b.partnumbers.size(); i++) {

                if (table.getValueAt(row, 0).toString().equals(b.partnumbers.get(i)[0])) {

                    narancs = false;

                }

            }

//pn szinezese pirosra ha nincs az adatbazisban!
            boolean piros = true;
            for (int i = 0; i < Tc_Betervezo.partn.size(); i++) {

                if (table.getValueAt(row, 0).toString().equals(Tc_Betervezo.partn.get(i))) {

                    piros = false;

                }

            }

//ha nincs ciklusido szurke
            boolean cian = true;

            for (int n = 0; n < Tc_Betervezo.ciklusidok.get(0).length; n++) {
                try {
                    if (Tc_Betervezo.ciklusidok.get(0)[n][0].equals(Tc_Betervezo.Tervezotabbed.getTitleAt(Tc_Betervezo.Tervezotabbed.getSelectedIndex())) && Tc_Betervezo.ciklusidok.get(0)[n][1].equals(table.getValueAt(row, 0).toString()) && Tc_Betervezo.ciklusidok.get(0)[n][2].equals(table.getValueAt(row, 2).toString())) {

                        cian = false;

                    }
                } catch (Exception e) {
                }

            }

            if (piros == true) {

                c.setBackground(Color.RED);
                c.setIcon(null);
                c.setToolTipText(null);

            } else if (narancs == true) {

                c.setBackground(Color.ORANGE);
                c.setIcon(null);
                c.setToolTipText(null);

            } else if (cian == true) {

                c.setBackground(Color.LIGHT_GRAY);
                c.setIcon(null);
                c.setToolTipText(null);

            }

//megnezzuk mikor gyartottuk utoljara ezt a pn-t
            try {
                for (int i = 0; i < b.utsogyartasok.length; i++) {
                    try {
                        if (table.getValueAt(row, 0).toString().equals(b.utsogyartasok[i][2].toString())) {
                            tooltiptext = "<html>A terméket " + b.utsogyartasok[i][1] + " napja gyártottuk!";
                            c.setToolTipText(tooltiptext);
                            if (Integer.parseInt(b.utsogyartasok[i][1]) >= 90) {
                                c.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/olds.png")));
                            }
                            break;

                        }

                    } catch (Exception e) {
                    }

                }
//ha nincs tooltiptext akkor az azt jelenti h meg nem gyartottuk
                if (tooltiptext.length() == 0) {
                    tooltiptext = "<html>Nem gyártottuk még!";
                    c.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/news.png")));
                    c.setToolTipText(tooltiptext);
                }

            } catch (Exception ex) {
            }

        }

        //jöhet a ws színezés
        //ws szinezes , narancs ha nincs az adott cellaban
        if (column == 2 && (table.getValueAt(row, 2) != null && !table.getValueAt(row, 2).toString().equals("")) && !table.getValueAt(row, 3).toString().equals("Infó")) {

            boolean narancs = true;

            for (int i = 0; i < b.workstations.size(); i++) {

                if (table.getValueAt(row, 2).toString().equals(b.workstations.get(i))) {

                    narancs = false;

                }

            }
// ha nem letezik a ws egyaltalan piros

            boolean piros = true;
            for (int i = 0; i < Tc_Betervezo.works.size(); i++) {

                if (table.getValueAt(row, 2).toString().equals(Tc_Betervezo.works.get(i))) {

                    piros = false;

                }

            }

//ha nincs ciklusido szurke
            boolean cian = true;

            for (int n = 0; n < Tc_Betervezo.ciklusidok.get(0).length; n++) {
                try {
                    if (Tc_Betervezo.ciklusidok.get(0)[n][0].equals(Tc_Betervezo.Tervezotabbed.getTitleAt(Tc_Betervezo.Tervezotabbed.getSelectedIndex())) && Tc_Betervezo.ciklusidok.get(0)[n][1].equals(table.getValueAt(row, 0).toString()) && Tc_Betervezo.ciklusidok.get(0)[n][2].equals(table.getValueAt(row, 2).toString())) {

                        cian = false;

                    }
                } catch (Exception e) {
                }

            }

            if (piros == true) {

                c.setBackground(Color.RED);
                c.setIcon(null);
                c.setToolTipText(null);

            } else if (narancs == true) {

                c.setBackground(Color.ORANGE);
                c.setIcon(null);
                c.setToolTipText(null);

            } else if (cian == true) {

                c.setBackground(Color.LIGHT_GRAY);
                c.setIcon(null);
                c.setToolTipText(null);

            }

        }

//tooltip beállítása + ikon hozzáadása ha van megjegyzés a pn hez
        //komment a pn hez ha van és tervező van bent
        if (column == 0 && table.getValueAt(row, 0) != null && !table.getValueAt(row, 0).toString().equals("")) {

            for (int i = 0; i < b.partnumbers.size(); i++) {
                //System.out.println(b.partnumbers.get(i)[0]);
                if (b.partnumbers.get(i)[0].equals(table.getValueAt(row, 0).toString()) && b.partnumbers.get(i)[2] != null && !b.partnumbers.get(i)[2].equals("") && ablak.planner) {

                    tooltiptext += "<br>Komment: " + b.partnumbers.get(i)[2] + "</html>";
                    if (c.getIcon() == null) {
                        c.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/comment.jpg")));
                    } //ha be van állítva a régi icon
                    else if (c.getIcon().toString().equals(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/olds.png")).toString())) {
                        c.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/oldcomment.jpg")));

                    } //ha be van állítva az uj icon
                    else if (c.getIcon().toString().equals(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/news.png")).toString())) {
                        c.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/newcomment.jpg")));

                    }

                    c.setToolTipText(tooltiptext);
                    break;

                }

            }

        }

// tooltip beállítása ha kint vagyunk a terv részen
        if (column > 3 && (table.getValueAt(row, 0) != null && table.getValueAt(row, 2) != null) && table.getValueAt(row, column) != null && !table.getValueAt(row, column).toString().equals("")) {

            String komment = "";
            try {
                Tc_Stringbolint si = new Tc_Stringbolint(table.getValueAt(row, column).toString());
                tooltiptext = ("<html>" + "Terv/Tény: " + table.getValueAt(row, 3).toString() + "<br>" + "PN: " + table.getValueAt(row, 0).toString() + "<br>" + "JOB: " + table.getValueAt(row, 1).toString() + "<br>" + "WS: " + table.getValueAt(row, 2).toString());
                komment = si.komment;
                tooltiptext += "<br> Komment: " + komment + "</br>";
                //megkeressük a mérnöki időt
                tooltiptext += "<br> Mérnöki idő: " + String.valueOf(b.tablaadat[row - infsor][column].engtime) + "</html>";
                c.setToolTipText(tooltiptext);
                c.setIcon(null);

            } catch (Exception e) {

            }
        }
//
//       
//
//released ikon beállítása a jobszámok mellé és tooltip
        try {
            if (column == 1 && !table.getValueAt(row, 1).toString().equals("") && !table.getValueAt(row, 3).toString().equals("Infó") && table.getValueAt(row, 3) != null) {

//végigtekerjük a sheet jobadatait és ha megtaláljuk a JOB ot akkor pipa , ha nem akko warn
                String ok = "C";
                String jobtooltip = "<html>";

                for (int i = 0; i < b.jobadat.get(0).length; i++) {
//ha egyezik a jobszam es nem N a statusz , azaz released
                    if (table.getValueAt(row, 1).toString().trim().equals(b.jobadat.get(0)[i][0]) && !b.jobadat.get(0)[i][3].equals("N")) {

                        ok = "R";
                        String location = "Skeleton/TP15";

//ha az unit status unit component akkor átírjuk arra 
                        if (b.jobadat.get(0)[i][4].equals("Unit Component")) {

                            location = "Unit Component";

                        }

//ha nem üres a workstation és nem null akkor felvesszuk lokációnak az ott szereplő állomást
                        if (!b.jobadat.get(0)[i][1].toString().equals("") && b.jobadat.get(0)[i][1] != null) {

                            location = b.jobadat.get(0)[i][1].toString();
                        }
//ha nem írtuk át akkor ugye marad a skeleton tp 15 , és hozzácsapjuk a darabot , ha átrtuk akkor is hozzácsapjuk a darabot
                        jobtooltip += "Location: " + location + " QTY: " + b.jobadat.get(0)[i][2].toString() + "<br>";

                    }
//ha pedig a job státusza not released
                    if (table.getValueAt(row, 1).toString().trim().equals(b.jobadat.get(0)[i][0]) && b.jobadat.get(0)[i][3].equals("N")) {

                        c.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/n.png")));
                        c.setToolTipText("Not released in 42Q!");
                        ok = "N";

                    }

                }
//ha released beállítjuk az ikont es a tooltipet ha nincs a jobtooltip szövegben a skeleton szócska
                if (ok.equals("R") && !jobtooltip.contains("Skeleton")) {
                    c.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/ok1.png")));
                    c.setToolTipText(jobtooltip);

                } //ámde ha benne van a skeleton szócska akkor más ikont állítunk neki
                else if (ok.equals("R") && jobtooltip.contains("Skeleton")) {

                    c.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/charge1.png")));
                    c.setToolTipText(jobtooltip);

                } //ha nem letezik akkor beallitjuk az ikont es a tooltipet               
                else if (ok.equals("C")) {

                    c.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/warn1.png")));
                    c.setToolTipText("Does not exist in 42Q!");
                }

            }
        } catch (Exception e) {
        }

//
// beallitjuk a mernoki sapkat ha mernoki ha mernoki
        try {

            //ha a terv részen vagyunk
            if (column > 3 && !table.getValueAt(row, 3).toString().equals("Infó") && table.getValueAt(row, 3).toString().equals("Terv")) {

                if (b.tablaadat[row - infsor][column].engtime > 0) {

                    c.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/eng1.png")));

                }

            }

        } catch (Exception e) {
        }
        //ha tény részen vagyunk beállítjuk az anyaghiányt vagy az állásidőt vagy mindkettőt

        try {

            //ha a tény részen vagyunk 
            if (column > 3 && !table.getValueAt(row, 3).toString().equals("Infó") && table.getValueAt(row, 3).toString().equals("Tény")) {

                //beallitjuk az ikont  ha anyaghianyos de nincs állásidő
                if ((!b.tablaadat[row - infsor][column].ah.equals("") || !b.tablaadat[row - infsor - 1][column].ah.equals("")) && (b.tablaadat[row - infsor][column].all == 0 && b.tablaadat[row - infsor - 1][column].all == 0)) {

                    c.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/ah.jpg")));

                } //ha van anyaghiány is és van állásidő is
                else if ((!b.tablaadat[row - infsor][column].ah.equals("") || !b.tablaadat[row - infsor - 1][column].ah.equals("")) && (b.tablaadat[row - infsor][column].all != 0 || b.tablaadat[row - infsor - 1][column].all != 0)) {

                    c.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/skullka.png")));

                } //ha nincs anyaghiány de van állásidő 
                else if ((b.tablaadat[row - infsor][column].ah.equals("") && b.tablaadat[row - infsor - 1][column].ah.equals("")) && (b.tablaadat[row - infsor][column].all != 0 || b.tablaadat[row - infsor - 1][column].all != 0)) {

                    c.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/dtke.png")));

                }

            }

        } catch (Exception e) {
        }

//
//beállítjuk a cella színét ha a terv részen vagyunk
        try {

            //ha a terv részen vagyunk
            if (column > 3 && !table.getValueAt(row, 3).toString().equals("Infó")) {

                if (b.tablaadat[row - infsor][column].szin != 0) {
//itt össze kell rakni a szint
                    c.setBackground(new Color(b.tablaadat[row - infsor][column].szin));

                }
            }

        } catch (Exception e) {
        }

        //ha job cella van kijelölve   
        if (isSelected) {
//felvesszük a jobszámot
            try {
                b.jobaszinezeshez = table.getValueAt(row, 1).toString();
                table.repaint();
            } catch (Exception e) {
            }

        }

//ha ki van jelölve a cella
        if (isSelected && (column != 1 || column != 2 || column != 0)) {

            //c.setBackground(new Color(220, 224, 232));
            c.setForeground(new Color(0, 0, 0));
            c.setBorder(BorderFactory.createLineBorder(new Color(232, 11, 44), 2));

        }

        return c;

    }

}
