package dev.apma.cnat.userservice.exception;


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
