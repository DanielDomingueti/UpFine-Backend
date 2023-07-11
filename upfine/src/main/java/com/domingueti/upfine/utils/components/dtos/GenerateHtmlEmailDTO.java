package com.domingueti.upfine.utils.components.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class GenerateHtmlEmailDTO {

    private @Getter @Setter String user;

    private @Getter @Setter String date;

    public GenerateHtmlEmailDTO(String user, LocalDate date) {
        this.user = user;
        this.date = date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }
}