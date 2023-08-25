package dev.apma.cnat.userservice.response;


import org.springframework.validation.FieldError;

public record FieldValidationError(String field, String error) {

    public static FieldValidationError fromFieldError(FieldError error) {
        return new FieldValidationError(error.getField(),
                error.getDefaultMessage() == null ? "" : error.getDefaultMessage());
    }
}
