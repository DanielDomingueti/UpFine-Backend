package com.domingueti.upfine.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@ControllerAdvice
public class CustomExceptionResponseHandler {

    @ExceptionHandler(InvalidRequestException.class)
    public final ResponseEntity<ValidationResponse> handleInvalidRequestException(InvalidRequestException ex, HttpServletRequest request) {
        final HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;

        ValidationResponse response = new ValidationResponse(new Date(), "Invalid Request", status.value(),
                ex.getMessage(), request.getRequestURI());

        for (FieldMessage f : ex.getFields()) {
            response.addError(f.getFieldName(), f.getMessage());
        }

        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(BusinessException.class)
    public final ResponseEntity<ExceptionResponse> businessInvalidRequestException(BusinessException ex, HttpServletRequest request) {
        final HttpStatus status = HttpStatus.BAD_REQUEST;

        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), "Bad Request", status.value(),
                ex.getMessage(), request.getRequestURI());

        return ResponseEntity.status(status).body(exceptionResponse);
    }

    @ExceptionHandler(NotFoundException.class)
    public final ResponseEntity<ExceptionResponse> notFoundException(NotFoundException ex, HttpServletRequest request) {
        final HttpStatus status = HttpStatus.NOT_FOUND;

        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), "Not found", status.value(),
                ex.getMessage(), request.getRequestURI());

        return ResponseEntity.status(status).body(exceptionResponse);
    }

    public final ResponseEntity<ExceptionResponse> forbiddenException(ForbiddenException ex, HttpServletRequest request) {
        final HttpStatus status = HttpStatus.FORBIDDEN;

        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), "Forbidden", status.value(),
                ex.getMessage(), request.getRequestURI());

        return ResponseEntity.status(status).body(exceptionResponse);
    }

}
