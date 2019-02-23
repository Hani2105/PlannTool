/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import jxl.write.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

/**
 *
 * @author gabor_hanacsek
 */
public class Riporterszal extends Thread {

    //ez az utolso kuldes idopontja
    private static String idopont = "";
    //ez az ora ami utan kuldhetjuk
    private static String kuldesora = "";

    public void run() {

        planconnect pc = new planconnect();

        while (true) {

//megvizsgaljuk hogy kell e futtatni a riportert , lekerdezzuk a tc_riport_kuldest es a tc riport idot
            String query = "SELECT * FROM planningdb.tc_riport_kuldes order by idtc_riport_kuldes desc limit 1";

            try {
                pc.planconnect(query);
                while (pc.rs.next()) {

                    idopont = pc.rs.getString(2);

                }

                pc.kinyir();

                idopont = idopont.substring(0, idopont.length() - 10);
                idopont = idopont.trim();

            } catch (SQLException ex) {
                Logger.getLogger(Riporterszal.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Riporterszal.class.getName()).log(Level.SEVERE, null, ex);
            }

            query = "SELECT * FROM planningdb.tc_riport_ido order by tc_riport_ido.idtc_riport_ido desc limit 1";
            try {
                pc.planconnect(query);
                while (pc.rs.next()) {

                    kuldesora = pc.rs.getString(2);

                }

                pc.kinyir();

                kuldesora = kuldesora.substring(0, kuldesora.length() - 1);

            } catch (SQLException ex) {
                Logger.getLogger(Riporterszal.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Riporterszal.class.getName()).log(Level.SEVERE, null, ex);
            }

// atalakitasa
            //aktualisido
            LocalTime localTime = new LocalTime();
            DateTimeFormatter parseFormat = new DateTimeFormatterBuilder().appendPattern("hh:mm:ss").toFormatter();
            LocalTime kuldesiido = LocalTime.parse(kuldesora, parseFormat);
            //az utolso kuldott datum atalakitasa
            parseFormat = new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd").toFormatter();
            LocalDate kuldesdatum = LocalDate.parse(idopont, parseFormat);
            //aktualisdatum
            LocalDate localDate = new LocalDate();

//ha a beállított időnél nagyobb a now idő és a nowdátum nagyobb mint amikor utoljara kuldtunk akkor mehetunk tovabb
            if (localTime.isAfter(kuldesiido) && localDate.isAfter(kuldesdatum)) {

//lefuttajuk az url-t
                xmlfeldolg f = new xmlfeldolg();
                URL url = null;
                Object rowdata[][] = null;
                try {

                    url = new URL("http://143.116.140.120/rest/request.php?page=planning_iswip&product=&format=xml");
                    ArrayList<String> lista = new ArrayList();

                    String nodelist = "planning_iswip";
                    lista.add("Serial_Number");
                    lista.add("Part_Number");
                    lista.add("SFDC_Location_Name");
                    lista.add("Days_in_Location");
                    lista.add("Shop_Order");

                    rowdata = (Object[][]) f.xmlfeldolg(url, nodelist, lista);

                } catch (MalformedURLException ex) {
                    Logger.getLogger(ablak.class.getName()).log(Level.SEVERE, null, ex);
                }

//kiszedjuk a prefixeket
                query = "select * from tc_riport_prefix";
                ArrayList<String> prefixek = new ArrayList<>();

                try {
                    pc.planconnect(query);
                    while (pc.rs.next()) {

                        prefixek.add(pc.rs.getString(2));

                    }

                    pc.kinyir();

                } catch (SQLException ex) {
                    Logger.getLogger(Riporterszal.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Riporterszal.class.getName()).log(Level.SEVERE, null, ex);
                }

//lekerdezzuk a beallitott days in wip et
                query = "SELECT * FROM planningdb.tc_riport_ido order by idtc_riport_ido desc limit 1";
                int age = 100000;

                try {
                    pc.planconnect(query);
                    while (pc.rs.next()) {

                        age = pc.rs.getInt(3);

                    }

                    pc.kinyir();

                } catch (SQLException ex) {
                    Logger.getLogger(Riporterszal.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Riporterszal.class.getName()).log(Level.SEVERE, null, ex);
                }

                String tablaadat = "";
                int szamlalo = 0;
//vegigporgetjuk a rowdatát és ha nagyobb a days in loc mint a beállított és stimmel a prefix akkor berakjuk a html táblába
                for (int i = 0; i < rowdata.length; i++) {

//ha nagyobb a days in loc minbt ami be van allitva
                    if (Integer.parseInt(rowdata[i][3].toString()) >= age) {

                        for (int n = 0; n < prefixek.size(); n++) {

//ha benne van a prefix a pn ben
                            if (rowdata[i][1].toString().toLowerCase().contains(prefixek.get(n).toLowerCase())) {

                                tablaadat += "<tr><td>" + rowdata[i][0].toString() + "</td><td>" + rowdata[i][1].toString() + "</td><td>" + rowdata[i][2].toString() + "</td><td>" + rowdata[i][3].toString() + "</td><td>" + rowdata[i][4].toString() + "</td></tr>";
                                szamlalo++;
                            }

                        }

                    }

                }

//összerakjuk a prefieket stringe
                String prefixstzring = "";
                for (int i = 0; i < prefixek.size(); i++) {

                    prefixstzring += prefixek.get(i) + ", ";

                }

                prefixstzring = prefixstzring.substring(0, prefixstzring.length() - 2);

                //megvan a listank , feldolgozzuk , csinalunk belole html formátumot
                String level = "<html>\n"
                        + "    <head>\n"
                        + "        <title>TODO supply a title</title>\n"
                        + "        <meta charset=\"UTF-8\">\n"
                        + "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                        + "    </head>\n"
                        + "    <body>\n"
                        + "        <div>Ez egy automata levél a régi egységekről , mely a következő beállításokkal futott le: </div>\n"
                        + "        <div>Days in loc nagyobb vagy egyenlő: " + age + " nap</div>\n"
                        + "        <div>Figyelt prefixek: " + prefixstzring + "</div>\n"
                        + "        <div>Küldés időpontja nagyobb mint: " + kuldesiido.toString().substring(0, kuldesiido.toString().length() - 4) + " óra</div></br></br>\n"
                        + "        <div>Ezen beállítások szerint " + szamlalo + " db egység van a WIP-en!</div></br></br>\n"
                        + "<table border = \"5\"><th>Serial N</th><th>Part N</th><th>Location N</th><th>Days in Loc</th><th>Shop Order</th>";

                level += tablaadat + "</table></body></html>";

//lekerdezzuk a cimzetteket
                String cimzettek = "";

                query = "select tc_riport_email.email from tc_riport_email";

                try {
                    pc.planconnect(query);

                    while (pc.rs.next()) {

                        cimzettek += pc.rs.getString(1) + ",";

                    }

                    pc.kinyir();

//kikuldjuk a levelet
                } catch (SQLException ex) {
                    Logger.getLogger(Riporterszal.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Riporterszal.class.getName()).log(Level.SEVERE, null, ex);
                }

                try {

                    cimzettek = cimzettek.substring(0, cimzettek.length() - 1);
                } catch (Exception e) {
                }

                Tc_Levelkuldes l = new Tc_Levelkuldes("Aging riport", level, cimzettek, "PlannTool_Riporter@sanmina.com");
                l.start();

//beirjuk a jelenlegi datumot az adatbazisba h kiment a level
                String most = localDate.toString();

                query = "insert into tc_riport_kuldes (tc_riport_kuldes.datum) values ('" + most + "')";
                pc.feltolt(query, false);

// itt van az if vége , amit ciklusban szeretnenk futtatni
            }

            pc.kinyir();

            try {
                Thread.sleep(30 * 60 * 1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Riporterszal.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

}
