package com.domingueti.upfine.modules.Corporation.validators;

import com.domingueti.upfine.exceptions.FieldMessage;
import com.domingueti.upfine.exceptions.InvalidRequestException;
import com.domingueti.upfine.modules.Corporation.dtos.ChooseCorporationDTO;
import com.domingueti.upfine.modules.Corporation.repositories.CorporationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@AllArgsConstructor
public class InsertUserChosenCorporationsValidator {

    private CorporationRepository corporationRepository;

    public void execute(ChooseCorporationDTO chooseCorporationDTO) {

        Map<String, String> fieldErrors = new HashMap<>();
        Boolean validInsert = true;

        for (Long corporationId : chooseCorporationDTO.getCorporationIds()) {
            if(!corporationRepository.existsById(corporationId)) {
                fieldErrors.put("corporation.id", "Corporation ID does not exist");
                validInsert = false;
            }
        }

        if (!validInsert) {
            InvalidRequestException exception = new InvalidRequestException("Erro ao validar os dados inseridos");

            fieldErrors.forEach((field, message) -> exception.getFields().add(new FieldMessage(field, message)));

            throw exception;
        }
    }
}
