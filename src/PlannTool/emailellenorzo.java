/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author gabor_hanacsek
 */
public class emailellenorzo {

    public boolean check(String emailStr) {

        boolean valide = false;
        Matcher matcher = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE).matcher(emailStr);
        if (matcher.find()) {

            valide = true;
//            System.out.println("The given email is valid");
        } else {
//            System.out.println("The given email is invalid");
            infobox info = new infobox();
            info.infoBox(emailStr + "nem megfelelő formátum!", emailStr);
        }
        return valide;

    }

}
