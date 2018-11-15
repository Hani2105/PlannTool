/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool;

import com.mysql.jdbc.UpdatableResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gabor_hanacsek
 */
public class Tc_sincronizerszal extends Thread {

    public static boolean fut = true;

    public void run() {

//hány másodpercenként fusson és az intervallum ne legyen túl nagy
        int ido = 0;

        if (Integer.parseInt(ablak.jTextField20.getText()) > 20) {

            infobox info = new infobox();
            info.infoBox("Túl nagy intervallum , max 20 nap!", "Hiba!");
            return;

        }

        try {
            ido = Integer.parseInt(ablak.jTextField16.getText());

        } catch (NumberFormatException e) {

            infobox info = new infobox();
            info.infoBox("Nem adtál meg futásidőt vagy intervallumot!", "Hiba!");

        }

//kikapcsoljuk a gombot , hogy ne tudjunk még egy szálat indítani
        ablak.jButton21.setEnabled(false);

//string a logba
        String cellaklog = "Start Run " + LocalDateTime.now() + " \n";

        while (fut == true && ido > 0) {

//cellák felvétele!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//lista a cellaknak
            List<String> cellak = new ArrayList<>();

//megnezzuk milyen cellak hianyoznak az uj adatbazisbol
            String Query = "select Beterv.sht as regicella , tc_becells.cellname as ujcella from Beterv\n"
                    + "left join tc_becells on tc_becells.cellname = Beterv.sht\n"
                    + "where tc_becells.cellname is null and Beterv.active = 2 \n"
                    + "group by regicella";

            planconnect pc = new planconnect();
//string a logba
            cellaklog += "Cellák hozzáadása: " + LocalDateTime.now() + " \n";
            try {
                pc.planconnect(Query);

                while (pc.rs.next()) {

                    cellak.add(pc.rs.getString("regicella"));
                    cellaklog += pc.rs.getString("regicella") + "    " + LocalDateTime.now() + " \n";

                }

                ablak.jTextArea1.setText(cellaklog);
            } catch (SQLException ex) {
                Logger.getLogger(ablak.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ablak.class.getName()).log(Level.SEVERE, null, ex);
            }

            // a kapott eredmenyt beillesztjuk az uj adatbazisba
            if (cellak.size() > 0) {

//az insert query eleje
                Query = "insert ignore tc_becells (cellname) values ";
//osszeallitjuk az adatokat
                String adatok = "";
                for (int i = 0; i < cellak.size(); i++) {

                    adatok += "('" + cellak.get(i) + "'),";

                }

                adatok = adatok.substring(0, adatok.length() - 1);

                Query += adatok;

                pc.feltolt(Query, false);

            }
//ws -ek felvétele!!!!!!!!!!!!!!!!!!!!!!!!!   
//lista a cellaknak
            List<String> wsek = new ArrayList<>();

//megnezzuk milyen cellak hianyoznak az uj adatbazisbol
            Query = "select Beterv.ws as regiws , tc_bestations.workstation as ujws from Beterv\n"
                    + "left join tc_bestations on tc_bestations.workstation = Beterv.ws collate latin2_hungarian_ci\n"
                    + "where tc_bestations.workstation is null and Beterv.active = 2 \n"
                    + "group by regiws";

            try {
                pc.planconnect(Query);
//string a logba
                cellaklog += "WS-ek hozzáadása: " + LocalDateTime.now() + " \n";

                while (pc.rs.next()) {

                    wsek.add(pc.rs.getString("regiws"));
                    cellaklog += pc.rs.getString("regiws") + "    " + LocalDateTime.now() + "\n";

                }

                ablak.jTextArea1.setText(cellaklog);
            } catch (SQLException ex) {
                Logger.getLogger(ablak.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ablak.class.getName()).log(Level.SEVERE, null, ex);
            }

// a kapott eredmenyt beillesztjuk az uj adatbazisba
            if (wsek.size() > 0) {

//az insert query eleje
                Query = "insert ignore tc_bestations (workstation) values ";
                //osszeallitjuk az adatokat
                String adatok = "";
                for (int i = 0; i < wsek.size(); i++) {

                    adatok += "('" + wsek.get(i) + "'),";

                }

                adatok = adatok.substring(0, adatok.length() - 1);

                Query += adatok;

                pc.feltolt(Query, false);

            }

//pn -ek felvétele!!!!!!!!!!!!!!!!!!!!!!!!!   
            //lista a cellaknak
            List<String> pnek = new ArrayList<>();

//megnezzuk milyen cellak hianyoznak az uj adatbazisbol
            Query = "select Beterv.pn as regipn , tc_bepns.partnumber as ujpn from Beterv\n"
                    + "left join tc_bepns on tc_bepns.partnumber = Beterv.pn collate latin2_hungarian_ci\n"
                    + "where tc_bepns.partnumber is null and Beterv.active = 2 \n"
                    + "group by regipn";

            try {
                pc.planconnect(Query);
//string a logba
                cellaklog += "PN-ek hozzáadása: " + LocalDateTime.now() + " \n";

                while (pc.rs.next()) {

                    pnek.add(pc.rs.getString("regipn"));
                    cellaklog += pc.rs.getString("regipn") + "    " + LocalDateTime.now() + "\n";

                }

                ablak.jTextArea1.setText(cellaklog);
            } catch (SQLException ex) {
                Logger.getLogger(ablak.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ablak.class.getName()).log(Level.SEVERE, null, ex);
            }

// a kapott eredmenyt beillesztjuk az uj adatbazisba
            if (pnek.size() > 0) {

//az insert query eleje
                Query = "insert ignore tc_bepns (partnumber) values ";
//osszeallitjuk az adatokat
                String adatok = "";
                for (int i = 0; i < pnek.size(); i++) {

                    adatok += "('" + pnek.get(i) + "'),";

                }

                adatok = adatok.substring(0, adatok.length() - 1);

                Query += adatok;

                pc.feltolt(Query, false);

            }

//frissítjük a prodmatrixot
// lekerdezzuk a queryt , ebben mar benne van az összetett kulcs ami a darab/orat nem tartalmazza
            Query = "select distinct tc_bepns.idtc_bepns , tc_becells.idtc_cells , tc_bestations.idtc_bestations , beciklusidok.DBPO , concat(idtc_bepns , idtc_cells , idtc_bestations) as pk\n"
                    + "from beciklusidok\n"
                    + "left join tc_bepns on tc_bepns.partnumber = beciklusidok.PN collate latin2_hungarian_ci\n"
                    + "left join tc_becells on tc_becells.cellname = beciklusidok.CELL collate latin2_hungarian_ci\n"
                    + "left join tc_bestations on tc_bestations.workstation = beciklusidok.WS collate latin2_hungarian_ci\n"
                    + "where beciklusidok.active = 1 and idtc_cells is not null and idtc_bepns is not null and idtc_bestations is not null";

            int szamlalo = 0;
            try {
                pc.planconnect(Query);

                //összerakjuk az insert queryt a pk alapján
                String adatok = "";

                while (pc.rs.next()) {

                    adatok += "('" + pc.rs.getString(1) + "','" + pc.rs.getString(2) + "','" + pc.rs.getShort(3) + "','" + pc.rs.getString(4) + "','" + pc.rs.getString(5) + "'),";
                    szamlalo++;
                }

                if (adatok.length() > 0) {

                    adatok = adatok.substring(0, adatok.length() - 1);

                    Query = "insert into tc_prodmatrix (id_tc_bepns , id_tc_becells , id_tc_bestations , ciklusido , pk) values " + adatok + " on duplicate key update ciklusido = values (ciklusido)";
                    pc.feltolt(Query, false);

                }

            } catch (SQLException ex) {
                Logger.getLogger(Tc_sincronizerszal.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Tc_sincronizerszal.class.getName()).log(Level.SEVERE, null, ex);
            }

            cellaklog += "Updateljük a prodmatrixot " + szamlalo + " sorral" + LocalDateTime.now() + " \n";
            ablak.jTextArea1.setText(cellaklog);

//terv szinkronizálása
//lekerdezzuk a migralos queryt
            Query = "select Beterv.startdate , idtc_cells , idtc_bestations , idtc_bepns , Beterv.qty , Beterv.prio , Beterv.active ,case Beterv.terv when 'Terv' then 0 when 'TÉNY' then 1 else 1 end as tt  , Beterv.date , Beterv.User , Beterv.job , \n"
                    + "concat(Beterv.startdate , idtc_cells , idtc_bestations , idtc_bepns , Beterv.qty , Beterv.prio  ,case Beterv.terv when 'Terv' then 0 when 'TÉNY' then 1 else 1 end  ,Beterv.date  ,Beterv.job) as pk \n"
                    + "from Beterv\n"
                    + "left join tc_becells on tc_becells.cellname = Beterv.sht collate latin2_hungarian_ci\n"
                    + "left join tc_bestations on tc_bestations.workstation = Beterv.ws collate latin2_hungarian_ci\n"
                    + "left join tc_bepns on tc_bepns.partnumber = Beterv.pn collate latin2_hungarian_ci\n"
                    + "where Beterv.startdate > now() - interval " + Integer.parseInt(ablak.jTextField20.getText()) + " day and Beterv.startdate > 0 and Beterv.date > 0 and Beterv.sht = '"+ablak.jTextField21.getText()+"'";

//megprobáljuk beilleszteni az eredményt , ha egyezik a kulcs , updateljük az activot
//ehhez összeállítjuk az insert query adatait
            szamlalo = 0;
            try {
                pc.planconnect(Query);
                StringBuffer insertadat = new StringBuffer();

                while (pc.rs.next()) {

                    insertadat.append("('").append(pc.rs.getString(1)).append("','").append(pc.rs.getString(2)).append("','").append(pc.rs.getString(3)).append("','").append(pc.rs.getString(4)).append("','").append(pc.rs.getString(5)).append("','").append(pc.rs.getString(6)).append("','").append(pc.rs.getString(7)).append("','").append(pc.rs.getString(8)).append("','").append(pc.rs.getString(9)).append("','").append(pc.rs.getString(10)).append("','").append(pc.rs.getString(11)).append("','").append(pc.rs.getString(12)).append("'),");
                    szamlalo++;
                }

//átalakítjuk a stringbuffert stringé ha van benne adat
                if (insertadat.length() > 0) {

                    String insertadatstring = insertadat.toString();
                    insertadatstring = insertadatstring.substring(0, insertadatstring.length() - 1);
                    Query = Query = "insert into tc_terv (date , idtc_becells , idtc_bestations , idtc_bepns , qty , wtf , active , tt , timestamp , user ,job , pktomig) values " + insertadatstring + " on duplicate key update active = values (active)";
                    pc.feltolt(Query, false);

                }

            } catch (SQLException ex) {
                Logger.getLogger(Tc_sincronizerszal.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Tc_sincronizerszal.class.getName()).log(Level.SEVERE, null, ex);
            }

            cellaklog += "Updateljük a tervet " + szamlalo + " sorral" + LocalDateTime.now() + " \n";
            ablak.jTextArea1.setText(cellaklog);

//            Query = "select startdate , cellaid , workstationid , partnumberid , qty , prio , active , tt , user , job , timestamp , pk , honnan from(\n"
//                    + "select Beterv.startdate as startdate , tc_becells.idtc_cells as cellaid , tc_bestations.idtc_bestations  as workstationid , tc_bepns.idtc_bepns as partnumberid , Beterv.qty as qty  , Beterv.prio as prio , Beterv.active  as active , \n"
//                    + "case Beterv.terv when 'Terv' then 0 when 'TÉNY' then 1 else 1 end as tt  , Beterv.User as user  , Beterv.job collate latin2_hungarian_ci as job   ,Beterv.id  , Beterv.date as timestamp ,\n"
//                    + "concat(Beterv.startdate  , tc_becells.idtc_cells , tc_bestations.idtc_bestations , tc_bepns.\n"
//                    + "idtc_bepns , case Beterv.terv when 'Terv' then 0 when 'TÉNY' then 1 else 1 end , Beterv.job collate latin2_hungarian_ci , Beterv.date , Beterv.qty , Beterv.prio , Beterv.active ) as pk , 'regi' as honnan\n"
//                    + "from tc_becells\n"
//                    + "left join Beterv on tc_becells.cellname = Beterv.sht \n"
//                    + "left join tc_bestations on tc_bestations.workstation = Beterv.ws collate latin2_hungarian_ci\n"
//                    + "left join tc_bepns on tc_bepns.partnumber = Beterv.pn collate latin2_hungarian_ci\n"
//                    + "where Beterv.startdate > 0 and tc_bestations.idtc_bestations is not null and tc_becells.idtc_cells is not null and tc_bepns.idtc_bepns is not null and Beterv.terv is not null and Beterv.date > (now() - INTERVAL 15 DAY)\n"
//                    + "union all \n"
//                    + "select tc_terv.date as startdate , tc_terv.idtc_becells as cellaid , tc_terv.idtc_bestations as workstationid , tc_terv.idtc_bepns as partnumberid , tc_terv.qty , tc_terv.wtf as prio ,\n"
//                    + "tc_terv.active , tc_terv.tt  ,  tc_terv.user , tc_terv.job ,  tc_terv.idtc_terv as id ,tc_terv.timestamp ,\n"
//                    + "concat(tc_terv.date , tc_terv.idtc_becells , tc_terv.idtc_bestations , tc_terv.idtc_bepns , tc_terv.tt , tc_terv.job , tc_terv.timestamp , tc_terv.qty , tc_terv.wtf , tc_terv.active ) as pk , 'uj' as honnan\n"
//                    + "from tc_terv \n"
//                    + "where tc_terv.date > 0 and tc_terv.timestamp > (now() - INTERVAL 15 DAY) \n"
//                    + ") t\n"
//                    + "group by pk , active\n"
//                    + "having count(*) = 1 \n"
//                    + "order by pk ";
//
//            cellaklog += "Updateljük a terveket: " + LocalDateTime.now() + " \n";
//            ablak.jTextArea1.setText(cellaklog);
//
//            int sz = 0;
//            int del = 0;
//            try {
//
//                pc.planconnect(Query);
//
////összerakjuk az insert query-t
//                StringBuffer insertadat = new StringBuffer();
//                StringBuffer deletedata = new StringBuffer();
//
//                while (pc.rs.next()) {
//
//                    if (pc.rs.getString(13).equals("regi")) {
//                        insertadat.append("('").append(pc.rs.getString(1)).append("','").append(pc.rs.getString(2)).append("','").append(pc.rs.getString(3)).append("','").append(pc.rs.getString(4)).append("','").append(pc.rs.getString(5)).append("','").append(pc.rs.getString(6)).append("','").append(pc.rs.getString(7)).append("','").append(pc.rs.getString(8)).append("','").append(pc.rs.getString(9)).append("','").append(pc.rs.getString(10)).append("','").append(pc.rs.getString(11)).append("','").append(pc.rs.getString(12)).append("'),");
//                        sz++;
//                    } else {
//
//                        deletedata.append("('").append(pc.rs.getString(12)).append("'),");
//                        del++;
//
//                    }
//                }
//
//                cellaklog += "Beillesztünk " + sz + " adatot" + LocalDateTime.now() + " \n";
//                cellaklog += "Kitörlünk " + del + " adatot" + LocalDateTime.now() + " \n";
//                ablak.jTextArea1.setText(cellaklog);
////átalakítjuk a stringbuffereket stringé
//                String insertadatstring = insertadat.toString();
//                String deletedatastring = deletedata.toString();
//
//                if (insertadatstring.length() > 0) {
//                    insertadatstring = insertadatstring.substring(0, insertadatstring.length() - 1);
//
//                    Query = "insert into tc_terv (date , idtc_becells , idtc_bestations , idtc_bepns , qty , wtf , active , tt , user ,job , timestamp  , pktomig) values " + insertadatstring + " on duplicate key update active = values (active)";
//                    pc.feltolt(Query, false);
//                }
//
//                if (deletedatastring.length() > 0) {
//
//                    deletedatastring = deletedatastring.substring(0, deletedatastring.length() - 1);
//                    Query = "delete FROM tc_terv where (pktomig) in (" + deletedatastring + ")";
//                    pc.feltolt(Query, false);
//
//                }
//
//            } catch (SQLException ex) {
//                Logger.getLogger(Tc_sincronizerszal.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (ClassNotFoundException ex) {
//                Logger.getLogger(Tc_sincronizerszal.class.getName()).log(Level.SEVERE, null, ex);
//            }
//<--------------------------------------------------------------------------ezt a részt----------------------------------------------------------------->
            try {
                Thread.sleep(ido * 1000);

            } catch (InterruptedException ex) {
                Logger.getLogger(Tc_sincronizerszal.class.getName()).log(Level.SEVERE, null, ex);
            }

//eddig megy a while
        }

//visszakapcsoljuk a gombot hogy indítható legyen
        ablak.jButton21.setEnabled(true);

        cellaklog += "Thread stopped now! \n";
        ablak.jTextArea1.setText(cellaklog);

    }

}
