package com.api.admarket.api.v1.exeption;

public class UserAccountStateException extends RuntimeException {
    public UserAccountStateException() {
        super();
    }

    public UserAccountStateException(String message) {
        super(message);
    }

    public UserAccountStateException(Throwable cause) {
        super(cause);
    }
}
