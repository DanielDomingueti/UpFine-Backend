package com.domingueti.upfine.modules.RelevantFact.daos;

import java.time.LocalDate;

public interface RelevantFactIpeDAO {

    String getUser();

    String getCorporation();

    String getCnpj();

    String getSubject();

    LocalDate getDate();

    String getSummarized();

    String getLink();

}
