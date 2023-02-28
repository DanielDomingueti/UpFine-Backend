package com.domingueti.upfine.modules.RelevantFact.dtos;

import com.domingueti.upfine.modules.RelevantFact.daos.RelevantFactIpeDAO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RelevantFactIpeHtmlDTO {

    private String name;

    private String cnpj;

    private String subject;

    private String summarizedText;

    public RelevantFactIpeHtmlDTO(RelevantFactIpeDAO dao) {
        name = dao.getName();
        cnpj = dao.getCnpj();
        subject = dao.getSubject();
        summarizedText = dao.getSummarizedText();
    }

}
