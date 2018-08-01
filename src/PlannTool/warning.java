/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool;

import java.time.Duration;
import static java.time.Instant.now;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 *
 * @author gabor_hanacsek
 */
public class warning {

    LocalDateTime masodik = LocalDateTime.now();
    int counter = 0;
    ablak a;

    public void keszlet(ablak a) {
        this.a = a;
        a.wgcounter++;
        String[] nev = System.getProperty("user.name").split("_");
        String keresztnev = nev[0].substring(0, 1).toUpperCase() + nev[0].substring(1, nev[0].length());

        if (a.wgcounter >= 2) {

            Duration duration = Duration.between(a.elso, masodik);
            long diff = Math.abs(duration.toMinutes());
            //System.out.println(diff);
            a.elso = masodik;

            if (diff < 2 && a.jTextField2.getText().substring(1, 4).equals(a.pref)) {

                infobox info = new infobox();
                info.infoBox("Egy percen belül keresel ugyan arra a termékcsaládra többször! \n Kedves " + keresztnev + " , a te és a rendszer érdekében kérdezz le prefixre (PL: LFHB), \n  majd használd a kereső funkciót!  (bal - alsó)", "Jótanács!");

            }

        }

        a.pref = a.jTextField2.getText().substring(1, 4);

    }

}
