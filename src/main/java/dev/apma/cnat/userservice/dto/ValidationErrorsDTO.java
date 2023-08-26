package dev.apma.cnat.userservice.dto;


import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

public record ValidationErrorsDTO(List<FieldValidationError> validationErrors) {
    record FieldValidationError(String field, String error) {
        static FieldValidationError fromFieldError(FieldError error) {
            return new FieldValidationError(error.getField(),
                    error.getDefaultMessage() == null ? "" : error.getDefaultMessage());
        }
    }

    public static ValidationErrorsDTO fromFieldError(MethodArgumentNotValidException ex) {
        return new ValidationErrorsDTO(ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldValidationError::fromFieldError)
                .toList());
    }
}
