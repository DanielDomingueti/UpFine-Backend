package com.domingueti.upfine.modules.RelevantFact.dtos;

import com.domingueti.upfine.modules.RelevantFact.daos.RelevantFactIpeDAO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

import static com.domingueti.upfine.utils.statics.ConvertCnpj.rawToFormatted;

@AllArgsConstructor
public class RelevantFactIpeDTO {

    private @Getter @Setter String userName;

    private @Getter @Setter String corporation;

    private @Getter @Setter String cnpj;

    private @Getter @Setter String subject;

    private @Getter @Setter String summarized;

    private @Getter @Setter LocalDate referenceDate;

    public RelevantFactIpeDTO(RelevantFactIpeDAO relevantFactIpeDAO) {
        this.userName = relevantFactIpeDAO.getUserName();
        this.corporation = relevantFactIpeDAO.getCorporation();
        this.subject = relevantFactIpeDAO.getSubject();
        this.summarized = relevantFactIpeDAO.getSummarized();
        this.referenceDate = relevantFactIpeDAO.getReferenceDate();
        this.cnpj = rawToFormatted(relevantFactIpeDAO.getCnpj());
    }

}
