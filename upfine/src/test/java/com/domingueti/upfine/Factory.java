package com.domingueti.upfine;

import java.sql.Timestamp;
import java.time.LocalDate;

import static java.time.Instant.now;
import static java.time.LocalDate.of;

public class Factory {

    private final static LocalDate referenceDate = of(2023, 2, 23);
    private final static Timestamp localTimestamp = Timestamp.from(now());

//    public static User createUser() {
//        return new User(1L, "name", "email@test.com", referenceDate, localTimestamp, localTimestamp, null);
//    }

}
