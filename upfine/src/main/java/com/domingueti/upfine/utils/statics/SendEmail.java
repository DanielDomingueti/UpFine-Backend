package com.domingueti.upfine.utils.statics;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

public class SendEmail {

   public void execute() throws MessagingException {
       Properties properties = new Properties();
       properties.put("mail.smtp.host", "smtp.gmail.com");
       properties.put("mail.smtp.port", "465");
       properties.put("mail.smtp.auth", "true");
       properties.put("mail.smtp.starttls.enable", "true");
       properties.put("mail.smtp.starttls.required", "true");
       properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
       properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

       Authenticator auth =  new Authenticator() {
           @Override
           protected PasswordAuthentication getPasswordAuthentication() {
               return new PasswordAuthentication("danielbaudocla@gmail.com", System.getenv("GMAIL_APP_PASSWORD"));
           }
       };

       Session session = Session.getInstance(properties, auth);

       Message message = new MimeMessage(session);
       message.setFrom(new InternetAddress("danielbaudocla@gmail.com"));
       message.setRecipients(
               Message.RecipientType.TO, InternetAddress.parse("danieldomingueti123@gmail.com"));
       message.setSubject("Mail Subject");

       String msg = "This is my first email using JavaMailer";

       MimeBodyPart mimeBodyPart = new MimeBodyPart();
       mimeBodyPart.setContent(msg, "text/html; charset=utf-8");

       Multipart multipart = new MimeMultipart();
       multipart.addBodyPart(mimeBodyPart);

       message.setContent(multipart);

       Transport.send(message);
   }

}
