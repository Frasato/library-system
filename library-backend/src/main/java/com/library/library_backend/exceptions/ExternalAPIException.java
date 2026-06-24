package com.library.library_backend.exceptions;

public class ExternalAPIException extends RuntimeException {
    public ExternalAPIException(String message) {
        super(message);
    }
}
