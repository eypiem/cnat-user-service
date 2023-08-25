package dev.apma.cnat.userservice.service;


import dev.apma.cnat.userservice.requests.UserAuthRequest;
import dev.apma.cnat.userservice.requests.UserDeleteRequest;
import dev.apma.cnat.userservice.requests.UserRegisterRequest;

public interface UserService {
    void register(UserRegisterRequest req);

    boolean authenticate(UserAuthRequest req);

    void delete(UserDeleteRequest req);
}
