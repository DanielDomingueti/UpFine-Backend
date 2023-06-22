package com.domingueti.upfine.modules.Cron.relevantfact;

import com.domingueti.upfine.exceptions.BusinessException;
import com.domingueti.upfine.modules.RelevantFact.daos.RelevantFactIpeDAO;
import com.domingueti.upfine.modules.RelevantFact.services.GetRelevantFactsByUserIdService;
import com.domingueti.upfine.modules.User.models.User;
import com.domingueti.upfine.modules.User.repositories.UserRepository;
import com.domingueti.upfine.utils.components.GenerateHtmlEmail;
import com.domingueti.upfine.utils.components.SendEmail;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class RelevantFactCron {

    final private UserRepository userRepository;

    final private GetRelevantFactsByUserIdService getRelevantFactsByUserIdService;

    final private GenerateHtmlEmail generateHtmlEmail;

    final private SendEmail sendEmail;


    public void execute() {
        final List<User> activeUsers = userRepository.findByActiveIsTrueAndDeletedAtIsNull();

        try {

            for (User activeUser : activeUsers) {
                final List<RelevantFactIpeDAO> filteredDailyRelevantFacts = getRelevantFactsByUserIdService.execute(activeUser.getId());

                final String htmlEmailOutput = generateHtmlEmail.execute(filteredDailyRelevantFacts);
                sendEmail.execute(activeUser.getEmail(), htmlEmailOutput);
            }

        } catch (Exception e) {
            throw new BusinessException("Erro ao rodar CRON de RelevantFact." + e.getMessage());
        }
    }

}
