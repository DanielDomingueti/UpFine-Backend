package com.domingueti.upfine.modules.Corporation.dtos;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class ChooseCorporationDTO {

    @NotNull(message = "Name is mandatory")
    private @Getter @Setter String name;

    @Email(message = "Invalid email")
    private @Getter @Setter String email;

    @Size(min = 1, message = "At least one corporation should be chosen")
    private @Getter @Setter List<Long> corporationIds;


}
