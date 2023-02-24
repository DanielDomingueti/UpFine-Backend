package com.domingueti.upfine.modules.Cron.relevantfact.services;

import com.domingueti.upfine.modules.Cron.relevantfact.daos.RelevantFactIpeDAO;
import com.domingueti.upfine.modules.RelevantFact.repositories.RelevantFactRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class RelevantFactCron {

    private RelevantFactRepository relevantFactRepository;

    public void execute() {
        List<RelevantFactIpeDAO> relevantFactIpeDAOs = relevantFactRepository.findNameAndCnpjAndRelevantFactAndSubjectAndDateOfToday();


    }

}
