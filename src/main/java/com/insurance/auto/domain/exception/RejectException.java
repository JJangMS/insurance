package com.insurance.auto.domain.exception;

public class RejectException extends RuntimeException {
    public RejectException(String message) {
        super(message);
    }
}
