package dev.apma.cnat.userservice.dto;


import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

/**
 * This DTO class represents the validation error format returned from all CNAT services.
 *
 * @author Amir Parsa Mahdian
 */
public record ValidationErrorsDTO(List<FieldValidationError> validationErrors) {
    record FieldValidationError(String field, String error) {
        static FieldValidationError fromFieldError(FieldError error) {
            return new FieldValidationError(error.getField(),
                    error.getDefaultMessage() == null ? "" : error.getDefaultMessage());
        }
    }

    /**
     * Returns a {@code ValidationErrorsDTO} representation of the {@code MethodArgumentNotValidException}.
     *
     * @param ex the {@code MethodArgumentNotValidException} to be mapped
     * @return a {@code ValidationErrorsDTO} representation of the {@code MethodArgumentNotValidException}
     */
    public static ValidationErrorsDTO fromFieldError(MethodArgumentNotValidException ex) {
        return new ValidationErrorsDTO(ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldValidationError::fromFieldError)
                .toList());
    }
}
