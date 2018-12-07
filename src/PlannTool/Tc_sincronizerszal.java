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

//kikapcsoljuk a gombot , hogy ne tudjunk még egy szálat indítani
        ablak.jButton21.setEnabled(false);

//string a logba
        String cellaklog = "Start Run " + LocalDateTime.now() + " \n";

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

                Query = "insert ignore tc_prodmatrix (id_tc_bepns , id_tc_becells , id_tc_bestations , ciklusido , pk) values " + adatok ; /*on duplicate key update ciklusido = values (ciklusido)";*/
                pc.feltolt(Query, false);

            }

        } catch (SQLException ex) {
            Logger.getLogger(Tc_sincronizerszal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Tc_sincronizerszal.class.getName()).log(Level.SEVERE, null, ex);
        }

        cellaklog += "Updateljük a prodmatrixot " + szamlalo + " sorral" + LocalDateTime.now() + " \n";
        ablak.jTextArea1.setText(cellaklog);

//terv szinkronizálása-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//kitoroljuk az eddigi terveket az adott cellabol
        Query = "delete tc_terv from tc_terv \n"
                + "left join tc_becells on tc_becells.idtc_cells = tc_terv.idtc_becells\n"
                + "where tc_becells.cellname = '" + ablak.jTextField21.getText() + "'";
        pc.feltolt(Query, false);

//kitoroljuk a temp tablat ha van
        Query = "drop temporary table if exists seged;";
        pc.feltolt(Query, false);

//letrehozzuk a seged tablat
//eloszor a tervet
        Query = "create temporary table seged select  sum(Beterv.qty) as tenyqty, concat(Beterv.startdate , idtc_cells , idtc_bestations , idtc_bepns , 3 , Beterv.job) as pkseged \n"
                + "from Beterv\n"
                + "left join tc_becells on tc_becells.cellname = Beterv.sht collate latin2_hungarian_ci\n"
                + "left join tc_bestations on tc_bestations.workstation = Beterv.ws collate latin2_hungarian_ci\n"
                + "left join tc_bepns on tc_bepns.partnumber = Beterv.pn collate latin2_hungarian_ci\n"
                + "where Beterv.startdate > now() - interval " + Integer.parseInt(ablak.jTextField20.getText()) + " day and Beterv.startdate > 0  and Beterv.sht = '" + ablak.jTextField21.getText() + "' and Beterv.terv = 'Teny' and active = 2 group by pkseged;";

        pc.createtemp(Query, false);
//lekerdezzuk a temp tablat

        Query = "select Beterv.startdate as startdatum , idtc_cells as cellid , idtc_bestations as stationid , idtc_bepns as pnid , Beterv.qty as tervqty , Beterv.prio as wtf , Beterv.active as active , 3 , Beterv.date as timestamp , Beterv.User as user , Beterv.job as job , \n"
                + "concat(Beterv.startdate , idtc_cells , idtc_bestations , idtc_bepns , 3 , Beterv.job) as pk , ifnull((select ifnull(seged.tenyqty , 0) from seged where seged.pkseged = pk) , 0) as tenyqty\n"
                + "from Beterv\n"
                + "left join tc_becells on tc_becells.cellname = Beterv.sht collate latin2_hungarian_ci\n"
                + "left join tc_bestations on tc_bestations.workstation = Beterv.ws collate latin2_hungarian_ci\n"
                + "left join tc_bepns on tc_bepns.partnumber = Beterv.pn collate latin2_hungarian_ci\n"
                + "where  Beterv.startdate > now() - interval " + Integer.parseInt(ablak.jTextField20.getText()) + " day and Beterv.startdate > 0  and Beterv.sht = '" + ablak.jTextField21.getText() + "' and Beterv.terv = 'Terv' and active = 2";
        szamlalo = 0;
        try {
            pc.templekerdez(Query);
            StringBuffer insertadat = new StringBuffer();

            while (pc.rs.next()) {

                insertadat.append("('").append(pc.rs.getString(1)).append("','").append(pc.rs.getString(2)).append("','").append(pc.rs.getString(3)).append("','").append(pc.rs.getString(4)).append("','").append(pc.rs.getString(5)).append("','").append(pc.rs.getString(6)).append("','").append(pc.rs.getString(7)).append("','").append(pc.rs.getString(8)).append("','").append(pc.rs.getString(9)).append("','").append(pc.rs.getString(10)).append("','").append(pc.rs.getString(11)).append("','").append(pc.rs.getString(12)).append("','").append(pc.rs.getString(13)).append("'),");
                szamlalo++;
            }

//átalakítjuk a stringbuffert stringé ha van benne adat
            if (insertadat.length() > 0) {

                String insertadatstring = insertadat.toString();
                insertadatstring = insertadatstring.substring(0, insertadatstring.length() - 1);
                Query = Query = "insert ignore tc_terv (date , idtc_becells , idtc_bestations , idtc_bepns , qty , wtf , active , tt , timestamp , user ,job , pktomig , qty_teny) values " + insertadatstring + "";
                pc.createtemp(Query, false);

            }

        } catch (SQLException ex) {
            Logger.getLogger(Tc_sincronizerszal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Tc_sincronizerszal.class.getName()).log(Level.SEVERE, null, ex);
        }

