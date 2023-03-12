package com.domingueti.upfine.modules.Corporation.services;

import com.domingueti.upfine.exceptions.BusinessException;
import com.domingueti.upfine.exceptions.InvalidRequestException;
import com.domingueti.upfine.modules.Corporation.dtos.ChooseCorporationDTO;
import com.domingueti.upfine.modules.Corporation.models.Corporation;
import com.domingueti.upfine.modules.Corporation.repositories.CorporationRepository;
import com.domingueti.upfine.modules.Corporation.validators.InsertDesiredCorporationsValidator;
import com.domingueti.upfine.modules.User.models.User;
import com.domingueti.upfine.modules.User.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class InsertDesiredCorporationsService {

    final private UserRepository userRepository;

    final private CorporationRepository corporationRepository;

    final private InsertDesiredCorporationsValidator validator;

    @Transactional
    public void execute(ChooseCorporationDTO chooseCorporationDTO) {
        validator.execute(chooseCorporationDTO);

        try {

            final User user = userRepository.findByEmail(chooseCorporationDTO.getEmail());

            deletePreviousDesiredCorporations(user);
            insertNewDesiredCorporations(user, chooseCorporationDTO.getCorporationIds());

        }
        catch (InvalidRequestException e) {
            throw e;
        }
        catch (Exception e) {
            throw new BusinessException("Error while inserting chosen corporations for user: " + e.getMessage());
        }

    }

    private void deletePreviousDesiredCorporations(User user) {
        user.getCorporations().clear();
        userRepository.save(user);
    }

    private void insertNewDesiredCorporations(User user, List<Long> corporationIds) {
        for (Long corporationId : corporationIds) {
            Corporation corporation = corporationRepository.findById(corporationId).get();
            user.getCorporations().add(corporation);
        }
        userRepository.save(user);
    }

}
