/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool;

import java.sql.SQLException;
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
public class Riporter {

    //ez az utolso kuldes idopontja
    private static String idopont = "";
    //ez az ora ami utan kuldhetjuk
    private static String kuldesora = "";

    static void Riporter() {

//megvizsgaljuk hogy kell e futtatni a riportert , lekerdezzuk a tc_riport_kuldest es a tc riport idot
        String query = "SELECT * FROM planningdb.tc_riport_kuldes order by idtc_riport_kuldes desc limit 1";
        planconnect pc = new planconnect();
        try {
            pc.planconnect(query);
            while (pc.rs.next()) {

                idopont = pc.rs.getString(2);

            }
            
            idopont= idopont.substring(0, idopont.length()-10);
            idopont = idopont.trim();

        } catch (SQLException ex) {
            Logger.getLogger(Riporter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Riporter.class.getName()).log(Level.SEVERE, null, ex);
        }

        query = "SELECT * FROM planningdb.tc_riport_ido order by tc_riport_ido.idtc_riport_ido desc limit 1";
        try {
            pc.planconnect(query);
            while (pc.rs.next()) {

                kuldesora = pc.rs.getString(2);

            }
            
            kuldesora = kuldesora.substring(0, kuldesora.length()-1);

        } catch (SQLException ex) {
            Logger.getLogger(Riporter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Riporter.class.getName()).log(Level.SEVERE, null, ex);
        }

        //akkor kell tovabbmenni a koddal ha elmult a kuldesi ido es meg ma nem kuldtunk
        //aktualisido
        LocalTime localTime = new LocalTime();
        DateTimeFormatter parseFormat = new DateTimeFormatterBuilder().appendPattern("hh:mm:ss").toFormatter();
        LocalTime kuldesiido = LocalTime.parse(kuldesora, parseFormat);
        //az utolso kuldott datum atalakitasa
        parseFormat = new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd").toFormatter();
        LocalDate kuldesdatum = LocalDate.parse(idopont, parseFormat);
        //aktualisdatum
        LocalDate localDate = new LocalDate();
        
        if(kuldesiido.isAfter(localTime) && localDate.isAfter(kuldesdatum)){
        
           
        
        }

    }

}
