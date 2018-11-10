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
//kikapcsoljuk a gombot , hogy ne tudjunk még egy szálat indítani
        ablak.jButton21.setEnabled(false);

//hány másodpercenként fusson
        int ido = 0;
        try {
            ido = Integer.parseInt(ablak.jTextField16.getText());
        } catch (NumberFormatException e) {

            infobox info = new infobox();
            info.infoBox("Nem adtál meg futásidőt!", "Hiba!");

        }

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
            Query = "select tc_bepns.idtc_bepns , tc_becells.idtc_cells , tc_bestations.idtc_bestations , beciklusidok.DBPO , concat(idtc_bepns , idtc_cells , idtc_bestations) as pk\n"
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

////lekerdezzük a regi setupbol az adatokat
//            Query = "select tc_bepns.idtc_bepns , tc_becells.idtc_cells , tc_bestations.idtc_bestations , beciklusidok.DBPO \n"
//                    + "from beciklusidok\n"
//                    + "left join tc_bepns on tc_bepns.partnumber = beciklusidok.PN collate latin2_hungarian_ci\n"
//                    + "left join tc_becells on tc_becells.cellname = beciklusidok.CELL collate latin2_hungarian_ci\n"
//                    + "left join tc_bestations on tc_bestations.workstation = beciklusidok.WS collate latin2_hungarian_ci\n"
//                    + "where beciklusidok.active = 1 and idtc_cells is not null and idtc_bepns is not null and idtc_bestations is not null";
//
//            try {
//                pc.planconnect(Query);
//
////leellenőrizzük , hogy minden adat megfelelő e a beillesztéshez , hogy a truncate után ne maradjon üres a tábla
//                boolean torolhetunk = true;
//                String prodmatrixadat = "";
//
//                while (pc.rs.next()) {
//
//                    for (int i = 0; i < 4; i++) {
//
//                        try {
//                            //pc.rs.getDouble(i);
//                            prodmatrixadat += "('" + pc.rs.getString(1) + "','" + pc.rs.getString(2) + "','" + pc.rs.getString(3) + "','" + pc.rs.getDouble(4) + "'),";
//
//                        } catch (Exception e) {
//
//                            torolhetunk = false;
//                            break;
//
//                        }
//
//                    }
//
//                }
//
//                prodmatrixadat = prodmatrixadat.substring(0, prodmatrixadat.length() - 1);
////ha rendben van a lekérdezés mehet a truncate és tovább a kód
//                if (torolhetunk == true) {
//                    cellaklog += "Updateljük a prodmatrixot: " + LocalDateTime.now() + " \n";
//                    ablak.jTextArea1.setText(cellaklog);
//                    pc.feltolt("truncate tc_prodmatrix", false);
//
////összerakjuk az update queryt
//                    Query = "insert into tc_prodmatrix (id_tc_bepns , id_tc_becells , id_tc_bestations ,ciklusido ) values " + prodmatrixadat;
//                    pc.feltolt(Query, false);
//
//                }
//
//            } catch (SQLException ex) {
//                Logger.getLogger(Tc_sincronizerszal.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (ClassNotFoundException ex) {
//                Logger.getLogger(Tc_sincronizerszal.class.getName()).log(Level.SEVERE, null, ex);
//            }
//terv szinkronizálása
//lekerdezzuk a migralos queryt
            Query = "select startdate , cellaid , workstationid , partnumberid , qty , prio , active , tt , user , job , timestamp , pk , honnan from(\n"
                    + "select Beterv.startdate as startdate , tc_becells.idtc_cells as cellaid , tc_bestations.idtc_bestations  as workstationid , tc_bepns.idtc_bepns as partnumberid , Beterv.qty as qty  , Beterv.prio as prio , Beterv.active  as active , \n"
                    + "case Beterv.terv when 'Terv' then 0 when 'TÉNY' then 1 else 1 end as tt  , Beterv.User as user  , Beterv.job collate latin2_hungarian_ci as job   ,Beterv.id  , Beterv.date as timestamp ,\n"
                    + "concat(Beterv.startdate  , tc_becells.idtc_cells , tc_bestations.idtc_bestations , tc_bepns.\n"
                    + "idtc_bepns , case Beterv.terv when 'Terv' then 0 when 'TÉNY' then 1 else 1 end , Beterv.job collate latin2_hungarian_ci , Beterv.date , Beterv.qty , Beterv.prio , Beterv.active ) as pk , 'regi' as honnan\n"
                    + "from tc_becells\n"
                    + "left join Beterv on tc_becells.cellname = Beterv.sht \n"
                    + "left join tc_bestations on tc_bestations.workstation = Beterv.ws collate latin2_hungarian_ci\n"
                    + "left join tc_bepns on tc_bepns.partnumber = Beterv.pn collate latin2_hungarian_ci\n"
                    + "where Beterv.startdate > 0 and tc_bestations.idtc_bestations is not null and tc_becells.idtc_cells is not null and tc_bepns.idtc_bepns is not null and Beterv.terv is not null and Beterv.date > (now() - INTERVAL 15 DAY)\n"
                    + "union all \n"
                    + "select tc_terv.date as startdate , tc_terv.idtc_becells as cellaid , tc_terv.idtc_bestations as workstationid , tc_terv.idtc_bepns as partnumberid , tc_terv.qty , tc_terv.wtf as prio ,\n"
                    + "tc_terv.active , tc_terv.tt  ,  tc_terv.user , tc_terv.job ,  tc_terv.idtc_terv as id ,tc_terv.timestamp ,\n"
                    + "concat(tc_terv.date , tc_terv.idtc_becells , tc_terv.idtc_bestations , tc_terv.idtc_bepns , tc_terv.tt , tc_terv.job , tc_terv.timestamp , tc_terv.qty , tc_terv.wtf , tc_terv.active ) as pk , 'uj' as honnan\n"
                    + "from tc_terv \n"
                    + "where tc_terv.date > 0 and tc_terv.timestamp > (now() - INTERVAL 15 DAY) \n"
                    + ") t\n"
                    + "group by pk , active\n"
                    + "having count(*) = 1 \n"
                    + "order by pk ";

            cellaklog += "Updateljük a terveket: " + LocalDateTime.now() + " \n";
            ablak.jTextArea1.setText(cellaklog);

            int sz = 0;
            int del = 0;
            try {

                pc.planconnect(Query);

//összerakjuk az insert query-t
                StringBuffer insertadat = new StringBuffer();
                StringBuffer deletedata = new StringBuffer();

                while (pc.rs.next()) {

                    if (pc.rs.getString(13).equals("regi")) {
                        insertadat.append("('").append(pc.rs.getString(1)).append("','").append(pc.rs.getString(2)).append("','").append(pc.rs.getString(3)).append("','").append(pc.rs.getString(4)).append("','").append(pc.rs.getString(5)).append("','").append(pc.rs.getString(6)).append("','").append(pc.rs.getString(7)).append("','").append(pc.rs.getString(8)).append("','").append(pc.rs.getString(9)).append("','").append(pc.rs.getString(10)).append("','").append(pc.rs.getString(11)).append("','").append(pc.rs.getString(12)).append("'),");
                        sz++;
                    } else {

                        deletedata.append("('").append(pc.rs.getString(12)).append("'),");
                        del++;

                    }
                }

                cellaklog += "Beillesztünk " + sz + " adatot" + LocalDateTime.now() + " \n";
                cellaklog += "Kitörlünk " + del + " adatot" + LocalDateTime.now() + " \n";
                ablak.jTextArea1.setText(cellaklog);
//átalakítjuk a stringbuffereket stringé
                String insertadatstring = insertadat.toString();
                String deletedatastring = deletedata.toString();

                if (insertadatstring.length() > 0) {
                    insertadatstring = insertadatstring.substring(0, insertadatstring.length() - 1);

                    Query = "insert into tc_terv (date , idtc_becells , idtc_bestations , idtc_bepns , qty , wtf , active , tt , user ,job , timestamp  , pktomig) values " + insertadatstring + " on duplicate key update active = values (active)";
                    pc.feltolt(Query, false);
                }

                if (deletedatastring.length() > 0) {

                    deletedatastring = deletedatastring.substring(0, deletedatastring.length() - 1);
                    Query = "delete FROM tc_terv where (pktomig) in (" + deletedatastring + ")";
                    pc.feltolt(Query, false);

                }

            } catch (SQLException ex) {
                Logger.getLogger(Tc_sincronizerszal.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Tc_sincronizerszal.class.getName()).log(Level.SEVERE, null, ex);
            }

