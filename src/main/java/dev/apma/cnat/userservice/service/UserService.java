package dev.apma.cnat.userservice.service;


import dev.apma.cnat.userservice.dto.UserDTO;
import dev.apma.cnat.userservice.exception.UserAlreadyExistsException;
import dev.apma.cnat.userservice.exception.UserAuthenticationFailException;
import dev.apma.cnat.userservice.exception.UserDoesNotExistException;
import dev.apma.cnat.userservice.request.UserAuthRequest;
import dev.apma.cnat.userservice.request.UserDeleteRequest;
import dev.apma.cnat.userservice.request.UserRegisterRequest;

public interface UserService {
    void register(UserRegisterRequest req) throws UserAlreadyExistsException;

    UserDTO find(String email) throws UserDoesNotExistException;

    void authenticate(UserAuthRequest req) throws UserAuthenticationFailException, UserDoesNotExistException;

    void delete(UserDeleteRequest req) throws UserAuthenticationFailException;
}
