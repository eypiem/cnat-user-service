package dev.apma.cnat.userservice.exception;


/**
 * This {@code Exception} indicates a situation where a requested user does not exist.
 *
 * @author Amir Parsa Mahdian
 */
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
