package dev.apma.cnat.userservice.exception;


/**
 * This {@code Exception} indicates a situation where an authentication request has failed.
 *
 * @author Amir Parsa Mahdian
 */
public final class UserAuthenticationFailException extends Exception {

    public UserAuthenticationFailException() {
        super();
    }

    public UserAuthenticationFailException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserAuthenticationFailException(String message) {
        super(message);
    }

    public UserAuthenticationFailException(Throwable cause) {
        super(cause);
    }
}
