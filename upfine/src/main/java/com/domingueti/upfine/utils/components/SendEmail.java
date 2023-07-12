package com.domingueti.upfine.utils.components;

import com.domingueti.upfine.modules.Config.services.GetConfigByNameService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import static java.time.LocalDate.now;
import static javax.mail.Message.RecipientType.TO;
import static javax.mail.Transport.send;
import static javax.mail.internet.InternetAddress.parse;

@AllArgsConstructor
@Component
public class SendEmail {

    final private GetConfigByNameService getConfigByNameService;

    public void execute(String to, String text, File attachmentPdf) throws MessagingException {
        final Session session = Session.getInstance(createSmptProperties(), createAuthenticationWithEmailAndAppPassword());
        Message message = createMessage(session, to, text, attachmentPdf);
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

    private Message createMessage(Session session, String to, String text, File attachmentPdf) {
        final String sendFrom = getConfigByNameService.execute("EMAIL-SENDER").getValue();
        final String subject = "Fato relevante: " + now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        Message message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(sendFrom));
            message.setRecipients(TO, parse(to));
            message.setSubject(subject);
            message.setContent(createMultipart(text, attachmentPdf));
            return message;
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private Multipart createMultipart(String text, File attachmentPdf) {
        try {
            MimeBodyPart textBodyPart = new MimeBodyPart();
            textBodyPart.setContent(text, "text/html; charset=UTF-8");

            MimeBodyPart attachmentBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(attachmentPdf);
            attachmentBodyPart.setDataHandler(new DataHandler(source));
            attachmentBodyPart.setFileName("fato-relevante-" + now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ".pdf");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(textBodyPart);
            multipart.addBodyPart(attachmentBodyPart);
            return multipart;
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

}
