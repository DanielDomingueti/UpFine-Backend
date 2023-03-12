package com.domingueti.upfine;

import com.domingueti.upfine.modules.Corporation.dtos.ChooseCorporationDTO;
import com.domingueti.upfine.modules.Corporation.models.Corporation;
import com.domingueti.upfine.modules.User.models.User;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import static java.time.Instant.now;
import static java.time.LocalDate.of;

public class Factory {

    private final static LocalDate referenceDate = of(2023, 2, 23);
    private final static Timestamp localTimestamp = Timestamp.from(now());

    public static Corporation createCorporation() {
        return new Corporation(1L, "86780313000125", "Corporation name s.a", localTimestamp, localTimestamp, null, new ArrayList<>(), new ArrayList<>());
    }

    public static User createUser() {
        return new User(1L, "name", "email@test.com", true, referenceDate, localTimestamp, localTimestamp, null, new ArrayList<>(Arrays.asList(createCorporation())));
    }

    public static ChooseCorporationDTO createChooseCorporationDTO() {
        return new ChooseCorporationDTO("test@gmail.com", Arrays.asList(1L));
    }
}
