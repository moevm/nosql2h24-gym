package com.example.gym.exception;

public class UniquenessViolationException extends RuntimeException {

    public UniquenessViolationException(String message) {
        super(message);
    }

    public UniquenessViolationException(String message, Throwable cause) {
        super(message, cause);
    }

    public UniquenessViolationException(Throwable cause) {
        super(cause);
    }

}
