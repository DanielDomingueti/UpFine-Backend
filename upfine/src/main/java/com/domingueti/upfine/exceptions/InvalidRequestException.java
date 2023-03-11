package com.domingueti.upfine.exceptions;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class InvalidRequestException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private @Getter List<FieldMessage> fields = new ArrayList<>();

    public InvalidRequestException(String message) {
        super(message);
    }
}