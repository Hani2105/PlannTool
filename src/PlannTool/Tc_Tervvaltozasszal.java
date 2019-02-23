/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.joda.time.DateTime;
import static org.joda.time.DateTime.now;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author gabor_hanacsek
 */
public class Tc_Tervvaltozasszal extends Thread {

    JTable m = null;
    String neve = null;

    public Tc_Tervvaltozasszal(JTable m, String neve) {
        this.m = m;
        this.neve = neve;
    }

    public void run() {
        String level = "<html>\n"
                + "    <head>\n"
                + "        <title></title>\n"
                + "        <meta charset=\"UTF-8\">\n"
                + "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                + "    </head>\n"
                + "    <body>\n"
                + "    <div style=\"font-size:200%\">Terv változás történt a <span style=\"color:red\">" + neve + "</span> cellában!</div>\n"
                + "    <div style=\"font-size:200%\">Az aktuális terv a következő:</div>\n";
//tábla indít és fej indít
        level += "<table style=\"width:100%\" border = \"5\"><thead>";
//hozzáadjuk a table headeket
        level += "<tr>";
        for (int i = 0; i < m.getColumnCount(); i++) {

            level += "<th>" + m.getColumnName(i) + "</th>";

        }
        level += "</tr>";

//bezárjuk a fejet
        level += "</thead><tbody>";
//hozzáadjuk a létező sorokat

        for (int r = 0; r < m.getRowCount(); r++) {
//hozzáadunk egy sort
            level += "<tr>";
//a második sortól indulunk
            for (int o = 0; o < m.getColumnCount(); o++) {

                String cellaertek = "";
//hozzáadjuk a cellákat a sorból
                try {
                    cellaertek = m.getValueAt(r, o).toString();
                } catch (Exception e) {
                }

                level += "<td>" + cellaertek + "</td>";

            }
//bezárjuk a sort
            level += "</tr>";

        }

        level += "</tbody></table></body></html>";
//a dátum beformázása
        Date date = new Date();
        String modifiedDate = new SimpleDateFormat("yyyy-MM-dd hh:mm").format(date);

//lekerdezzuk az adatbazisbol a cimlistat es atalakitjuk a megfelelő formátumba
//behuzzuk a cimlistat
        String query = "SELECT tc_tervvaltozas_cimlista.email FROM planningdb.tc_tervvaltozas_cimlista";
        planconnect pc = new planconnect();

        try {
            pc.planconnect(query);
        } catch (SQLException ex) {
            Logger.getLogger(Tc_Tervvaltozasszal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Tc_Tervvaltozasszal.class.getName()).log(Level.SEVERE, null, ex);
        }

        //cimlista string
        String cimlista = "";

        try {
            while (pc.rs.next()) {

                cimlista += pc.rs.getString(1) + ",\n";
            }
        } catch (SQLException ex) {
            Logger.getLogger(Tc_Tervvaltozasszal.class.getName()).log(Level.SEVERE, null, ex);
        }

        pc.kinyir();
//a levél elküldése
        Tc_Levelkuldes t = new Tc_Levelkuldes("Tervváltozás " + neve + " " + modifiedDate, level, cimlista, "PlannTool@sanmina.com");
        t.start();

    }

}
