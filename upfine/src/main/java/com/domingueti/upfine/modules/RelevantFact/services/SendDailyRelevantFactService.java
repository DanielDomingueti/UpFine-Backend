package com.domingueti.upfine.modules.RelevantFact.services;

import com.domingueti.upfine.exceptions.BusinessException;
import com.domingueti.upfine.modules.RelevantFact.daos.RelevantFactIpeDAO;
import com.domingueti.upfine.modules.User.models.User;
import com.domingueti.upfine.modules.User.repositories.UserRepository;
import com.domingueti.upfine.utils.components.GenerateHtmlEmail;
import com.domingueti.upfine.utils.components.SendEmail;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class SendDailyRelevantFactService {

    final private UserRepository userRepository;

    final private GetRelevantFactsByUserIdService getRelevantFactsByUserIdService;

    final private GenerateHtmlEmail generateHtmlEmail;

    final private SendEmail sendEmail;

    @Transactional
    public void execute() {

       final List<User> activeUsers = userRepository.findByActiveIsTrueAndDeletedAtIsNull();

        try {
            for (User activeUser : activeUsers) {
                final List<RelevantFactIpeDAO> filteredDailyRelevantFacts = getRelevantFactsByUserIdService.execute(activeUser.getId());

                if (!filteredDailyRelevantFacts.isEmpty()) {
                    final String htmlEmailOutput = generateHtmlEmail.execute(filteredDailyRelevantFacts);
                    sendEmail.execute(activeUser.getEmail(), htmlEmailOutput);
                }
            }
        } catch (Exception e) {
            throw new BusinessException("CRON: Error on sending daily relevant facts. Error: " + e.getMessage());
        }
    }
}