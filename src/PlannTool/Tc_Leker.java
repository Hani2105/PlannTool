/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool;

import static PlannTool.Tc_Betervezo.Besheets;
import static PlannTool.Tc_Betervezo.jComboBox1;
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

    public Tc_Leker(String sheetname , String miindit) {

        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        String first = "";
        String second = "";

        try {
            first = df.format(Tc_Betervezo.jDateChooser1.getDate());
            second = df.format(Tc_Betervezo.jDateChooser2.getDate());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Date one = null;
        Date two = null;
        int napok = 0;
        if (!first.equals("") && !second.equals("")) {
            try {
                one = df.parse(first);
            } catch (ParseException ex) {
                Logger.getLogger(Tc_Betervezo.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                two = df.parse(second);
            } catch (ParseException ex) {
                Logger.getLogger(Tc_Betervezo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        Tc_Napszamolo nap = new Tc_Napszamolo();
        if (!first.equals("") && !second.equals("")) {
            napok = nap.daysBetweenUsingJoda(one, two);
        }

        //System.out.println(napok);
        //hozzaadjuk a napok es a muszakhossznak megfelelo oszlopok szamat a tablahoz
        String neve = sheetname;

        //kitoroljuk az oszlopokat
        DefaultTableModel model = (DefaultTableModel) Besheets.get(neve).jTable2.getModel();
        model.setColumnCount(4);

        //oszlopok neve a datumbol
        Calendar c = Calendar.getInstance();
        c.setTime(Tc_Betervezo.jDateChooser1.getDate());
        Date dt = new Date();
        dt = c.getTime();
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
            if (jComboBox1.getSelectedIndex() == 0) {
                for (int k = 0; k < 2; k++) {

                    szak = (k == 0) ? " 06:00" : " 18:00";
                    columneve = fmt.print(dtOrg.plusDays(i)) + szak;

                    napneve = fmtnap.print(dtOrg.plusDays(i));
                    model.addColumn(columneve + " " + napneve);

                }
            }

            //ha 8 órás 3 szor
            if (jComboBox1.getSelectedIndex() == 1) {
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
        String Query = "select tc_terv.date , tc_bepns.partnumber , tc_terv.job , tc_bestations.workstation , tc_terv.qty , tc_terv.tt ,tc_terv.qty_teny \n"
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
            model.setRowCount(0);
            int r = 0;

            //vegigporgetjuk a resultsetet
            while (pc.rs.next()) {

                //porgetjuk az oszlopokat
                for (int i = 4; i < model.getColumnCount(); i++) {

                    //h egyezik a query datuma az oszlop datumaval akkor 
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
                        model.addRow(new Object[]{pc.rs.getString(2), pc.rs.getString(3), pc.rs.getString(4), "Terv"});
                        model.setValueAt(terv, model.getRowCount() - 1, i);
//a tény hozzáadása
                        model.addRow(new Object[]{pc.rs.getString(2), pc.rs.getString(3), pc.rs.getString(4), "Tény"});
                        model.setValueAt(teny, model.getRowCount() - 1, i);
                        r++;

                    }

                }

            }

        } catch (SQLException ex) {
            Logger.getLogger(Tc_Betervezo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Tc_Betervezo.class.getName()).log(Level.SEVERE, null, ex);
        }

        pc.kinyir();
        Besheets.get(neve).jTable2.setModel(model);
        Tc_Calculator calc = new Tc_Calculator(Besheets.get(neve), false, 0);

        if (ablak.planner == true && miindit.equals("mentes")) {

            //megkérdezzük , hogy akarunk e levelet küldeni a terv változásról
            JDialog.setDefaultLookAndFeelDecorated(true);
            int response = JOptionPane.showConfirmDialog(null, "Küldjünk levelet a terv változásról?", "Levélküldés",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (response == JOptionPane.NO_OPTION) {
                //System.out.println("No button clicked");
            } else if (response == JOptionPane.YES_OPTION) {
                //System.out.println("Yes button clicked");
                Tc_Tervváltozásszál t = new Tc_Tervváltozásszál(Besheets.get(neve).jTable2, neve);
                t.start();

            } else if (response == JOptionPane.CLOSED_OPTION) {
                System.out.println("JOptionPane closed");
            }

        }

    }

}
