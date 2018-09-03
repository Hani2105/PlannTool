/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool;

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
public class Stat {

    public void beir(String Username, String Tabname, String szoveg, String cimzett) {

        
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  
        LocalDateTime now = LocalDateTime.now(); 
        String Query = "insert into planningdb.plannstat (username , tabname) values ('"+Username+"','"+Tabname+"')";
        planconnect conn = new planconnect();
        conn.feltolt(Query , false);
        
        
        String to = cimzett;//change accordingly  
        String from = "PlannTool@sanmina.com"; //change accordingly  
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
            message.setSubject("PlannTool használat!" + dtf.format(now));
            message.setText(Username +"   " +Tabname + "  " + dtf.format(now) + szoveg);

            // Send message  
            Transport.send(message);
            //System.out.println("message sent successfully....");

        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
        
    }

}
