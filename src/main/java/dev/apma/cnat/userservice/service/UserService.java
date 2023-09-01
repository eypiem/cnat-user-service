package dev.apma.cnat.userservice.service;


import dev.apma.cnat.userservice.dto.UserDTO;
import dev.apma.cnat.userservice.exception.UserAlreadyExistsException;
import dev.apma.cnat.userservice.exception.UserAuthenticationFailException;
import dev.apma.cnat.userservice.exception.UserDoesNotExistException;
import dev.apma.cnat.userservice.request.UserAuthRequest;
import dev.apma.cnat.userservice.request.UserDeleteRequest;
import dev.apma.cnat.userservice.request.UserRegisterRequest;

/**
 * This interface represents a service for performing operations on {@code UserRepository}.
 *
 * @author Amir Parsa Mahdian
 * @see dev.apma.cnat.userservice.repository.UserRepository
 */
public interface UserService {
    /**
     * Registers a new user.
     *
     * @param req the {@code UserRegisterRequest} containing the registration information
     * @throws UserAlreadyExistsException if another user is already registered with the requested email
     */
    void register(UserRegisterRequest req) throws UserAlreadyExistsException;

    /**
     * Returns the user having the provided email.
     *
     * @param email the user's email
     * @return the user having the provided email
     * @throws UserDoesNotExistException if a user with the provided email does not exist
     */
    UserDTO find(String email) throws UserDoesNotExistException;

    /**
     * Authenticates a user.
     *
     * @param req the {@code UserAuthRequest} containing the authentication information
     * @throws UserAuthenticationFailException if the request fails the authentication
     * @throws UserDoesNotExistException       if a user with the provided email does not exist
     */
    void authenticate(UserAuthRequest req) throws UserAuthenticationFailException, UserDoesNotExistException;

    /**
     * Deletes a user.
     *
     * @param req the {@code UserDeleteRequest} containing the deletion information
     * @throws UserAuthenticationFailException if the request fails the authentication
     */
    void delete(UserDeleteRequest req) throws UserAuthenticationFailException;
}
