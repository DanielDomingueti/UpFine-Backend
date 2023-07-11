package com.domingueti.upfine.modules.RelevantFact.services;

import com.domingueti.upfine.exceptions.BusinessException;
import com.domingueti.upfine.modules.RelevantFact.daos.RelevantFactIpeDAO;
import com.domingueti.upfine.modules.User.models.User;
import com.domingueti.upfine.modules.User.repositories.UserRepository;
import com.domingueti.upfine.utils.components.GenerateHtmlEmail;
import com.domingueti.upfine.utils.components.GeneratePdfEmail;
import com.domingueti.upfine.utils.components.SendEmail;
import com.domingueti.upfine.utils.components.dtos.GenerateHtmlEmailDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;

import static java.time.LocalDate.now;

@Service
@AllArgsConstructor
public class SendDailyRelevantFactService {

    final private UserRepository userRepository;

    final private GetRelevantFactsByUserIdService getRelevantFactsByUserIdService;

    final private GenerateHtmlEmail generateHtmlEmail;

    final private GeneratePdfEmail generatePdfEmail;

    final private SendEmail sendEmail;

    @Transactional
    public void execute() {

       final List<User> activeUsers = userRepository.findByActiveIsTrueAndDeletedAtIsNull();

        try {
            for (User activeUser : activeUsers) {
                final List<RelevantFactIpeDAO> filteredDailyRelevantFacts = getRelevantFactsByUserIdService.execute(activeUser.getId());

                if (!filteredDailyRelevantFacts.isEmpty()) {
                    final String htmlEmailOutput = generateHtmlEmail.execute(new GenerateHtmlEmailDTO(activeUser.getName(), now()));
                    final File attachmentPdf = generatePdfEmail.execute(filteredDailyRelevantFacts);
                    sendEmail.execute(activeUser.getEmail(), htmlEmailOutput, attachmentPdf);
                }
            }
        } catch (Exception e) {
            throw new BusinessException("CRON: Error on sending daily relevant facts. Error: " + e.getMessage());
        }
    }
}