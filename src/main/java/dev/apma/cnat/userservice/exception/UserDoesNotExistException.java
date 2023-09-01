package dev.apma.cnat.userservice.exception;


public final class UserDoesNotExistException extends Exception {

    public UserDoesNotExistException() {
        super();
    }

    public UserDoesNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserDoesNotExistException(String message) {
        super(message);
    }

    public UserDoesNotExistException(Throwable cause) {
        super(cause);
    }
}
