package com.library.library_backend.exceptions;

public class InvalidIDException extends RuntimeException {
    public InvalidIDException() {
        super("ID can't be empty");
    }
}
