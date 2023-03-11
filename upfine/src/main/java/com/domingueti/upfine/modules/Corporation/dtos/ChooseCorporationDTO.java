package com.domingueti.upfine.modules.Corporation.dtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ChooseCorporationDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Email(message = "Invalid email")
    private @Getter @Setter String email;

    @Size(min = 1, message = "At least one corporation should be chosen")
    private @Getter List<Long> corporationIds = new ArrayList<>();


}
