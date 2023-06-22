package com.domingueti.upfine.modules.RelevantFact.services;

import com.domingueti.upfine.exceptions.BusinessException;
import com.domingueti.upfine.modules.RelevantFact.daos.RelevantFactIpeDAO;
import com.domingueti.upfine.modules.RelevantFact.repositories.RelevantFactRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class GetRelevantFactsByUserIdService {

    final private RelevantFactRepository relevantFactRepository;

    @Transactional
    public List<RelevantFactIpeDAO> execute(Long userId) {
        try {
            return relevantFactRepository.findNameAndCnpjAndRelevantFactAndSubjectAndDateOfTodayByUserId(userId).isEmpty() ?
                    relevantFactRepository.findNameAndCnpjAndRelevantFactAndSubjectAndDateOfToday(userId) :
                        relevantFactRepository.findNameAndCnpjAndRelevantFactAndSubjectAndDateOfTodayByUserId(userId);
        } catch (Exception e) {
            throw new BusinessException("Error while fetching RelevantFact by user ID: " + userId + ". Erro: " + e.getMessage());
        }

    }
}