//<-------------------------------------------------------------------------ujragondolni!!!------------------------------------------------------------->
////itt folytatjuk a terv szinkronizálásával
////az update query adatainak a tárolója
//            List<String[]> updatequery = new ArrayList<String[]>();
//
//// multi tömb a régi tervnek
//            cellaklog += "A régi terv lekérdezése " + LocalDateTime.now() + "\n";
//            ablak.jTextArea1.setText(cellaklog);
//
//            List<String[]> regiterv = new ArrayList<String[]>();
//
//            Query = "select Beterv.startdate, tc_becells.idtc_cells as cellaid , tc_bestations.idtc_bestations  as workstationid , tc_bepns.idtc_bepns as partnumberid , Beterv.qty , Beterv.prio , Beterv.active as active , \n"
//                    + "case Beterv.terv when 'Terv' then 0 when 'TÉNY' then 1 else 1 end as tt , Beterv.User as user , Beterv.job , Beterv.id , Beterv.date\n"
//                    + "from tc_becells\n"
//                    + "left join Beterv on Beterv.sht = tc_becells.cellname\n"
//                    + "left join tc_bestations on Beterv.ws collate latin2_hungarian_ci = tc_bestations.workstation\n"
//                    + "left join tc_bepns on tc_bepns.partnumber = Beterv.pn collate latin2_hungarian_ci\n"
//                    + "where Beterv.active >0  and Beterv.startdate > 0 and tc_bestations.idtc_bestations is not null and tc_becells.idtc_cells is not null and tc_bepns.idtc_bepns is not null and Beterv.terv is not null order by active";
//
//            try {
//                pc.planconnect(Query);
////belepakoljuk a listbe az integer tömböket 
//
//                while (pc.rs.next()) {
//
//                    String[] a = new String[12];
//
//                    for (int i = 0; i < 12; i++) {
//
//                        a[i] = pc.rs.getString(i + 1);
//
//                    }
//
//                    regiterv.add(a);
//
//                }
//
//            } catch (SQLException ex) {
//                Logger.getLogger(Tc_sincronizerszal.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (ClassNotFoundException ex) {
//                Logger.getLogger(Tc_sincronizerszal.class.getName()).log(Level.SEVERE, null, ex);
//            }
//
//            // multi tömb az uj tervnek
//            cellaklog += "Az új terv lekérdezése " + LocalDateTime.now() + "\n";
//            ablak.jTextArea1.setText(cellaklog);
//
//            List<String[]> ujterv = new ArrayList<String[]>();
//
//            Query = "select tc_terv.date as startdate , tc_terv.idtc_becells as cellaid , tc_terv.idtc_bestations as workstationid , tc_terv.idtc_bepns as partnumberid , tc_terv.qty , tc_terv.wtf as prio , tc_terv.active , tc_terv.tt , tc_terv.user , tc_terv.job , tc_terv.idtc_terv , tc_terv.timestamp\n"
//                    + "from tc_terv\n"
//                    + "where tc_terv.active > 0 and tc_terv.date > 0 order by active";
//
//            try {
//                pc.planconnect(Query);
////belepakoljuk a listbe az integer tömböket 
//
//                while (pc.rs.next()) {
//
//                    String[] a = new String[12];
//
//                    for (int i = 0; i < 12; i++) {
//
//                        a[i] = pc.rs.getString(i + 1);
//
//                    }
//
//                    ujterv.add(a);
//
//                }
//
//            } catch (SQLException ex) {
//                Logger.getLogger(Tc_sincronizerszal.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (ClassNotFoundException ex) {
//                Logger.getLogger(Tc_sincronizerszal.class.getName()).log(Level.SEVERE, null, ex);
//            }
//
//
//
////összeforgatjuk a tömböket
//            cellaklog += "Összeforgatjuk a terveket " + LocalDateTime.now() + "\n";
//            ablak.jTextArea1.setText(cellaklog);
//
//            long torles = 0;
//            long update = 0;
//            ablak.jTextField21.setText(String.valueOf(update));
//            
//            StringBuffer updateadat = new StringBuffer();
//
//            for (int r = 0; r < regiterv.size(); r++) {
//
//                for (int u = 0; u < ujterv.size(); u++) {
//
//                    //ha egyezik a startdate (0) a cellaid (1), a workstation id (2) , parnumberid (3) , qty (4) , tt (7) , job (9) , timestamp (11)
//                    if (regiterv.get(r)[0].equals(ujterv.get(u)[0]) && regiterv.get(r)[1].equals(ujterv.get(u)[1]) && regiterv.get(r)[2].equals(ujterv.get(u)[2]) && regiterv.get(r)[3].equals(ujterv.get(u)[3]) && regiterv.get(r)[4].equals(ujterv.get(u)[4]) && regiterv.get(r)[7].equals(ujterv.get(u)[7]) && regiterv.get(r)[9].equals(ujterv.get(u)[9]) && regiterv.get(r)[11].equals(ujterv.get(u)[11])) {
//                        //ha egyezik az active akkor töröljük
//                        if (regiterv.get(r)[6].equals(ujterv.get(u)[6])) {
//                            regiterv.remove(r);
//                            r--;
//                            torles++;
//                            ablak.jTextField20.setText(String.valueOf(torles));
//                            break;
//
//                        } //ha nem egyezik akkor update , ehhez berakjuk egy tárolóba, majd töröljük
//                        else {
//
//                            updateadat.append("('").append(ujterv.get(u)[0]).append("','").append(ujterv.get(u)[1]).append("','").append(ujterv.get(u)[2]).append("','").append(ujterv.get(u)[3]).append("','").append(ujterv.get(u)[4]).append("','").append(ujterv.get(u)[5]).append("','").append(regiterv.get(r)[6]).append("','").append(ujterv.get(u)[7]).append("','").append(ujterv.get(u)[8]).append("','").append(ujterv.get(u)).append("','").append(ujterv.get(u)[10]).append("','").append(ujterv.get(u)[11]).append("'),");
//                            regiterv.remove(r);
//                            r--;
//                            update++;
//                            ablak.jTextField21.setText(String.valueOf(update));
//                            break;
//
//                        }
//
//                    }
//
//                }
//
//            }
//
////vege az összeforgatásnak , updateljük az új táblát a megfelelő adatokkal
////összeállítjuk az update queryt
//            cellaklog += "Updatelni fogunk " + update + " adatot! " + LocalDateTime.now() + "\n";
//            ablak.jTextArea1.setText(cellaklog);
//
//            if (update > 0) {
//                String updateadatstring = updateadat.toString();
//                updateadatstring = updateadat.substring(0, updateadat.length() - 1);
//
//                Query = "insert into tc_terv (date , idtc_becells , idtc_bestations , idtc_bepns , qty , wtf , active , tt , user , job , idtc_terv , timestamp) values " + updateadatstring + " on duplicate key update  active = values (active)";
//
//                pc.feltolt(Query, false);
//            }
//
////beillesztjuk azt ami megmaradt
////összeállítjuk a beilleszt tömböt
//            cellaklog += "Be fogunk illeszteni " + regiterv.size() + " adatot! " + LocalDateTime.now() + "\n";
//            ablak.jTextArea1.setText(cellaklog);
//
//            if (regiterv.size() > 0) {
//
//                StringBuffer insertadat = new StringBuffer();
////összeállítjuk a beilleszt queryt
//                for (int i = 0; i < regiterv.size(); i++) {
//
//                    insertadat.append("('").append(regiterv.get(i)[0]).append("','").append(regiterv.get(i)[1]).append("','").append(regiterv.get(i)[2]).append("','").append(regiterv.get(i)[3]).append("','").append(regiterv.get(i)[4]).append("','").append(regiterv.get(i)[5]).append("','").append(regiterv.get(i)[6]).append("','").append(regiterv.get(i)[7]).append("','").append(regiterv.get(i)[8]).append("','").append(regiterv.get(i)[9]).append("','").append(regiterv.get(i)[11]).append("'),");
//
//                }
//
//                String stringinsertadat = insertadat.toString();
//                stringinsertadat = stringinsertadat.substring(0, stringinsertadat.length() - 1);
//                Query = "insert into tc_terv (date , idtc_becells , idtc_bestations , idtc_bepns , qty , wtf , active , tt , user , job , timestamp) values " + stringinsertadat;
//
//                pc.feltolt(Query, false);
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
