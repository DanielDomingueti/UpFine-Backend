package com.domingueti.upfine.modules.Corporation.services;

import com.domingueti.upfine.exceptions.BusinessException;
import com.domingueti.upfine.exceptions.InvalidRequestException;
import com.domingueti.upfine.modules.Corporation.dtos.ChooseCorporationDTO;
import com.domingueti.upfine.modules.Corporation.models.Corporation;
import com.domingueti.upfine.modules.Corporation.repositories.CorporationRepository;
import com.domingueti.upfine.modules.Corporation.validators.InsertUserChosenCorporationsValidator;
import com.domingueti.upfine.modules.User.models.User;
import com.domingueti.upfine.modules.User.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static java.time.LocalDate.now;

@Service
@AllArgsConstructor
public class InsertUserChosenCorporationsService {

    final private UserRepository userRepository;

    final private CorporationRepository corporationRepository;

    final private InsertUserChosenCorporationsValidator validator;

    @Transactional
    public void execute(ChooseCorporationDTO chooseCorporationDTO) {
        validator.execute(chooseCorporationDTO);

        try {

            User user = new User();
            final Optional<User> userOptional = userRepository.findByEmail(chooseCorporationDTO.getEmail());

            if (userOptional.isEmpty()) {
                user.setName(chooseCorporationDTO.getName());
                user.setEmail(chooseCorporationDTO.getEmail());
                user.setActive(true);
                user.setReferenceDate(now());
                user = userRepository.save(user);
            } else {
                user = userOptional.get();
            }

            deletePreviousChosenCorporations(user);
            insertNewChosenCorporations(user, chooseCorporationDTO.getCorporationIds());

        }
        catch (InvalidRequestException e) {
            throw e;
        }
        catch (Exception e) {
            throw new BusinessException("Error while inserting chosen corporations for user: " + e.getMessage());
        }

    }

    private void deletePreviousChosenCorporations(User user) {
        user.getCorporations().clear();
        userRepository.save(user);
    }

    private void insertNewChosenCorporations(User user, List<Long> corporationIds) {
        for (Long corporationId : corporationIds) {
            Corporation corporation = corporationRepository.findById(corporationId).get();
            user.getCorporations().add(corporation);
        }
        userRepository.save(user);
    }

}
