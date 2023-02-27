package com.domingueti.upfine.utils.statics;

import com.sun.mail.smtp.SMTPTransport;
import lombok.AllArgsConstructor;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@AllArgsConstructor
public class SendEmail {

    private String accessToken;

    public void sendEmail(String toEmail, String subject, String body) throws MessagingException {
        // Create properties object for the email session
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.smtp.auth.mechanisms", "XOAUTH2");
        props.put("mail.smtp.auth.login.disable", "true");
        props.put("mail.smtp.auth.plain.disable", "true");

        // Create a Session object with the properties and authentication information
        Session session = Session.getInstance(props, null);
        SMTPTransport transport = (SMTPTransport)session.getTransport("smtp");

        // Connect to the SMTP server using OAuth2 authentication
        transport.connect("smtp.gmail.com", 587, null, null);
        transport.issueCommand("AUTH XOAUTH2 " + getOAuth2Token(), 235);

        // Create a MimeMessage object
        MimeMessage message = new MimeMessage(session);

        // Set the From, To, Subject, and Content fields of the email
        message.setFrom(new InternetAddress("danielbaudocla@gmail.com"));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
        message.setSubject(subject);
        message.setText(body);

        // Send the email
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }

    private String getOAuth2Token() {
        return "user=" + "sender@gmail.com" + "\1auth=Bearer " + accessToken + "\1\1";
    }

}
