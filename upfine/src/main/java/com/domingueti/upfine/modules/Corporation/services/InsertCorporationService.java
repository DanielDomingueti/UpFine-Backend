package com.domingueti.upfine.modules.Corporation.services;

import com.domingueti.upfine.modules.Corporation.dtos.CorporationDTO;
import com.domingueti.upfine.modules.Corporation.models.Corporation;
import com.domingueti.upfine.modules.Corporation.repositories.CorporationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class InsertCorporationService {

    final private CorporationRepository corporationRepository;

    @Transactional
    public CorporationDTO execute(String ipeCorporationCnpj, String ipeCorporationName) {
        Corporation corporation = new Corporation();
        corporation.setCnpj(ipeCorporationCnpj);
        corporation.setName(ipeCorporationName);
        return new CorporationDTO(corporationRepository.save(corporation));
    }
}
