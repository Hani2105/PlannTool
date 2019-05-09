/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool;

import static PlannTool.Tc_Betervezo.Besheets;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

/**
 *
 * @author gabor_hanacsek
 */
public class Tc_Leker {

    public Tc_Leker(String sheetname, String miindit) {

        int napok = 0;

        Tc_Napszamolo nap = new Tc_Napszamolo();
        if (!Tc_Betervezo.first.equals("") && !Tc_Betervezo.second.equals("")) {
            napok = nap.daysBetweenUsingJoda(Tc_Betervezo.one, Tc_Betervezo.two);
        }

        //System.out.println(napok);
        //hozzaadjuk a napok es a muszakhossznak megfelelo oszlopok szamat a tablahoz
        String neve = sheetname;

        //kitoroljuk az oszlopokat
        DefaultTableModel model = (DefaultTableModel) Besheets.get(neve).jTable2.getModel();
        model.setColumnCount(4);

        //oszlopok neve a datumbol
//        Calendar c = Calendar.getInstance();
        Date dt = new Date();
        dt = Tc_Betervezo.c.getTime();
        org.joda.time.format.DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
        org.joda.time.format.DateTimeFormatter fmtnap = DateTimeFormat.forPattern("E");

        DateTime dtOrg = new DateTime(dt);
        String columneve = "";
        String szak = "";
        String napneve = "";
        TableColumn column = null;

        //napok szamaszor futtatjuk
        for (int i = 0; i < napok; i++) {

            //ha 12 órás a műszakrend 2 szer
            if (Tc_Betervezo.comboertek == 0) {
                for (int k = 0; k < 2; k++) {

                    szak = (k == 0) ? " 06:00" : " 18:00";
                    columneve = fmt.print(dtOrg.plusDays(i)) + szak;

                    napneve = fmtnap.print(dtOrg.plusDays(i));
                    model.addColumn(columneve + " " + napneve);

                }
            }

            //ha 8 órás 3 szor
            if (Tc_Betervezo.comboertek == 1) {
                for (int k = 0; k < 3; k++) {

                    if (k == 0) {

                        szak = " 06:00";

                    } else if (k == 1) {

                        szak = " 14:00";
                    } else {

                        szak = " 22:00";

                    }
                    columneve = fmt.print(dtOrg.plusDays(i)) + szak;
                    napneve = fmtnap.print(dtOrg.plusDays(i));
                    model.addColumn(columneve + " " + napneve);

                }
            }

        }
        //col szelesseg allitas

        //lekerdezzuk az adatbazis adatokat
        String Query = "select tc_terv.date , tc_bepns.partnumber , tc_terv.job , tc_bestations.workstation , tc_terv.qty , tc_terv.tt ,tc_terv.qty_teny , tc_terv.mernoki , tc_terv.mernokiido , tc_terv.cellaszin  \n"
                + "from tc_terv \n"
                + "left join tc_bepns on tc_bepns.idtc_bepns = tc_terv.idtc_bepns \n"
                + "left join tc_bestations on tc_bestations.idtc_bestations = tc_terv.idtc_bestations\n"
                + "left join tc_becells on tc_becells.idtc_cells = tc_terv.idtc_becells\n"
                + "where tc_terv.date between '" + fmt.print(dtOrg) + " 06:00:00" + "' and '" + columneve + ":00" + "' and tc_terv.active = 2 and tc_becells.cellname = '" + neve + "'  and tc_terv.tt = 3  \n"
                + "order by tc_terv.date asc , tc_terv.wtf asc, tc_terv.tt asc , tc_terv.timestamp desc";

        //feldolgozzuk az eredmenyt
        planconnect pc = new planconnect();
        try {
            pc.planconnect(Query);

//<<<<<<<<<<<<<<<<<-----------------------------Ez az új rész --------------------------------------->>>>>>>>>>>>>>>>>>>>>>>>>>>>>
            // lekerdezzük , hogy hány sor van a queryben , hogy be tudjuk állítani az arraynak
            pc.rs.last();
            int utolsosor = pc.rs.getRow();
            pc.rs.beforeFirst();

            // megcsináljuk az arrayt amiben a tábla adatait fogjuk tárolni
            Besheets.get(neve).tablaadat = new Tc_CellClass[utolsosor * 2][model.getColumnCount()];

            //telerakjuk peldanyokkal az üres tablatonbunket
            for (int i = 0; i < utolsosor * 2; i++) {

                for (int n = 0; n < model.getColumnCount(); n++) {

                    Besheets.get(neve).tablaadat[i][n] = new Tc_CellClass("", 0, 0.0, 0);

                }

            }

            //<<<<<<<<<<<<<<<<<-----------------------------Eddig --------------------------------------->>>>>>>>>>>>>>>>>>>>>>>>>>>>>          
            model.setRowCount(0);

            int cellsor = 0;

            //vegigporgetjuk a resultsetet
            while (pc.rs.next()) {

                //porgetjuk az oszlopokat
                for (int i = 0; i < model.getColumnCount(); i++) {

                    //h egyezik a query datuma az oszlop datumaval akkor 
                    try {
                        if (pc.rs.getString(1).equals(model.getColumnName(i).substring(0, model.getColumnName(i).length() - 4) + ":00.0")) {

//a terv es teny darabszamok
                            String terv = "";
                            String teny = "";
                            try {
                                terv = pc.rs.getString(5);
                            } catch (Exception e) {
                                terv = "0";
                            }

                            if (terv.equals("")) {
                                terv = "0";
                            }
                            try {
                                teny = pc.rs.getString(7);
                            } catch (Exception e) {
                                teny = "0";
                            }

                            if (teny.equals("")) {
                                teny = "0";
                            }

                            // hozzaadunk ket sort (tervet es tenyt)
                            //a terv hozzáadása
// betesszuk az adattablaba a terv sort
                            try {
                                Besheets.get(neve).tablaadat[cellsor][0].value = pc.rs.getString(2);
                            } catch (Exception e) {
                            }
                            try {
                                Besheets.get(neve).tablaadat[cellsor][1].value = pc.rs.getString(3);
                            } catch (Exception e) {
                            }
                            try {
                                Besheets.get(neve).tablaadat[cellsor][2].value = pc.rs.getString(4);
                            } catch (Exception e) {
                            }
                            try {
                                Besheets.get(neve).tablaadat[cellsor][3].value = "Terv";
                            } catch (Exception e) {
                            }
                            try {
                                Besheets.get(neve).tablaadat[cellsor][i].value = terv;
                            } catch (Exception e) {
                            }
                            try {
                                Besheets.get(neve).tablaadat[cellsor][i].eng = pc.rs.getInt(8);
                            } catch (Exception e) {
                            }
                            try {
                                Besheets.get(neve).tablaadat[cellsor][i].engtime = pc.rs.getDouble(9);
                            } catch (Exception e) {
                            }
                            try {
                                Besheets.get(neve).tablaadat[cellsor][i].szin = pc.rs.getInt(10);
                            } catch (Exception e) {
                            }
// betesszuk az adattablaba a teny sort
                            cellsor++;

                            try {
                                Besheets.get(neve).tablaadat[cellsor][0].value = pc.rs.getString(2);
                            } catch (Exception e) {
                            }
                            try {
                                Besheets.get(neve).tablaadat[cellsor][1].value = pc.rs.getString(3);
                            } catch (Exception e) {
                            }
                            try {
                                Besheets.get(neve).tablaadat[cellsor][2].value = pc.rs.getString(4);
                            } catch (Exception e) {
                            }
                            try {
                                Besheets.get(neve).tablaadat[cellsor][3].value = "Tény";
                            } catch (Exception e) {
                            }
                            try {
                                Besheets.get(neve).tablaadat[cellsor][i].value = teny;
                            } catch (Exception e) {
                            }
                            try {
                                Besheets.get(neve).tablaadat[cellsor][i].eng = pc.rs.getInt(8);
                            } catch (Exception e) {
                            }
                            try {
                                Besheets.get(neve).tablaadat[cellsor][i].szin = pc.rs.getInt(10);
                            } catch (Exception e) {
                            }

                            model.addRow(new Object[model.getColumnCount()]);

//a tény hozzáadása
                            model.addRow(new Object[model.getColumnCount()]);

                        }
                    } catch (Exception e) {
                    }

                }

                cellsor++;

            }

        } catch (SQLException ex) {
            Logger.getLogger(Tc_Betervezo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Tc_Betervezo.class.getName()).log(Level.SEVERE, null, ex);
        }

        pc.kinyir();

//bealitjuk az adatokat a tablaba
        Tc_AdatInterface a = new Tc_AdatInterface(Besheets.get(neve));
        a.tablabatolt();
        Tc_Calculator calc = new Tc_Calculator(Besheets.get(neve), false, 0);
        calc.run();

        //lekérdezzük a job adatokat
        Tc_Jobinfotoplan j = new Tc_Jobinfotoplan(Besheets.get(neve));
        j.start();

        if (ablak.planner == true && miindit.equals("mentes")) {

            //megkérdezzük , hogy akarunk e levelet küldeni a terv változásról
            JDialog.setDefaultLookAndFeelDecorated(true);
            int response = JOptionPane.showConfirmDialog(null, "Küldjünk levelet a terv változásról?", "Levélküldés",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (response == JOptionPane.NO_OPTION) {
                //System.out.println("No button clicked");
            } else if (response == JOptionPane.YES_OPTION) {
                //System.out.println("Yes button clicked");
                Tc_Tervvaltozasszal t = new Tc_Tervvaltozasszal(Besheets.get(neve).jTable2, neve);
                t.start();

            } else if (response == JOptionPane.CLOSED_OPTION) {
                System.out.println("JOptionPane closed");
            }

        }

        //visszaallitjuk a nezetvaltas figyelojet
        Tc_Betervezo.nezetvaltas = true;

    }

}
