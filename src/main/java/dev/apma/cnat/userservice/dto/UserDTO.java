package dev.apma.cnat.userservice.dto;


/**
 * This DTO class represents the user format returned from CNAT User Service.
 *
 * @author Amir Parsa Mahdian
 */
public record UserDTO(String email, String firstName, String lastName) {
}
