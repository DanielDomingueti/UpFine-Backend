package com.domingueti.upfine.modules.RelevantFact.daos;

import java.time.LocalDate;

public interface RelevantFactIpeDAO {

    String getUserName();

    String getCorporation();

    String getCnpj();

    String getSubject();

    LocalDate getReferenceDate();

    String getSummarized();

}
