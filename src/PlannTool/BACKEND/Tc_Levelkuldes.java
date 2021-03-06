/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool.BACKEND;

import static PlannTool.ablak.jTabbedPane1;
import static PlannTool.ablak.stat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author gabor_hanacsek
 */
public class Tc_Levelkuldes extends Thread {

    String Subject;
    String szoveg;
    String cimzett;
    String kuldo;

    public Tc_Levelkuldes(String Subject, String szoveg, String cimzett, String kuldo) {

        this.Subject = Subject;
        this.szoveg = szoveg;
        this.cimzett = cimzett;
        this.kuldo = kuldo;

    }

    public void run() {

        String to = this.cimzett;//change accordingly  
        String from = this.kuldo; //change accordingly  
        String host = "mailhub.sanmina.com";//or IP address  

        //Get the session object  
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", host);
        Session session = Session.getDefaultInstance(properties);

        //compose the message
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(this.Subject);
            //message.setText(szoveg);
            message.setContent(this.szoveg, "text/html; charset=utf-8");

            // Send message  
            Transport.send(message);
            //System.out.println("message sent successfully....");
            //stat.beir(System.getProperty("user.name"), jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()), "Elkuldtuk a levelet" + cimzett + szoveg, "gabor.hanacsek@sanmina.com");

        } catch (MessagingException mex) {
            //stat.beir(System.getProperty("user.name"), jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()), "Elhasaltunk a Tc levelkuldes reszen" + mex, "gabor.hanacsek@sanmina.com");
        }

    }

}
