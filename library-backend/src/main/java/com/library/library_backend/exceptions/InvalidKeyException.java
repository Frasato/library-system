package com.library.library_backend.exceptions;

public class InvalidKeyException extends RuntimeException {
    public InvalidKeyException() {
        super("KEY can't be empty");
    }
}
