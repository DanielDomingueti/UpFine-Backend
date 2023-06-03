package com.domingueti.upfine.modules.RelevantFact.services;

import com.domingueti.upfine.exceptions.BusinessException;
import com.domingueti.upfine.modules.RelevantFact.models.RelevantFact;
import com.domingueti.upfine.modules.RelevantFact.repositories.RelevantFactRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class InsertRelevantFactFromCronService {

    final private RelevantFactRepository relevantFactRepository;


    @Transactional
    public void execute(Long ipeId) {

        try {
//            sleep(2000);
//          String summarizedPdfContent = gptClient.summarizeText(rawPdfContent, 300);
            final String summarizedPdfContent = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industrys standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.";

            RelevantFact relevantFact = new RelevantFact();
            relevantFact.setIpeId(ipeId);
            relevantFact.setSummarized(summarizedPdfContent);
            relevantFactRepository.save(relevantFact);
        } catch (Exception e) {
            throw new BusinessException("Error while inserting RelevantFact from CRON: " + e.getMessage());
        }

    }
}
