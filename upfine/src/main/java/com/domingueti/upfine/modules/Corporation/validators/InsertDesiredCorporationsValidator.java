package com.domingueti.upfine.modules.Corporation.validators;

import com.domingueti.upfine.exceptions.FieldMessage;
import com.domingueti.upfine.exceptions.InvalidRequestException;
import com.domingueti.upfine.modules.Corporation.dtos.ChooseCorporationDTO;
import com.domingueti.upfine.modules.Corporation.repositories.CorporationRepository;
import com.domingueti.upfine.modules.User.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class InsertDesiredCorporationsValidator {

    private Map<String, String> fieldErrors;
    private Boolean validInsert;

    @Autowired
    private CorporationRepository corporationRepository;

    @Autowired
    private UserRepository userRepository;

    public void execute(ChooseCorporationDTO chooseCorporationDTO) {
        fieldErrors = new HashMap<>();
        validInsert = true;

        for (Long corporationId : chooseCorporationDTO.getCorporationIds()) {
            if(!corporationRepository.existsById(corporationId)) {
                fieldErrors.put("corporation.id", "Corporation ID does not exist");
                validInsert = false;
            }
        }

        if (!userRepository.existsByEmail(chooseCorporationDTO.getEmail())) {
            fieldErrors.put("email", "User email does not exist");
            validInsert = false;
        }

        if (!validInsert) {
            InvalidRequestException exception = new InvalidRequestException("Erro ao validar os dados inseridos");

            fieldErrors.forEach((field, message) -> exception.getFields().add(new FieldMessage(field, message)));

            throw exception;
        }


    }
}
