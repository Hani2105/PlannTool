/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool;

import PlannTool.CONNECTS.planconnect;
import PlannTool.CTB_CALC.CTB;
import PlannTool.CTB_CALC.CTB_Bejel;
import static PlannTool.CTB_CALC.CTB_Bejel.jTextField1;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gabor_hanacsek
 */
public class Prio_Bejel extends CTB_Bejel {

    @Override
    public void bejel() {

        //csak ha valami van írva a felh nevhez es jelszohoz
        if (jTextField1.getText().length() > 0 && jPasswordField1.getText().length() > 0) {

            // TODO add your handling code here:
            planconnect pc = new planconnect();
            char[] password = jPasswordField1.getPassword();
            String pass = "";

            try {
                pass = String.valueOf(password);
                //pass = pass.replace("+", "%2B");
            } catch (Exception e) {

            }

            //proba universal login
            URL oracle = null;
            try {
                oracle = new URL("http://143.116.140.120/api/auth/authlib.php?username=" + jTextField1.getText() + "&password=" + URLEncoder.encode(pass, "UTF-8") + "");
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(CTB_Bejel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (MalformedURLException ex) {
                Logger.getLogger(CTB_Bejel.class.getName()).log(Level.SEVERE, null, ex);
            }

            BufferedReader in = null;
            try {
                in = new BufferedReader(
                        new InputStreamReader(oracle.openStream()));
            } catch (IOException ex) {
                Logger.getLogger(CTB_Bejel.class.getName()).log(Level.SEVERE, null, ex);
            }

            String inputLine = "";
            String truee = "";
            try {
                while ((inputLine = in.readLine()) != null) {

                    truee = inputLine;
                }
            } catch (IOException ex) {
                Logger.getLogger(CTB_Bejel.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                in.close();
            } catch (IOException ex) {
                Logger.getLogger(CTB_Bejel.class.getName()).log(Level.SEVERE, null, ex);
            }

            //eredeti kód
            String query = "SELECT job_positions_id as poz , pass FROM planningdb.perm where perm.email like '%" + jTextField1.getText().replace(".", "_") + "%'";

            ResultSet rs = null;
            try {
                rs = (ResultSet) pc.lekerdez(query);
            } catch (SQLException ex) {
                Logger.getLogger(CTB_Bejel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(CTB_Bejel.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {
                if (rs.next()) {

                    String poz = "";
                    poz = rs.getString(1);
//sikeres bejelentkezes és planner a jogosultság
                    if (truee.equals("true") && (Integer.parseInt(poz) == 1 || Integer.parseInt(poz) == 2 || Integer.parseInt(poz) == 4)) {

                        ablak.planner = true;
                        ablak.prioment();
                        this.dispose();
//ha jó a login de nem planner jogosultsági szint hanem művez
                    } else if (truee.equals("true") && (Integer.parseInt(poz) == 6 || Integer.parseInt(poz) == 7 || Integer.parseInt(poz) == 8)) {

                        infobox info = new infobox();
                        info.infoBox("Nincs jogosultságod ide!", "Hiba!");

                    } //ha jo az universal login de nem planner es nem muvez              
                    else if (truee.equals("true")) {

                        infobox info = new infobox();
                        info.infoBox("Nincs jogosultságod ide!", "Hiba!");

                    } //ha nem jo az universal login
                    else if (!truee.equals("true")) {

                        infobox info = new infobox();
                        info.infoBox("Hibás adatok a bejelentkezésnél!", "Hiba!");

                    }

                } else if (truee.equals("true")) {

                    infobox info = new infobox();
                    info.infoBox("Nincs jogosultságod ide!", "Hiba!");

                } else if (!truee.equals("true")) {

                    infobox info = new infobox();
                    info.infoBox("Hibás adatok a bejelentkezésnél!", "Hiba!");

                }

            } catch (SQLException ex) {
                Logger.getLogger(CTB_Bejel.class.getName()).log(Level.SEVERE, null, ex);
            }

            pc.kinyir();

        } else {

            infobox info = new infobox();
            info.infoBox("Nem adtál meg Felhasználót / Jelszót!", "Hiba!");

        }

    }

}
