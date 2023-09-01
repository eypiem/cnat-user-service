package dev.apma.cnat.userservice.exception;


/**
 * This {@code Exception} indicates a situation where a user with the requested email already exists.
 *
 * @author Amir Parsa Mahdian
 */
public final class UserAlreadyExistsException extends Exception {

    public UserAlreadyExistsException() {
        super();
    }

    public UserAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserAlreadyExistsException(String message) {
        super(message);
    }

    public UserAlreadyExistsException(Throwable cause) {
        super(cause);
    }
}
