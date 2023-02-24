package com.domingueti.upfine.modules.Cron.relevantfact.daos;

import java.time.LocalDate;

public interface RelevantFactIpeDAO {

    String getName();

    String getCnpj();

    String getSubject();

    LocalDate getReferenceDate();

    String getSummarizedText();

}
