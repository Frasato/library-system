package com.library.library_backend.exceptions;

public class InvalidISBNException extends RuntimeException {
    public InvalidISBNException() {
        super("ISBN can't be empty");
    }
}
