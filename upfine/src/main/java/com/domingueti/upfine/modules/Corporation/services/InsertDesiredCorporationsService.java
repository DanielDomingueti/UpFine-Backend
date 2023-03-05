package com.domingueti.upfine.modules.Corporation.services;

import com.domingueti.upfine.exceptions.BusinessException;
import com.domingueti.upfine.exceptions.InvalidRequestException;
import com.domingueti.upfine.modules.Corporation.dtos.CorporationSelectionDTO;
import com.domingueti.upfine.modules.Corporation.models.PivotCorporationUser;
import com.domingueti.upfine.modules.Corporation.repositories.PivotCorporationUserRepository;
import com.domingueti.upfine.modules.Corporation.validators.InsertDesiredCorporationsValidator;
import com.domingueti.upfine.modules.User.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class InsertDesiredCorporationsService {

    final private PivotCorporationUserRepository pivotCorporationUserRepository;

    final private UserRepository userRepository;

    final private InsertDesiredCorporationsValidator validator;

    public void execute(CorporationSelectionDTO corporationSelectionDTO) {
        validator.execute(corporationSelectionDTO);

        try {

            final Long userId = userRepository.findUserIdByEmail(corporationSelectionDTO.getEmail());

            deletePreviousDesiredCorporations(userId);
            insertNewDesiredCorporations(userId, corporationSelectionDTO.getCorporationIds());

        }
        catch (InvalidRequestException e) {
            throw e;
        }
        catch (Exception e) {
            throw new BusinessException("Error while inserting chosen corporations for user.");
        }

    }

    private void deletePreviousDesiredCorporations(Long userId) {
        pivotCorporationUserRepository.deleteAllByUserId(userId);
    }

    private void insertNewDesiredCorporations(Long userId, List<Long> corporationIds) {
        List<PivotCorporationUser> pivotCorporationUsers = new ArrayList<>();
        for (Long corporationId : corporationIds) {
            PivotCorporationUser pivotCorporationUser = new PivotCorporationUser();
            pivotCorporationUser.setUserId(userId);
            pivotCorporationUser.setCorporationId(corporationId);
            pivotCorporationUsers.add(pivotCorporationUser);
        }
        pivotCorporationUserRepository.saveAll(pivotCorporationUsers);
    }

}
