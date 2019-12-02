/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import service.REST_JavaSE;

/**
 *
 * @author Sebastian Schiefermayr
 */
public class EmailUtil {

    private static EmailUtil instance = null;
    
    private final Session session;
    
    // Props for reading the Config
    private Properties confProp = new Properties();
    
    public static EmailUtil getInstance() {
        if (instance == null) {
            instance = new EmailUtil();
        }
        return instance;
    }
    
    private EmailUtil() {
        session = initEmailNotification();
    }

    private Session initEmailNotification() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) { // Local testing new FileInputStream("src/main/resources/config.properties")
            // Props for sending Email
            Properties prop = new Properties();

            // load the config file
            confProp.load(input);
            
            // Setup the Mail-Account
            prop.put("mail.smtp.auth", true);
            prop.put("mail.smtp.starttls.enable", "true");
            prop.put("mail.smtp.host", confProp.getProperty("mail.host"));
            prop.put("mail.smtp.port", confProp.getProperty("mail.port"));
            prop.put("mail.smtp.ssl.trust", confProp.getProperty("mail.host"));

            Session session = Session.getInstance(prop, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(confProp.getProperty("mail.username"), confProp.getProperty("mail.password"));
                }
            });
            return session;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public void sendNotification(final String receiverMail, final String subject, final String messageText) {

        if (!receiverMail.isEmpty()) {            
            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(this.confProp.getProperty("mail.from")));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiverMail));
                message.setSubject(subject);

                MimeBodyPart mimeBodyPart = new MimeBodyPart();
                mimeBodyPart.setContent(messageText, "text/html");

                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(mimeBodyPart);

                message.setContent(multipart);

                Transport.send(message);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public Properties getConfigProps(){
        return this.confProp;
    }
}
