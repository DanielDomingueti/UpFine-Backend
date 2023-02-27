package com.domingueti.upfine.modules.Cron.relevantfact.services;

import com.domingueti.upfine.modules.Cron.relevantfact.daos.RelevantFactIpeDAO;
import com.domingueti.upfine.modules.RelevantFact.repositories.RelevantFactRepository;
import com.domingueti.upfine.utils.statics.SendEmail;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.util.List;

@Component
@AllArgsConstructor
public class RelevantFactCron {

    private RelevantFactRepository relevantFactRepository;

    public void execute() {
        List<RelevantFactIpeDAO> relevantFactIpeDAOs = relevantFactRepository.findNameAndCnpjAndRelevantFactAndSubjectAndDateOfToday();
        SendEmail sendEmail = new SendEmail("smtp.gmail.com", "587", "danielbaudocla@gmail.com", "Daniel17");


        try {

            sendEmail.execute("danieldomingueti@hotmail.com.com", "Test Email", "Hello, this is a test email!");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }

}
