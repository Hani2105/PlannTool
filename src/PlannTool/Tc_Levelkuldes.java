/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool;

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

    public Tc_Levelkuldes(String Subject, String szoveg, String cimzett) {

        this.Subject = Subject;
        this.szoveg = szoveg;
        this.cimzett = cimzett;

    }

    public void run() {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        String to = cimzett;//change accordingly  
        String from = "Muszakjelentes@sanmina.com"; //change accordingly  
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
            message.setSubject(Subject);
            //message.setText(szoveg);
            message.setContent(szoveg, "text/html; charset=utf-8");

            // Send message  
            Transport.send(message);
            //System.out.println("message sent successfully....");

        } catch (MessagingException mex) {
            stat.beir(System.getProperty("user.name"), jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()), "Elhasaltunk a Tc levelkuldes reszen" + mex, "gabor.hanacsek@sanmina.com");
        }

    }

}
