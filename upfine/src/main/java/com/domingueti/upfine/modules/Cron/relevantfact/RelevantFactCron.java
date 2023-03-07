package com.domingueti.upfine.modules.Cron.relevantfact;

import com.domingueti.upfine.modules.RelevantFact.daos.RelevantFactIpeDAO;
import com.domingueti.upfine.modules.RelevantFact.repositories.RelevantFactRepository;
import com.domingueti.upfine.modules.User.models.User;
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
        final List<User> activeUsers = userRepository.findByActiveIsTrueAndDeletedAtIsNull();

        try {

            for (User activeUser : activeUsers) {
                final List<RelevantFactIpeDAO> filteredDailyRelevantFactsByChosenCorporations = filterByUserChosenCorporation(activeUser.getId());
                final String htmlEmailOutput = generateHtmlEmail.execute(filteredDailyRelevantFactsByChosenCorporations);
                sendEmail.execute(activeUser.getEmail(), htmlEmailOutput);
            }

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }

    private List<RelevantFactIpeDAO> filterByUserChosenCorporation(Long userId) {
        return relevantFactRepository.findNameAndCnpjAndRelevantFactAndSubjectAndDateOfTodayByUserId(userId).isEmpty() ?
                        relevantFactRepository.findNameAndCnpjAndRelevantFactAndSubjectAndDateOfToday(userId) :
                        relevantFactRepository.findNameAndCnpjAndRelevantFactAndSubjectAndDateOfTodayByUserId(userId);
    }

}
