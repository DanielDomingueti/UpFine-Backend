package com.domingueti.upfine.utils.statics;

import lombok.NoArgsConstructor;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

import static javax.mail.Message.RecipientType.TO;
import static javax.mail.Transport.send;
import static javax.mail.internet.InternetAddress.parse;

@NoArgsConstructor
public class SendEmail {

    public void execute(String from, String to, String subject, String text) throws MessagingException {
       final Session session = Session.getInstance(createSmptProperties(), createAuthenticationWithEmailAndAppPassword());
       Message message = createMessage(session, from, to, subject, text);
       send(message);
   }

    private Properties createSmptProperties() {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.starttls.required", "true");
        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        return properties;
    }

    private Authenticator createAuthenticationWithEmailAndAppPassword() {
        return  new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("danielbaudocla@gmail.com", System.getenv("GMAIL_APP_PASSWORD"));
            }
        };
    }

    private Message createMessage(Session session, String from, String to, String subject, String text) {
        Message message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(from));
            message.setRecipients(TO, parse(to));
            message.setSubject(subject);
            message.setContent(createMultipart(text));
            return message;
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private Multipart createMultipart(String text) {
        try {
            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(text, "text/html; charset=UTF-8");
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);
            return multipart;
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

}
