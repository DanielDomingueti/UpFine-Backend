package com.domingueti.upfine.exceptions;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ValidationResponse extends ExceptionResponse{
    private static final long serialVersionUID = 1L;

    private @Getter List<FieldMessage> errors = new ArrayList<>();

    public ValidationResponse(Date timestamp, String error, Integer status, String message, String path) {
        super(timestamp, error, status, message, path);
    }

    public void addError(String fieldName, String message) {

        errors.add(new FieldMessage(fieldName, message));
    }

}