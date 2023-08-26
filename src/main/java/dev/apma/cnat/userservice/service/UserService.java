package dev.apma.cnat.userservice.service;


import dev.apma.cnat.userservice.exception.UserAlreadyExistsException;
import dev.apma.cnat.userservice.exception.UserAuthenticationFailException;
import dev.apma.cnat.userservice.request.UserAuthRequest;
import dev.apma.cnat.userservice.request.UserDeleteRequest;
import dev.apma.cnat.userservice.request.UserRegisterRequest;

public interface UserService {
    void register(UserRegisterRequest req) throws UserAlreadyExistsException;

    void authenticate(UserAuthRequest req) throws UserAuthenticationFailException;

    void delete(UserDeleteRequest req);
}
