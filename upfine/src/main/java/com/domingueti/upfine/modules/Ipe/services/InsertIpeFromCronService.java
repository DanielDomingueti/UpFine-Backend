package com.domingueti.upfine.modules.Ipe.services;

import com.domingueti.upfine.exceptions.BusinessException;
import com.domingueti.upfine.modules.Corporation.dtos.CorporationDTO;
import com.domingueti.upfine.modules.Corporation.models.Corporation;
import com.domingueti.upfine.modules.Corporation.repositories.CorporationRepository;
import com.domingueti.upfine.modules.Corporation.services.InsertCorporationService;
import com.domingueti.upfine.modules.Ipe.dtos.IpeDTO;
import com.domingueti.upfine.modules.Ipe.models.Ipe;
import com.domingueti.upfine.modules.Ipe.repositories.IpeRepository;
import com.domingueti.upfine.utils.statics.ConvertCnpj;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@AllArgsConstructor
public class InsertIpeFromCronService {

    final private CorporationRepository corporationRepository;

    final private IpeRepository ipeRepository;

    final private InsertCorporationService insertCorporationService;


    public IpeDTO execute(String[] ipeArray, LocalDate ipeReferenceDate) {
        try {
            final String ipeCorporationCnpj = ConvertCnpj.formattedToRaw(ipeArray[0]);
            final String ipeCorporationName = ipeArray[1];
            final String ipeSubject = ipeArray[7];
            final String ipeLink = ipeArray[12];
            final Long ipeCorporationId = defineCorporationId(ipeCorporationCnpj, ipeCorporationName);

            ipeRepository.deleteRepeated(ipeCorporationId, ipeSubject, ipeLink, ipeReferenceDate);

            final Ipe newIpe = createAndSaveIpe(ipeSubject, ipeLink, ipeCorporationId, ipeReferenceDate);
            return new IpeDTO(newIpe);
        }
        catch(Exception e) {
            throw new BusinessException("Error while inserting Ipe from CRON: " + e.getMessage());
        }

    }

    private Ipe createAndSaveIpe(String ipeSubject, String ipeLink, Long ipeCorporationId, LocalDate ipeReferenceDate) {
        Ipe ipe = new Ipe();
        ipe.setSubject(ipeSubject);
        ipe.setLink(ipeLink);
        ipe.setCorporationId(ipeCorporationId);
        ipe.setReferenceDate(ipeReferenceDate);
        return ipeRepository.save(ipe);
    }

    private Long defineCorporationId(String ipeCorporationCnpj, String ipeCorporationName) {
        final Optional<Corporation> corporationOptional = corporationRepository.findByCnpjAndName(ipeCorporationCnpj, ipeCorporationName);
        if (corporationOptional.isPresent()) {
            return corporationOptional.get().getId();
        }

        final CorporationDTO newCorporationDTO = insertCorporationService.execute(ipeCorporationCnpj, ipeCorporationName);
        return newCorporationDTO.getId();
    }

}
