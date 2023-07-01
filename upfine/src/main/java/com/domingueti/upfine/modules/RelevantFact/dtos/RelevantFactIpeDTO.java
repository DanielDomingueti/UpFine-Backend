package com.domingueti.upfine.modules.RelevantFact.dtos;

import com.domingueti.upfine.modules.RelevantFact.daos.RelevantFactIpeDAO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

import static com.domingueti.upfine.utils.statics.ConvertCnpj.rawToFormatted;

@AllArgsConstructor
public class RelevantFactIpeDTO {

    private @Getter @Setter String user;

    private @Getter @Setter String corporation;

    private @Getter @Setter String cnpj;

    private @Getter @Setter String subject;

    private @Getter @Setter String summarized;

    private @Getter @Setter String link;

    private @Getter @Setter LocalDate date;

    public RelevantFactIpeDTO(RelevantFactIpeDAO relevantFactIpeDAO) {
        final int endNameIndex = relevantFactIpeDAO.getUser().indexOf(" ");

        this.user = relevantFactIpeDAO.getUser().substring(0, endNameIndex);
        this.corporation = relevantFactIpeDAO.getCorporation();
        this.subject = relevantFactIpeDAO.getSubject();
        this.summarized = relevantFactIpeDAO.getSummarized();
        this.link = relevantFactIpeDAO.getLink();
        this.date = relevantFactIpeDAO.getDate();
        this.cnpj = rawToFormatted(relevantFactIpeDAO.getCnpj());
    }

}
