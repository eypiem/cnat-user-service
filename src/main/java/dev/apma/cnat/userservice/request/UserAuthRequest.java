package dev.apma.cnat.userservice.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserAuthRequest(@Email(regexp = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)"
                                              + "+[a-zA-Z]{2,}$", message = "Email format is not valid") String email,
                              @Size(min = 8, max = 64, message = "Password should be between 8 and 64 characters") String password) {
}
