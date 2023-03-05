package com.domingueti.upfine.modules.Ipe.dtos;

import com.domingueti.upfine.modules.Ipe.models.Ipe;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
public class IpeDTO {

    private @Getter @Setter Long id;

    private @Getter @Setter Long corporationId;

    private @Getter @Setter String subject;

    private @Getter @Setter String link;

    private @Getter @Setter LocalDate referenceDate;

    public IpeDTO(Ipe ipe) {
        this.id = ipe.getId();
        this.corporationId = ipe.getCorporationId();
        this.subject = ipe.getSubject();
        this.link = ipe.getLink();
        this.referenceDate = ipe.getReferenceDate();
    }

}
