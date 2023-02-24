package com.domingueti.upfine;

import com.domingueti.upfine.modules.Email.models.Email;

import java.sql.Timestamp;
import java.time.LocalDate;

import static java.time.Instant.now;
import static java.time.LocalDate.of;

public class Factory {

    private final static LocalDate referenceDate = of(2023, 02, 23);
    private final static Timestamp localTimestamp = Timestamp.from(now());

    public static Email createEmail() {
        return new Email(1L, "email@test.com", referenceDate, localTimestamp, localTimestamp, null);
    }

}
