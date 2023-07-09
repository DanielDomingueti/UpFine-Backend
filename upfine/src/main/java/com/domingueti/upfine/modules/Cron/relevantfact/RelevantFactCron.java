package com.domingueti.upfine.modules.Cron.relevantfact;

import com.domingueti.upfine.exceptions.BusinessException;
import com.domingueti.upfine.modules.Ipe.models.Ipe;
import com.domingueti.upfine.modules.Ipe.repositories.IpeRepository;
import com.domingueti.upfine.modules.RelevantFact.services.InsertRelevantFactCronService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@AllArgsConstructor
public class RelevantFactCron {

    final private IpeRepository ipeRepository;

    final private InsertRelevantFactCronService insertRelevantFactCronService;

    @Transactional
    public void execute() {

        try {
            final List<Ipe> newIpes = ipeRepository.findByNonExistingRelevantFactAndDeletedAtIsNull();
            newIpes.forEach(ipe -> insertRelevantFactCronService.execute(ipe.getId()));
        }
        catch (Exception e) {
            throw new BusinessException("CRON: Error on fetching and inserting new relevant facts. Error: " + e.getMessage());
        }
    }
}