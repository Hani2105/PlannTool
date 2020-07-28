/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool.BACKEND;

import PlannTool.ablak;
import PlannTool.CONNECTS.planconnect;
import static PlannTool.BACKEND.Tc_Betervezo.Besheets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author gabor_hanacsek
 */
public class Tc_Ment {

    public Tc_Ment() {

        String query = "select * from tc_becells";
        planconnect pc = new planconnect();
        List<String[][]> gyujto = new ArrayList<String[][]>();
        gyujto.clear();
//felengedjük a szűrőt

        try {
            int i = 0;
            pc.lekerdez(query);
            pc.rs.last();
            int last = pc.rs.getRow();
            pc.rs.beforeFirst();
            String[][] cellist = new String[last][2];

            while (pc.rs.next()) {

                cellist[i][0] = pc.rs.getString(1);
                cellist[i][1] = pc.rs.getString(2);

                i++;

            }

            gyujto.add(cellist);

        } catch (SQLException ex) {
            Logger.getLogger(Tc_Betervezo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Tc_Betervezo.class.getName()).log(Level.SEVERE, null, ex);
        }

        //wsek
        query = "select * from tc_bestations";

        try {
            int i = 0;
            pc.lekerdez(query);
            pc.rs.last();
            int last = pc.rs.getRow();
            pc.rs.beforeFirst();
            String[][] wslist = new String[last][2];

            while (pc.rs.next()) {

                wslist[i][0] = pc.rs.getString(1);
                wslist[i][1] = pc.rs.getString(2);

                i++;

            }

            gyujto.add(wslist);

        } catch (SQLException ex) {
            Logger.getLogger(Tc_Betervezo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Tc_Betervezo.class.getName()).log(Level.SEVERE, null, ex);
        }

        //pnek
        query = "select * from tc_bepns";

        try {
            int i = 0;
            pc.lekerdez(query);
            pc.rs.last();
            int last = pc.rs.getRow();
            pc.rs.beforeFirst();
            String[][] pnlist = new String[last][2];

            while (pc.rs.next()) {

                pnlist[i][0] = pc.rs.getString(1);
                pnlist[i][1] = pc.rs.getString(2);

                i++;

            }

            gyujto.add(pnlist);

        } catch (SQLException ex) {
            Logger.getLogger(Tc_Betervezo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Tc_Betervezo.class.getName()).log(Level.SEVERE, null, ex);
        }
        //kitalaljuk a sheet nevet
        int n = Tc_Betervezo.Tervezotabbed.getSelectedIndex();
        String neve = Tc_Betervezo.Tervezotabbed.getTitleAt(n);

//felengedjük a szűrőt
        Besheets.get(neve).jTable2.setRowSorter(null);

        String tol = Besheets.get(neve).jTable2.getColumnName(4).substring(0, 10) + " 06:00:00";
        String ig = "";

        //tol ig intervallum a sheeten
        if (Besheets.get(neve).jTable2.getColumnName(Besheets.get(neve).jTable2.getColumnCount() - 2).substring(11, 12).equals("2")) {

            ig += Besheets.get(neve).jTable2.getColumnName(Besheets.get(neve).jTable2.getColumnCount() - 2).substring(0, 10) + " 22:00:00";

        } else {

            ig += Besheets.get(neve).jTable2.getColumnName(Besheets.get(neve).jTable2.getColumnCount() - 2).substring(0, 10) + " 18:00:00";

        }

        //megkeressuk az id -kat es vegigmegyunk a tablan , oszlop , sor
        DefaultTableModel t2 = new DefaultTableModel();
        t2 = (DefaultTableModel) Besheets.get(neve).jTable2.getModel();
        int oszlopszam = t2.getColumnCount();  //t2 a tervtabla 
        int sorszam = t2.getRowCount();

        String feltoltadat = "";
        String cellid = "";

        //megszamoljuk az info sorokat , hogy ki tudjuk vonni a selected row ból
        int infsor = 0;

        for (int v = 0; v < Besheets.get(neve).jTable2.getRowCount(); v++) {

            if (Besheets.get(neve).jTable2.getValueAt(v, 3).toString().equals("Infó")) {

                infsor++;

            }

        }
        // elinditjuk a nagy ciklust , az oszlopok szama ()
        for (int i = 0; i < oszlopszam; i++) {

            //kis ciklus , sorok szama
            for (int r = 0; r < sorszam; r++) {

                String pn = "";
                String ws = "";
                String cell = "";
                String job = "";
                String pnid = "";
                String wsid = "";

                //ha nem infó a sor
                if (!t2.getValueAt(r, 3).equals("Infó")) {

                    pnid = "";
                    wsid = "";

                    //ha nem nullák a pn , job , ws
                    if (t2.getValueAt(r, 0) != null) {
                        pn = t2.getValueAt(r, 0).toString();
                    }
                    if (t2.getValueAt(r, 1) != null) {
                        job = t2.getValueAt(r, 1).toString();
                    }

                    if (job.equals("") && t2.getValueAt(r, 3).toString().equals("Tény")) {
                        try {
                            job = t2.getValueAt(r - 1, 1).toString();
                        } catch (Exception e) {
                        }

                    }

                    if (t2.getValueAt(r, 2) != null) {
                        ws = t2.getValueAt(r, 2).toString();
                    }
                    cell = neve;

                    // megallapitjuk az id -kat (pn,cella,ws) a lekerdezeshez
                    //pnid  
                    for (int k = 0; k < gyujto.get(2).length; k++) {

                        if (pn.equals(gyujto.get(2)[k][1])) {

                            pnid = gyujto.get(2)[k][0];
                            break;

                        }

                    }

                    //wsid
                    for (int k = 0; k < gyujto.get(1).length; k++) {

                        if (ws.equals(gyujto.get(1)[k][1])) {

                            wsid = gyujto.get(1)[k][0];
                            break;

                        }

                    }

                    //cellid
                    for (int k = 0; k < gyujto.get(0).length; k++) {

                        if (cell.equals(gyujto.get(0)[k][1])) {

                            cellid = gyujto.get(0)[k][0];
                            break;

                        }

                    }

                    //megkeressuk a datumot
                    //12 vagy 8 órás a terv?
                    String beosztas = "";
                    if (t2.getColumnName(t2.getColumnCount() - 1).substring(11, 12).equals("2")) {

                        beosztas = "8";

                    } else {

                        beosztas = "12";

                    }

                    String datum = "";
                    String ora = "";

                    if (i > 3) {

                        if (t2.getColumnName(i).substring(11, 13).equals("06")) {

                            ora = " 06:00:00";
                        }

                        if (t2.getColumnName(i).substring(11, 13).equals("18") && beosztas.equals("12")) {

                            ora = " 18:00:00";
                        }

                        if (t2.getColumnName(i).substring(11, 13).equals("14") && beosztas.equals("8")) {

                            ora = " 14:00:00";
                        }

                        if (t2.getColumnName(i).substring(11, 13).equals("22")) {

                            ora = " 22:00:00";
                        }

//bejarjuk az osszes sort es osszeállitjuk egy querysorba az adatokat ha van pn és van ws és van valami a darabszámos cellában 
                        if (t2.getValueAt(r, 0) != null && t2.getValueAt(r, 2) != null && !t2.getValueAt(r, 0).toString().equals("") && !t2.getValueAt(r, 2).toString().equals("") && t2.getValueAt(r, i) != null && !t2.getValueAt(r, i).toString().equals("") && !t2.getColumnName(i).equals("Sum: PN,JOB,WS")) {

//meghatarozzuk a darabokat
                            String terv = "0";
                            String teny = "0";

// ha terv sorban vagyunk
                            if (t2.getValueAt(r, 3).toString().equals("Terv")) {
                                try {
                                    terv = t2.getValueAt(r, i).toString();
                                } catch (Exception e) {

                                }

                                try {
                                    teny = t2.getValueAt(r + 1, i).toString();
                                } catch (Exception e) {

                                }

                            }

//ha tény sorban vagyunk
                            if (t2.getValueAt(r, 3).toString().equals("Tény")) {
                                try {
                                    terv = t2.getValueAt(r - 1, i).toString();
                                } catch (Exception e) {

                                }

                                try {
                                    teny = t2.getValueAt(r, i).toString();
                                } catch (Exception e) {

                                }

                            }

//meghatarozzuk a datumot
                            datum = t2.getColumnName(i).substring(0, 10) + ora;

//kiszedjuk a mernoki infot az adattablabol ha terv sorban vagyunk
                            int mernoki = 0;
                            Double mernokiido = 0.0;

                            if (t2.getValueAt(r, 3).toString().equals("Terv")) {

                                try {
                                    mernoki = Besheets.get(neve).tablaadat[r - infsor][i].eng;
                                } catch (Exception e) {
                                }

                            }

// ha teny sor                 
                            if (t2.getValueAt(r, 3).toString().equals("Tény")) {

                                try {
                                    mernoki = Besheets.get(neve).tablaadat[r - 1 - infsor][i].eng;
                                } catch (Exception e) {
                                }

                            }

                            if (t2.getValueAt(r, 3).toString().equals("Terv")) {

                                try {
                                    mernokiido = Besheets.get(neve).tablaadat[r - infsor][i].engtime;
                                } catch (Exception e) {
                                }

                            }

                            if (t2.getValueAt(r, 3).toString().equals("Tény")) {

                                try {
                                    mernokiido = Besheets.get(neve).tablaadat[r - 1 - infsor][i].engtime;
                                } catch (Exception e) {
                                }

                            }

//kiszedjuk a szint is a cellaadatbol
                            int szin = 0;
                            try {
                                szin = Besheets.get(neve).tablaadat[r - infsor][i].szin;
                            } catch (Exception e) {

                            }

                            if (t2.getValueAt(r, 3).toString().equals("Tény")) {

                                szin = Besheets.get(neve).tablaadat[r - 1 - infsor][i].szin;

                            }

                            feltoltadat += "('" + cellid + "','" + wsid + "','" + pnid + "','" + job + "','" + datum + "','" + terv + "','" + r + "'," + 3 + ",'" + ablak.user + "','" + datum + cellid + wsid + pnid + "3" + job + "','" + teny + "','" + mernoki + "','" + mernokiido + "','" + szin + "'),";

                        }

                    }

                }

            }
        }

        if (feltoltadat.length() > 0) {
            feltoltadat = feltoltadat.substring(0, feltoltadat.length() - 1);

            // ha planner akkor updatelunk , ezzel torlunk ki a tervbol teteleket , a tervezett darabszamot nullara allitjuk , hogy ha visszaupdatelodik mert a termeles megis csinalja ne legyen bebne a terv darabszam
            if (ablak.planner == true) {

                //update 2-->1-->0
                String updatequery = "update tc_terv set active = CASE when tc_terv.active = 2 then 1 when tc_terv.active = 1 then 0 end , tc_terv.qty = 0 where tc_terv.active in (2,1) and tc_terv.date between '" + tol + "' and '" + ig + "' and tc_terv.idtc_becells = '" + cellid + "'";
                pc.feltolt(updatequery, false);

                //feltoltjuk az adatokat 
                String feltoltquery = "insert ignore tc_terv (tc_terv.idtc_becells , tc_terv.idtc_bestations , tc_terv.idtc_bepns , tc_terv.job , tc_terv.date , tc_terv.qty , tc_terv.wtf , tc_terv.tt , tc_terv.user , tc_terv.pktomig , qty_teny , mernoki , mernokiido , cellaszin) values" + feltoltadat + "on duplicate key update qty = values  (qty) , wtf = values (wtf) , active = (2) , qty_teny = values (qty_teny) , timestamp = now() , user = '" + ablak.user + "' , mernoki = values (mernoki), mernokiido = values (mernokiido) , cellaszin = values (cellaszin)";
                pc.feltolt(feltoltquery, true);

            }

            //ámde ha nem planner akkor nem updatelunk , hogy ne torlodjenek a tervek
            if (ablak.planner == false) {
                //feltoltjuk az adatokat , es egyezoseg eseten csak a teny qty update no meg a wtf
                String feltoltquery = "insert ignore tc_terv (tc_terv.idtc_becells , tc_terv.idtc_bestations , tc_terv.idtc_bepns , tc_terv.job , tc_terv.date , tc_terv.qty , tc_terv.wtf , tc_terv.tt , tc_terv.user , tc_terv.pktomig , qty_teny , mernoki , mernokiido , cellaszin) values" + feltoltadat + "on duplicate key update  qty_teny = values (qty_teny) , wtf = values (wtf) , active = (2) , user = '" + ablak.user + "'";
                pc.feltolt(feltoltquery, true);
            }

        }

//kitöröljük azokat a tételeket ahol nullásak a qty és a teny_qty és nem aktívok 
        query = "DELETE FROM tc_terv WHERE qty = 0 and qty_teny = 0 and active <> 2";
        pc.feltolt(query, false);

    }
}