//lekerdezzuk fordítva is<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        //kitoroljuk a temp tablat ha van
        Query = "drop temporary table if exists seged;";
        pc.feltolt(Query, false);

//letrehozzuk a seged tablat ismet
        Query = "create temporary table seged select  sum(Beterv.qty) as tervqty, concat(Beterv.startdate , idtc_cells , idtc_bestations , idtc_bepns , 3 , Beterv.job) as pkseged \n"
                + "from Beterv\n"
                + "left join tc_becells on tc_becells.cellname = Beterv.sht collate latin2_hungarian_ci\n"
                + "left join tc_bestations on tc_bestations.workstation = Beterv.ws collate latin2_hungarian_ci\n"
                + "left join tc_bepns on tc_bepns.partnumber = Beterv.pn collate latin2_hungarian_ci\n"
                + "where Beterv.startdate > now() - interval " + Integer.parseInt(ablak.jTextField20.getText()) + " day and Beterv.startdate > 0  and Beterv.sht = '" + ablak.jTextField21.getText() + "' and Beterv.terv = 'Terv' and active = 2 group by pkseged;";

        pc.createtemp(Query, false);

//lekerdezzuk a tempet ismet
        Query = "select concat(Beterv.startdate , idtc_cells , idtc_bestations , idtc_bepns , 3 , Beterv.job) as pk , Beterv.startdate as startdatum , idtc_cells as cellid , idtc_bestations as stationid , idtc_bepns as pnid , ifnull((select ifnull(seged.tervqty , 0) from seged where pk = seged.pkseged ) , 0) as tervqty  , Beterv.prio as wtf , Beterv.active as active , 3 , Beterv.date as timestamp , Beterv.User as user , Beterv.job as job , \n"
                + "Beterv.qty as tenyqty\n"
                + "from Beterv\n"
                + "left join tc_becells on tc_becells.cellname = Beterv.sht collate latin2_hungarian_ci\n"
                + "left join tc_bestations on tc_bestations.workstation = Beterv.ws collate latin2_hungarian_ci\n"
                + "left join tc_bepns on tc_bepns.partnumber = Beterv.pn collate latin2_hungarian_ci\n"
                + "where  Beterv.startdate > now() - interval " + Integer.parseInt(ablak.jTextField20.getText()) + " day and Beterv.startdate > 0  and Beterv.sht = '" + ablak.jTextField21.getText() + "' and Beterv.terv = 'Tény' and active = 2";

        try {
            pc.templekerdez(Query);
            StringBuffer insertadat = new StringBuffer();

            while (pc.rs.next()) {

                insertadat.append("('").append(pc.rs.getString(1)).append("','").append(pc.rs.getString(2)).append("','").append(pc.rs.getString(3)).append("','").append(pc.rs.getString(4)).append("','").append(pc.rs.getString(5)).append("','").append(pc.rs.getString(6)).append("','").append(pc.rs.getString(7)).append("','").append(pc.rs.getString(8)).append("','").append(pc.rs.getString(9)).append("','").append(pc.rs.getString(10)).append("','").append(pc.rs.getString(11)).append("','").append(pc.rs.getString(12)).append("','").append(pc.rs.getString(13)).append("'),");
                szamlalo++;
            }

//átalakítjuk a stringbuffert stringé ha van benne adat ismet
            if (insertadat.length() > 0) {

                String insertadatstring = insertadat.toString();
                insertadatstring = insertadatstring.substring(0, insertadatstring.length() - 1);
                Query = Query = "insert ignore into tc_terv (pktomig , date , idtc_becells , idtc_bestations , idtc_bepns , qty , wtf , active , tt , timestamp , user ,job , qty_teny) values " + insertadatstring + "";
                pc.createtemp(Query, false);

            }

        } catch (SQLException ex) {
            Logger.getLogger(Tc_sincronizerszal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Tc_sincronizerszal.class.getName()).log(Level.SEVERE, null, ex);
        }

//kinyirjuk a kapcsolatot 
        pc.kinyir();

        cellaklog += "Updateljük a tervet " + szamlalo + " sorral" + LocalDateTime.now() + " \n";
        ablak.jTextArea1.setText(cellaklog);

//<--------------------------------------------------------------------------ezt a részt----------------------------------------------------------------->
//visszakapcsoljuk a gombot hogy indítható legyen
        ablak.jButton21.setEnabled(true);

        cellaklog += "Thread stopped now! \n";
        ablak.jTextArea1.setText(cellaklog);

    }

}
