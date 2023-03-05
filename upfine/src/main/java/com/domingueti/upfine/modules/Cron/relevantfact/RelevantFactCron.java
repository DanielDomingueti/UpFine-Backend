package com.domingueti.upfine.modules.Cron.relevantfact;

import com.domingueti.upfine.modules.RelevantFact.daos.RelevantFactIpeDAO;
import com.domingueti.upfine.modules.RelevantFact.repositories.RelevantFactRepository;
import com.domingueti.upfine.modules.User.repositories.UserRepository;
import com.domingueti.upfine.utils.components.GenerateHtmlEmail;
import com.domingueti.upfine.utils.components.SendEmail;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.util.List;

@Component
@AllArgsConstructor
public class RelevantFactCron {

    final private RelevantFactRepository relevantFactRepository;

    final private UserRepository userRepository;

    final private GenerateHtmlEmail generateHtmlEmail;

    final private SendEmail sendEmail;


    public void execute() {
        final List<RelevantFactIpeDAO> dailyRelevantFacts = relevantFactRepository.findNameAndCnpjAndRelevantFactAndSubjectAndDateOfToday();
        //Use ConvertCnpj rawToFormatted here after refactoring and applying SPR
        final String htmlEmailOutput = generateHtmlEmail.execute(dailyRelevantFacts);
        final List<String> activeEmails = userRepository.findEmailByActiveIsTrueAndDeletedAtIsNull();

        try {

            for (String activeEmail : activeEmails) {
                sendEmail.execute(activeEmail, htmlEmailOutput);
            }

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }

}
